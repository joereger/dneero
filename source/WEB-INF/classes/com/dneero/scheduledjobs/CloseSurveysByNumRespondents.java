package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.invoice.InvoiceCreator;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CloseSurveysByNumRespondents implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CloseSurveysByNumRespondents called");

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                               .add( Restrictions.eq("status", Survey.STATUS_OPEN))
                               .list();

        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();

            if (survey.getResponses().size()>=survey.getNumberofrespondentsrequested()){
                survey.setStatus(Survey.STATUS_CLOSED);
                try{
                    survey.save();
                } catch (GeneralException ex){
                    logger.error(ex);
                }

                //Create an invoice if necessary
                InvoiceCreator.createInvoiceIfNecessary(survey);
            }

        }

    }

}