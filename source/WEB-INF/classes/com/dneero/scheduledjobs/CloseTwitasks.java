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

public class CloseTwitasks implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CloseTwitasks called");
            List<Twitask> twitasks = HibernateUtil.getSession().createCriteria(Twitask.class)
                                   .add( Restrictions.eq("status", Twitask.STATUS_OPEN))
                                   .addOrder(Order.asc("startdate"))
                                   .list();
            for (Iterator<Twitask> iterator = twitasks.iterator(); iterator.hasNext();) {
                Twitask twitask = iterator.next();
                int respondentssofar = NumFromUniqueResult.getInt("select count(*) from Twitanswer where twitaskid='"+twitask.getTwitaskid()+"' and status='"+Twitanswer.STATUS_APPROVED+"'");
                if (respondentssofar>=twitask.getNumberofrespondentsrequested()){
                    closeTwitask(twitask);
                    continue;
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void closeTwitask(Twitask twitask){
        Logger logger = Logger.getLogger(CloseTwitasks.class);
        twitask.setStatus(Twitask.STATUS_CLOSED);
        twitask.setClosedintwitterdate(new Date());
        try{ twitask.save(); } catch (GeneralException ex){ logger.error("",ex); }
        //Announce closing on Twitter
        User user = User.get(twitask.getUserid());
        Pl pl = Pl.get(user.getPlid());
        String dotdotdot = "";
        if (twitask.getQuestion().length()>84){ dotdotdot = "..."; }
        String msg = "http://dNeero.com/tq/"+twitask.getTwitaskid()+" - Results to question \""+ Str.truncateString(twitask.getQuestion(), 84)+dotdotdot+"\"";
        try{
            Twitter twitter = new Twitter(pl.getTwitterusername(),pl.getTwitterpassword());
            twitter.setSource("dNeero.com");
            Status status = twitter.update(msg);
        } catch (Exception ex){ logger.error("",ex); }

    }

}