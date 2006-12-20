package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.systemprops.InstanceProperties;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class PendingToOpenSurveys implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() PendingToOpenSurveys called");
            List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                   .add( Restrictions.eq("status", Survey.STATUS_WAITINGFORSTARTDATE))
                                   .list();
            for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
                Survey survey = iterator.next();
                if (survey.getStartdate().before(new Date())){
                    survey.setStatus(Survey.STATUS_OPEN);
                    try{ survey.save(); } catch (GeneralException ex){ logger.error(ex); }
                }
            }
        }
    }

}
