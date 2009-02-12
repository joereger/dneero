package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;
import com.dneero.incentivetwit.Incentivetwit;
import com.dneero.incentivetwit.IncentivetwitCash;
import com.dneero.incentivetwit.IncentivetwitCoupon;

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
            logger.debug("execute() PendingToOpenTwitasks called");
            List<Twitask> twitasks = HibernateUtil.getSession().createCriteria(Twitask.class)
                                   .add( Restrictions.eq("status", Twitask.STATUS_WAITINGFORSTARTDATE))
                                   .addOrder(Order.asc("startdate"))
                                   .list();
            for (Iterator<Twitask> iterator = twitasks.iterator(); iterator.hasNext();) {
                Twitask twitask = iterator.next();
                logger.debug("twitask "+twitask.getQuestion()+" in STATUS_WAITINGFORSTARTDATE");
                logger.debug("twitask.getStartdate()="+ Time.dateformatcompactwithtime(Time.getCalFromDate(twitask.getStartdate())));
                logger.debug("new Date()="+ Time.dateformatcompactwithtime(Time.getCalFromDate(new Date())));
                if (twitask.getStartdate().before(new Date())){
                    logger.debug("opening twitask");
                    //Open it up
                    openTwitask(twitask);
                    //Only want to have one open per day
                    //break;
                } else {
                    logger.debug("not opening because twitask.getStartdate() not before new Date()");
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void openTwitask(Twitask twitask){
        Logger logger = Logger.getLogger(PendingToOpenTwitasks.class);
        try{
            User user = User.get(twitask.getUserid());
            Pl pl = Pl.get(user.getPlid());
            Incentivetwit incentivetwit = twitask.getIncentive();


            StringBuffer statusTxt = new StringBuffer();
            statusTxt.append("Q:");
            if (incentivetwit.getTwitaskincentive().getType()==IncentivetwitCash.ID){
                statusTxt.append(incentivetwit.getShortSummary());
            } else if (incentivetwit.getTwitaskincentive().getType()==IncentivetwitCoupon.ID){
                statusTxt.append("COUPON");
            }
            if (twitask.getIscharityonly()){
                statusTxt.append("->CHARITY");
            }
            statusTxt.append(": ");
            statusTxt.append(twitask.getQuestion());
            //Send update
            Twitter twitter = new Twitter(pl.getTwitterusername(),pl.getTwitterpassword());
            twitter.setSource("dNeero.com");
            Status status = twitter.update(statusTxt.toString());
            twitask.setTwitterid(status.getId());
            twitask.setStatus(Twitask.STATUS_OPEN);
            twitask.setSenttotwitterdate(new Date());
            try{ twitask.save(); twitask.refresh(); } catch (GeneralException ex){ logger.error("",ex); }
        } catch (Exception ex){ logger.error("",ex); }
    }

}