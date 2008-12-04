package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Userpersistentlogin;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.session.PersistentLogin;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.iptrack.CleanOldEntries;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class DeleteOldIptrack implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() DeleteOldIptrack called");

            //Clean the default number of days
            CleanOldEntries.clean();


        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }


    }

}