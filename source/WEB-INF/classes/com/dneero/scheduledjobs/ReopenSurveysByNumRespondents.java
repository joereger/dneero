package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.helpers.SlotsRemainingInConvo;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ReopenSurveysByNumRespondents implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ReopenSurveysByNumRespondents called");
            List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                   .add( Restrictions.eq("status", Survey.STATUS_CLOSED))
                                   .setCacheable(false)
                                   .list();
            for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
                Survey survey = iterator.next();
                logger.debug("survey.getSurveyid()="+survey.getSurveyid()+" title="+survey.getTitle());
                logger.debug("survey.getEnddate()="+Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate())));
                logger.debug("new Date()="+Time.dateformatcompactwithtime(Time.getCalFromDate(new Date())));
                //If the enddate of the survey is after today (now)
                if (survey.getEnddate().after(new Date())){
                    //If there are still slots open
                    if (SlotsRemainingInConvo.getSlotsRemaining(survey)>0){
                        survey.setStatus(Survey.STATUS_OPEN);
                        try{ survey.save(); } catch (GeneralException ex){ logger.error("",ex); }
                        logger.debug("reopening survey.getSurveyid()="+survey.getSurveyid()+" title="+survey.getTitle());
                    }
                } else {
                    logger.debug("keeping survey closed survey.getSurveyid()="+survey.getSurveyid()+" title="+survey.getTitle());
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}