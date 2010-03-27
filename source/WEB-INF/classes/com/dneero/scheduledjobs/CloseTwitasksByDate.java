package com.dneero.scheduledjobs;

import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class CloseTwitasksByDate implements Job {

    public static int DAYSTOKEEPOPEN = 3;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CloseTwitasksByDate called");
            List<Twitask> twitasks = HibernateUtil.getSession().createCriteria(Twitask.class)
                                   .add( Restrictions.eq("status", Twitask.STATUS_OPEN))
                                   .addOrder(Order.asc("startdate"))
                                   .list();
            for (Iterator<Twitask> iterator = twitasks.iterator(); iterator.hasNext();) {
                Twitask twitask = iterator.next();
                int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(twitask.getStartdate()));
                if (daysold>DAYSTOKEEPOPEN){
                    CloseTwitasks.closeTwitask(twitask);
                    continue;
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }



}