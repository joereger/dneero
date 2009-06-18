package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.*;
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
import twitter4j.RateLimitStatus;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 * Just adding this line to test SVN 1.6 + Idea 8.1.3
 */

public class CollectTwitterAnswers implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() PendingToOpenSurveys called");
            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                               .addOrder(Order.asc("plid"))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
                Pl pl=plIterator.next();
                if (pl.getTwitterusername()!=null && pl.getTwitterusername().length()>0 && pl.getTwitterpassword()!=null && pl.getTwitterpassword().length()>0){
                    collectReplies(pl);
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void collectReplies(Pl pl){
        Logger logger = Logger.getLogger(CollectTwitterAnswers.class);
        try{
            Twitter twitter = new Twitter(pl.getTwitterusername(),pl.getTwitterpassword());
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
            //Report on RateLimitStatus
            RateLimitStatus rls = twitter.rateLimitStatus();
            logger.debug("Twitter RateLimitStatus: hourlylimit="+rls.getHourlyLimit()+" remaininghits="+rls.getRemainingHits()+" resettimeinseconds="+rls.getResetTimeInSeconds()+" datetime="+Time.dateformatcompactwithtime(Time.getCalFromDate(rls.getDateTime())));
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
            if (twitask!=null){
                int respondentssofar = NumFromUniqueResult.getInt("select count(*) from Twitanswer where twitaskid='"+twitask.getTwitaskid()+"' and status='"+Twitanswer.STATUS_APPROVED+"'");
                if (respondentssofar>=twitask.getNumberofrespondentsrequested()){
                    istoolate = true;
                }
            }
            //See if it's alreadyanswered
            boolean isalreadyanswered = false;
            if (twitask!=null){
                int answers = NumFromUniqueResult.getInt("select count(*) from Twitanswer where twitaskid='"+twitask.getTwitaskid()+"' and status='"+Twitanswer.STATUS_APPROVED+"' and userid='"+userid+"'");
                if (answers>0){
                    isalreadyanswered = true;
                }
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
            twitanswer.setTwitterfollowerscount(status.getUser().getFollowersCount());
            twitanswer.setStatus(Twitanswer.STATUS_PENDINGREVIEW);
            if (istoolate){
                twitanswer.setStatus(Twitanswer.STATUS_TOOLATE);
            }
            if (isalreadyanswered){
                twitanswer.setStatus(Twitanswer.STATUS_ALREADYANSWERED);
            }
            if (!iscriteriaxmlqualified){
                twitanswer.setStatus(Twitanswer.STATUS_DOESNTQUALIFY);
            }
            if (user!=null && user.getUserid()>0 && user.getBloggerid()==0){
                twitanswer.setStatus(Twitanswer.STATUS_NOBLOGGER);
            }
            if (twitaskid==0){
                twitanswer.setStatus(Twitanswer.STATUS_NOTWITASK);
            }
            twitanswer.setTwitaskid(twitaskid);
            twitanswer.setTwittercreatedate(status.getCreatedAt());
            twitanswer.setTwitterid(status.getId());
            twitanswer.setTwitterinreplytostatusid(status.getInReplyToStatusId());
            twitanswer.setTwittertext(status.getText());
            twitanswer.setTwitterprofileimageurl(status.getUser().getProfileImageURL().toString());
            twitanswer.setTwitterusername(status.getUser().getScreenName());
            int twitaskincentiveid = 0;
            if (twitask!=null){
               twitaskincentiveid = twitask.getIncentive().getTwitaskincentive().getTwitaskincentiveid();
            }
            twitanswer.setTwitaskincentiveid(twitaskincentiveid);
            try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
            //Send status messages
            sentTwitterDMAAfterCollect(twitanswer);
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

    public static void sentTwitterDMAAfterCollect(Twitanswer twitanswer){
        Logger logger = Logger.getLogger(CollectTwitterAnswers.class);
        if (twitanswer.getUserid()==0){
            //Send msg that they need to create a dNeero account
            String msg = "Thanks for answering! We don't have your twitter account on record. Please go to http://dNeero.com and sign up.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, null);
            return;
        }

        if (twitanswer.getTwitaskid()==0){
            //Send msg that they need to reply to a specific question
            String msg = "Thanks for answering... but we can't use your response... you need to Reply to a specific question.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, null);
            return;
        }

        if (twitanswer.getStatus()==Twitanswer.STATUS_PENDINGREVIEW){
            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
            User user = User.get(twitask.getUserid());
            Pl pl = Pl.get(user.getPlid());
            String dotdotdot = "";
            if (twitask.getQuestion().length()>50){ dotdotdot = "..."; }
            String msg = "Thanks for answering: \""+ Str.truncateString(twitask.getQuestion(), 50)+dotdotdot+"\" It's pending review. Log in to your http://dNeero.com account for status.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, pl);
            return;
        }

        if (twitanswer.getStatus()==Twitanswer.STATUS_DOESNTQUALIFY){
            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
            User user = User.get(twitask.getUserid());
            Pl pl = Pl.get(user.getPlid());
            String dotdotdot = "";
            if (twitask.getQuestion().length()>50){ dotdotdot = "..."; }
            String msg = "Sorry, you didn't qualify for: \""+ Str.truncateString(twitask.getQuestion(), 50)+dotdotdot+"\" Check http://dNeero.com for status.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, pl);
            return;
        }

        if (twitanswer.getStatus()==Twitanswer.STATUS_ALREADYANSWERED){
            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
            User user = User.get(twitask.getUserid());
            Pl pl = Pl.get(user.getPlid());
            String dotdotdot = "";
            if (twitask.getQuestion().length()>50){ dotdotdot = "..."; }
            String msg = "Sorry, you've already answered: \""+ Str.truncateString(twitask.getQuestion(), 50)+dotdotdot+"\" Check http://dNeero.com for status.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, pl);
            return;
        }

        if (twitanswer.getStatus()==Twitanswer.STATUS_TOOLATE){
            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
            User user = User.get(twitask.getUserid());
            Pl pl = Pl.get(user.getPlid());
            String dotdotdot = "";
            if (twitask.getQuestion().length()>50){ dotdotdot = "..."; }
            String msg = "Sorry, question closed already: \""+ Str.truncateString(twitask.getQuestion(), 50)+dotdotdot+"\" Check http://dNeero.com for status.";
            sendTwitterDM(twitanswer.getTwitterusername(), msg, pl);
            return;
        }
    }

    public static void sendTwitterDM(String twitterusername, String msg, Pl pl){
        Logger logger = Logger.getLogger(CollectTwitterAnswers.class);
        try{
            if (pl==null || pl.getPlid()==0){
                pl = Pl.get(1);
            }
            Twitter twitter = new Twitter(pl.getTwitterusername(),pl.getTwitterpassword());
            RateLimitStatus rls = twitter.rateLimitStatus();
            logger.debug("Twitter RateLimitStatus: hourlylimit="+rls.getHourlyLimit()+" remaininghits="+rls.getRemainingHits()+" resettimeinseconds="+rls.getResetTimeInSeconds()+" datetime="+Time.dateformatcompactwithtime(Time.getCalFromDate(rls.getDateTime())));
            DirectMessage message = twitter.sendDirectMessage(twitterusername, msg);
        } catch (Exception ex){ logger.error("",ex); }
    }

}