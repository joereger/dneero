package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.hibernate.NumFromUniqueResultImpressions;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.systemprops.InstanceProperties;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class UpdateSurveyImpressionsPaid implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() UpdateSurveyImpressionsPaid called");
            List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                   .setCacheable(true)
                                   .list();
            for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
                Survey survey = iterator.next();
                int impressionstotal = NumFromUniqueResult.getInt("select sum(impressionstotal) from Response where surveyid='"+survey.getSurveyid()+"'");
                int impressionspaid = NumFromUniqueResult.getInt("select sum(impressionspaid) from Response where surveyid='"+survey.getSurveyid()+"'");
                int impressionstobepaid = NumFromUniqueResult.getInt("select sum(impressionstobepaid) from Response where surveyid='"+survey.getSurveyid()+"'");
                //Only call save() if something's changed
                boolean shouldsave = false;
                if (survey.getImpressionstotal()!=impressionstotal){
                    shouldsave=true;
                }
                if (survey.getImpressionspaid()!=impressionspaid){
                    shouldsave=true;
                }
                if (survey.getImpressionstobepaid()!=impressionstobepaid){
                    shouldsave=true;
                }
                if (shouldsave){
                    survey.setImpressionstotal(impressionstotal);
                    survey.setImpressionspaid(impressionspaid);
                    survey.setImpressionstobepaid(impressionstobepaid);
                    try{ survey.save(); } catch (GeneralException ex){ logger.error("",ex); }
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}