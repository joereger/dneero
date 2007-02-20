package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Userpersistentlogin;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.session.PersistentLogin;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SystemStats implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private static int totalbloggers=0;
    private static int totalblogs=0;
    private static int totalresearchers=0;
    private static int totalimpressions=0;
    private static int impressions30days=0;
    private static double dollarsavailabletobloggers=0;



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SystemStats called");

            totalbloggers = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Blogger").uniqueResult();
            totalblogs = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Blog").uniqueResult();
            totalresearchers = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Researcher").uniqueResult();
            totalimpressions = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Impressiondetail").uniqueResult();

            Calendar startDate = Time.xDaysAgoStart(Calendar.getInstance(), 30);
            impressions30days = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Impressiondetail where impressiondate>='"+Time.dateformatfordb(startDate)+"' and impressiondate<='"+Time.dateformatfordb(Calendar.getInstance())+"'").uniqueResult();

            dollarsavailabletobloggers = 0;
            List opensurveys = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_OPEN +"'").list();
            if (opensurveys!=null){
                for (Iterator iterator = opensurveys.iterator(); iterator.hasNext();) {
                    Survey survey = (Survey) iterator.next();
                    dollarsavailabletobloggers = dollarsavailabletobloggers + (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested()) + ((survey.getWillingtopaypercpm() * survey.getMaxdisplaystotal())/1000);
                }
            }


        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }


    }


    public static int getTotalbloggers() {
        return totalbloggers;
    }

    public static void setTotalbloggers(int totalbloggers) {
        SystemStats.totalbloggers = totalbloggers;
    }

    public static int getTotalblogs() {
        return totalblogs;
    }

    public static void setTotalblogs(int totalblogs) {
        SystemStats.totalblogs = totalblogs;
    }

    public static int getTotalresearchers() {
        return totalresearchers;
    }

    public static void setTotalresearchers(int totalresearchers) {
        SystemStats.totalresearchers = totalresearchers;
    }

    public static int getTotalimpressions() {
        return totalimpressions;
    }

    public static void setTotalimpressions(int totalimpressions) {
        SystemStats.totalimpressions = totalimpressions;
    }


    public static int getImpressions30days() {
        return impressions30days;
    }

    public static void setImpressions30days(int impressions30days) {
        SystemStats.impressions30days = impressions30days;
    }

    public static double getDollarsavailabletobloggers() {
        return dollarsavailabletobloggers;
    }

    public static void setDollarsavailabletobloggers(double dollarsavailabletobloggers) {
        SystemStats.dollarsavailabletobloggers = dollarsavailabletobloggers;
    }
}
