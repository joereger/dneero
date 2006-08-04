package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Invoice;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.invoice.InvoiceCreator;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 *
 * Generates invoices for long-running open surveys.
 *
 */
public class GenerateInvoicesForLongOpenSurveys implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() GenerateInvoicesForLongOpenSurveys called");

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                               .add( Restrictions.eq("status", Survey.STATUS_OPEN))
                               .list();

        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            logger.debug("Processing surveyid="+survey.getSurveyid()+" to see if an invoice needs to be created");

            boolean createANewSurvey = true;

            //Figure out most recently invoiced date (or survey start date if no invoices exist yet)
            Calendar startDate = Time.getCalFromDate(survey.getStartdate());
            for (Iterator<Invoice> iterator1 = survey.getInvoices().iterator(); iterator1.hasNext();) {
                Invoice invoice = iterator1.next();
                Calendar tmp = Time.getCalFromDate(invoice.getEnddate());
                if (tmp.after(startDate)){
                    startDate = (Calendar)tmp.clone();
                }
            }

            //Set the end date
            Calendar endDate = Calendar.getInstance();

            //Find the end date of the survey
            Calendar startDateOfSurvey = Time.getCalFromDate(survey.getStartdate());
            Calendar endDateOfSurvey = Time.getCalFromDate(survey.getEnddate());

            //Debug output
            logger.debug("startDate="+ Time.dateformatfordb(startDate));
            logger.debug("endDate="+ Time.dateformatfordb(endDate));
            logger.debug("startDateOfSurvey="+ Time.dateformatfordb(startDateOfSurvey));
            logger.debug("endDateOfSurvey="+ Time.dateformatfordb(endDateOfSurvey));
            logger.debug("DateDiff.dateDiff(\"day\", Calendar.getInstance(), endDateOfSurvey)="+ DateDiff.dateDiff("day", Calendar.getInstance(), endDateOfSurvey));
            logger.debug("DateDiff.dateDiff(\"minute\", startDate, endDate)="+DateDiff.dateDiff("minute", startDate, endDate));
            logger.debug("DateDiff.dateDiff(\"day\", startDateOfSurvey, endDateOfSurvey)="+DateDiff.dateDiff("day", startDateOfSurvey, endDateOfSurvey));

            //Make sure it's in the past
            if (Calendar.getInstance().before(startDate)){
                logger.debug("returning because the current time is not after the startDate of the invoice");
                createANewSurvey = false;
            }

            //See whether or not to create an invoice now
            if (survey.getStatus()==Survey.STATUS_OPEN){
                //Don't create a new invoice if the survey is ending within 7 days
                if (DateDiff.dateDiff("day", Calendar.getInstance(), endDateOfSurvey)<7){
                    logger.debug("returning because this invoice is within 7 days of the end of a survey");
                    createANewSurvey = false;
                }
                //If it's not the first day of the month, when billing is done
                if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)!=1){
                    logger.debug("returning because it's not the first day of the month");
                    createANewSurvey = false;
                }
                //If the survey is shorter than 45 days then we'll just wait until the end of the survey
                if (DateDiff.dateDiff("day", startDateOfSurvey, endDateOfSurvey)<=45){
                    logger.debug("returning because the survey is less than 45 days in total length");
                    createANewSurvey = false;
                }
            } else if (survey.getStatus()==Survey.STATUS_CLOSED){
                //If the start and end are just too close, effectively equal
                if (DateDiff.dateDiff("minute", startDate, endDate)<1){
                    logger.debug("returning because the startDate is too close to the endDate");
                    createANewSurvey = false;
                }
            }


            //Create an invoice if necessary
            if (createANewSurvey){
                InvoiceCreator.createInvoice(survey, startDate, endDate);
            }
        }

    }

}
