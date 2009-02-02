package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;
import com.dneero.dao.Twitanswer;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.util.GeneralException;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.util.Str;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;
import com.dneero.finders.SurveyCriteriaXML;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;

import twitter4j.Twitter;
import twitter4j.Status;
import twitter4j.DirectMessage;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class CollectTwitterAnswers implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() PendingToOpenSurveys called");
            collectReplies();
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void collectReplies(){
        Logger logger = Logger.getLogger(CollectTwitterAnswers.class);
        try{
            //@todo hold twitter credentials in private label
            Twitter twitter = new Twitter("dneero","physics");
            for(int i=1; i<100; i++){
                logger.debug("i="+i);
                List<Status> statuses = twitter.getRepliesByPage(i);
                if (statuses==null || statuses.size()<=0){
                    logger.debug("statuses==null || statuses.size()<=0 so breaking out of i 1->100 loop");
                    //Save api calls
                    break;
                }
                logger.debug("statuses.size()="+statuses.size());
                boolean foundonealreadyindb = false;
                for (Status status : statuses) {
                    logger.debug("start processing status: "+status.getText());
                    if (!doesTwitanswerAlreadyExist(status)){
                        logger.debug("status doesn't exist in dNeero so will process");
                        processTwitterStatus(status);
                    } else {
                        logger.debug("status already exists in dNeero db");
                        foundonealreadyindb = true;
                    }
                }
                if (foundonealreadyindb){
                    logger.debug("because twitter sent a status that's already in dNeero db i'm assuming that all furthers will exist too so breaking out of i 1->100 loop");
                    //Save api calls
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public static void processTwitterStatus(Status status){
        Logger logger = Logger.getLogger(CollectTwitterAnswers.class);
        if (!doesTwitanswerAlreadyExist(status)){
            //Get the question
            Twitask twitask = getTwitaskByReplyToId(status.getInReplyToStatusId());
            int twitaskid = 0;
            if (twitask!=null){
                twitaskid = twitask.getTwitaskid();
            }
            //See if a user exists
            User user = getUserByTwitterUsername(status.getUser().getScreenName());
            int userid = 0;
            if (user!=null){
                userid=user.getUserid();
            }
            //See if the user qualifies
            boolean iscriteriaxmlqualified = false;
            if (user!=null){
                SurveyCriteriaXML scXML = new SurveyCriteriaXML(twitask.getCriteriaxml());
                if (scXML.isUserQualified(user)){
                    iscriteriaxmlqualified = true;
                }
            }
            //See if it's too late
            boolean istoolate = false;
            int respondentssofar = NumFromUniqueResult.getInt("select count(*) from Twitanswer where twitaskid='"+twitask.getTwitaskid()+"' and status='"+Twitanswer.STATUS_APPROVED+"'");
            if (respondentssofar>=twitask.getNumberofrespondentsrequested()){
                istoolate = true;
            }
            //Record the answer
            Twitanswer twitanswer = new Twitanswer();
            twitanswer.setUserid(userid);
            twitanswer.setAnswer(status.getText());
            twitanswer.setIscriteriaxmlqualified(iscriteriaxmlqualified);
            twitanswer.setCharityname("");
            twitanswer.setIsforcharity(false);
            twitanswer.setIspaid(false);
            twitanswer.setIsresearcherrejected(false);
            twitanswer.setIsresearcherreviewed(false);
            twitanswer.setIssysadminrejected(false);
            twitanswer.setIssysadminreviewed(false);
            twitanswer.setResponsedate(status.getCreatedAt());
            twitanswer.setScorebyresearcher(0);
            twitanswer.setScorebysysadmin(0);
            twitanswer.setStatus(Twitanswer.STATUS_PENDINGREVIEW);
            if (istoolate){
                twitanswer.setStatus(Twitanswer.STATUS_TOOLATE);
            }
            if (!iscriteriaxmlqualified){
                twitanswer.setStatus(Twitanswer.STATUS_DOESNTQUALIFY);
            }
            twitanswer.setTwitaskid(twitaskid);
            twitanswer.setTwittercreatedate(status.getCreatedAt());
            twitanswer.setTwitterid(status.getId());
            twitanswer.setTwitterinreplytostatusid(status.getInReplyToStatusId());
            twitanswer.setTwittertext(status.getText());
            twitanswer.setTwitterprofileimageurl(status.getUser().getProfileImageURL().toString());
            twitanswer.setTwitterusername(status.getUser().getScreenName());
            twitanswer.setTwitaskincentiveid(twitask.getIncentive().getTwitaskincentive().getTwitaskincentiveid());
            try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
            //Send direct message acknowledging
            try{
                Twitter twitter = new Twitter("dneero","physics");
                String dotdotdot = "";
                if (twitask.getQuestion().length()>50){
                    dotdotdot = "...";
                }
                String msg = "Thanks for answering: \""+ Str.truncateString(twitask.getQuestion(), 50)+dotdotdot+"\" Check or create your http://dNeero.com account for status.";
                DirectMessage message = twitter.sendDirectMessage(status.getUser().getScreenName(), msg);
            } catch (Exception ex){ logger.error("",ex); }
        }
    }



    public static boolean doesTwitanswerAlreadyExist(Status status){
        List<Twitanswer> twitanswers = HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("twitterid", status.getId()))
                                           .setCacheable(true)
                                           .list();
        if (twitanswers!=null && twitanswers.size()>0){
            return true;
        } else {
            return false;
        }
    }

    public static User getUserByTwitterUsername(String twitterusername){
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("instantnotifytwitterusername", twitterusername.trim()))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>0){
            return users.get(0);
        } else {
            return null;
        }
    }

    public static Twitask getTwitaskByReplyToId(long inreplytostatusid){
        List<Twitask> twitasks = HibernateUtil.getSession().createCriteria(Twitask.class)
                                           .add(Restrictions.eq("twitterid", inreplytostatusid))
                                           .setCacheable(true)
                                           .list();
        if (twitasks!=null && twitasks.size()>0){
            return twitasks.get(0);
        } else {
            return null;
        }
    }

}