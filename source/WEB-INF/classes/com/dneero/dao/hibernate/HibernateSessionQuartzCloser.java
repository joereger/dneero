package com.dneero.dao.hibernate;

import com.dneero.dao.Schedextime;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.hibernate.HibernateException;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2007
 * Time: 10:19:02 AM
 */
public class HibernateSessionQuartzCloser implements JobListener {

    private long millisatstart = 0;
    private long millisatend = 0;

    public String getName() {
        return this.getClass().getName();
    }

    public void jobToBeExecuted(JobExecutionContext context){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("jobToBeExecuted(): "+context.getJobDetail().getFullName());
        }
        synchronized(this){
            //Create a random delay of 0-30 secs
            int rndDelay = Num.randomInt(30000);
            if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
                logger.debug("Start waiting "+rndDelay+" millis: "+new Date().getTime()+" for: "+context.getJobDetail().getFullName());
            }
            try{wait(rndDelay);}catch(Exception ex){logger.error("",ex);}
            if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
                logger.debug("End waiting "+rndDelay+" millis: "+new Date().getTime()+" for: "+context.getJobDetail().getFullName());
            }
            //Track start time
            millisatstart = new Date().getTime();
            //Start Hibernate Session
            //HibernateUtil.startSession();
        }
    }

    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobExecutionException) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Record execution time
        try{
            millisatend = new Date().getTime();
            long millistoexecute = millisatend - millisatstart;
            recordExecution(context.getJobDetail().getFullName(), new Long(millistoexecute).intValue());
            if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
                logger.debug("jobWasExecuted(): "+context.getJobDetail().getFullName()+ " in "+millistoexecute+" millis ("+(millistoexecute/1000)+" secs)");
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
        //Close the Hibernate Session
        try{
            HibernateUtil.closeSession();
            HibernateUtilDbcache.closeSession();
        } catch (Exception ex){
            logger.debug("Error closing hibernate session at end of quartz session");
            logger.error("",ex);
        }
            //HibernateUtil.endSession();
    }

    private void recordExecution(String taskname, int millistoexecute){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (millistoexecute>0){
                Schedextime schedextime = new Schedextime();
                schedextime.setDate(new Date());
                schedextime.setMillistoexecute(Integer.parseInt(String.valueOf(millistoexecute)));
                schedextime.setServername(InstanceProperties.getInstancename());
                schedextime.setTaskname(taskname);
                schedextime.save();
                //Now delete old entries
                int daysago = 7;
                int hoursago = daysago * 24;
                int minutesago = hoursago * 60;
                Calendar xcal = Time.xMinutesAgoStart(Calendar.getInstance(), minutesago);
                HibernateUtil.getSession().createQuery("delete Schedextime s where s.date>'"+xcal.getTime()+"'").executeUpdate();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }


}
