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
import com.dneero.util.GeneralException;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;

import twitter4j.Twitter;
import twitter4j.Status;

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
            for(int i=0; i<100; i++){
                List<Status> statuses = twitter.getRepliesByPage(i);
                logger.debug("statuses.size()="+statuses.size());
                if (statuses==null || statuses.size()<=0){
                    //Save api calls
                    break;
                }
                boolean foundonealreadyindb = false;
                for (Status status : statuses) {
                    if (!doesTwitanswerAlreadyExist(status)){
                        processTwitterStatus(status);
                    } else {
                        foundonealreadyindb = true;
                    }
                }
                if (foundonealreadyindb){
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
            User user = getUserByTwitterUsername(status.getUser().getScreenName());
            int userid = 0;
            if (user!=null){
                userid=user.getUserid();
            }
            Twitask twitask = getTwitaskByReplyToId(status.getInReplyToStatusId());
            int twitaskid = 0;
            if (twitask!=null){
                twitaskid = twitask.getTwitaskid();
            }
            Twitanswer twitanswer = new Twitanswer();
            twitanswer.setUserid(userid);
            twitanswer.setAnswer(status.getText());
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
            twitanswer.setTwitaskid(twitaskid);
            twitanswer.setTwittercreatedate(status.getCreatedAt());
            twitanswer.setTwitterid(status.getId());
            twitanswer.setTwitterinreplytostatusid(status.getInReplyToStatusId());
            twitanswer.setTwittertext(status.getText());
            twitanswer.setTwitterusername(status.getUser().getScreenName());
            try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
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
                                           .add(Restrictions.eq("twitterinreplytostatusid", inreplytostatusid))
                                           .setCacheable(true)
                                           .list();
        if (twitasks!=null && twitasks.size()>0){
            return twitasks.get(0);
        } else {
            return null;
        }
    }

}