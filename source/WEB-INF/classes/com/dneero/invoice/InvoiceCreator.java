package com.dneero.invoice;

import com.dneero.dao.Survey;
import com.dneero.dao.Invoice;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.util.GeneralException;

import java.util.Iterator;
import java.util.Calendar;

import org.apache.log4j.Logger;


/**
 * User: Joe Reger Jr
 * Date: Jul 20, 2006
 * Time: 11:59:01 AM
 */
public class InvoiceCreator {

    public static void createInvoiceIfNecessary(Survey survey){
        Logger logger = Logger.getLogger(InvoiceCreator.class);
        logger.debug("Processing surveyid="+survey.getSurveyid()+" to see if an invoice needs to be created");

        //Figure out most recently invoiced date (or survey start date if no invoices exist yet)
        Calendar startDate = Time.getCalFromDate(survey.getStartdate());
        for (Iterator<Invoice> iterator = survey.getInvoices().iterator(); iterator.hasNext();) {
            Invoice invoice = iterator.next();
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
        logger.debug("DateDiff.dateDiff(\"day\", Calendar.getInstance(), endDateOfSurvey)="+DateDiff.dateDiff("day", Calendar.getInstance(), endDateOfSurvey));
        logger.debug("DateDiff.dateDiff(\"minute\", startDate, endDate)="+DateDiff.dateDiff("minute", startDate, endDate));
        logger.debug("DateDiff.dateDiff(\"day\", startDateOfSurvey, endDateOfSurvey)="+DateDiff.dateDiff("day", startDateOfSurvey, endDateOfSurvey));

        //Make sure it's in the past
        if (Calendar.getInstance().before(startDate)){
            logger.debug("returning because the current time is not after the startDate of the invoice");
            return;
        }

        //See whether or not to create an invoice now
        if (survey.getStatus()==Survey.STATUS_OPEN){
            //Don't create a new invoice if the survey is ending within 7 days
            if (DateDiff.dateDiff("day", Calendar.getInstance(), endDateOfSurvey)<7){
                logger.debug("returning because this invoice is within 7 days of the end of a survey");
                return;
            }
            //If it's not the first day of the month, when billing is done
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)!=1){
                logger.debug("returning because it's not the first day of the month");
                return;
            }
            //If the survey is shorter than 45 days then we'll just wait until the end of the survey
            if (DateDiff.dateDiff("day", startDateOfSurvey, endDateOfSurvey)<=45){
                logger.debug("returning because the survey is less than 45 days in total length");
                return;
            }
        } else if (survey.getStatus()==Survey.STATUS_CLOSED){
            //If the start and end are just too close, effectively equal
            if (DateDiff.dateDiff("minute", startDate, endDate)<1){
                logger.debug("returning because the startDate is too close to the endDate");
                return;
            }
        }

        //Create an invoice
        logger.debug("An invoice will be created");
        Invoice invoice = new Invoice();
        invoice.setSurveyid(survey.getSurveyid());
        invoice.setStatus(Invoice.STATUS_NOTPAID);
        invoice.setStartdate(startDate.getTime());
        invoice.setEnddate(endDate.getTime());

        try{
            invoice.save();
        } catch (GeneralException gex){
            logger.error(gex);
        }

        //Calculate the price of this invoice
        double amtbase = InvoiceCostCalculator.getAmtBase(invoice);
        double amtdneero = amtbase + (amtbase*.2);
        double amtdiscount = 0;
        double amttotal = (amtbase + amtdneero) - amtdiscount;

        invoice.setAmtbase(amtbase);
        invoice.setAmtdneero(amtdneero);
        invoice.setAmtdiscount(amtdiscount);
        invoice.setAmttotal(amttotal);

        try{
            invoice.save();
        } catch (GeneralException gex){
            logger.error(gex);
        }

        logger.debug("Invoiceid="+invoice.getInvoiceid()+" has been created for $"+amttotal);
    }


}
