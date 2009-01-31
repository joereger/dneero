package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

import twitter4j.Twitter;
import twitter4j.Status;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class PendingToOpenTwitasks implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() PendingToOpenSurveys called");
            List<Twitask> twitasks = HibernateUtil.getSession().createCriteria(Twitask.class)
                                   .add( Restrictions.eq("status", Twitask.STATUS_WAITINGFORSTARTDATE))
                                   .addOrder(Order.asc("startdate"))
                                   .list();
            for (Iterator<Twitask> iterator = twitasks.iterator(); iterator.hasNext();) {
                Twitask twitask = iterator.next();
                if (twitask.getStartdate().before(new Date())){
                    //Open it up
                    openTwitask(twitask);
                    //Only want to have one open per day
                    //break;
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void openTwitask(Twitask twitask){
        Logger logger = Logger.getLogger(PendingToOpenTwitasks.class);
        try{
            Twitter twitter = new Twitter("dneero","physics");
            Status status = twitter.update(twitask.getQuestion());
            twitask.setTwitterid(status.getId());
            twitask.setStatus(Twitask.STATUS_OPEN);
            twitask.setSenttotwitterdate(new Date());
            try{ twitask.save(); } catch (GeneralException ex){ logger.error("",ex); }
        } catch (Exception ex){ logger.error("",ex); }
    }

}