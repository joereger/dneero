package com.dneero.invoice;

import com.dneero.dao.*;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 20, 2006
 * Time: 12:41:39 PM
 */
public class InvoiceCostCalculator {

    public static double getAmtBase(Invoice invoice){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        double amtbase = 0;
        int responsesInInvoicePeriod = getResponsesInInvoicePeriod(invoice).size();
        logger.debug("responsesInInvoicePeriod="+responsesInInvoicePeriod);
        int impressionsInInvoicePeriod = getImpressionDetailsInInvoicePeriod(invoice).size();
        logger.debug("impressionsInInvoicePeriod="+impressionsInInvoicePeriod);
        Survey survey = Survey.get(invoice.getSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            amtbase = (responsesInInvoicePeriod*survey.getWillingtopayperrespondent()) + ((impressionsInInvoicePeriod/1000)*survey.getWillingtopaypercpm());
            logger.debug("amtbase="+amtbase);
        }
        return amtbase;
    }

    public static ArrayList<Response> getResponsesInInvoicePeriod(Invoice invoice){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        ArrayList<Response> responses = new ArrayList<Response>();
        Calendar invoiceStartDate = Time.getCalFromDate(invoice.getStartdate());
        Calendar invoiceEndDate = Time.getCalFromDate(invoice.getEnddate());
        Survey survey = Survey.get(invoice.getSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Calendar responsedate = Time.getCalFromDate(response.getResponsedate());
                //If the responsedate is between the invoice start and end
                if ( (responsedate.after(invoiceStartDate)||responsedate.equals(invoiceStartDate)) && (responsedate.before(invoiceEndDate)||responsedate.equals(invoiceEndDate))){
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    public static ArrayList<Impressiondetail> getImpressionDetailsInInvoicePeriod(Invoice invoice){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        ArrayList<Impressiondetail> impressiondetails = new ArrayList<Impressiondetail>();
        Calendar invoiceStartDate = Time.getCalFromDate(invoice.getStartdate());
        Calendar invoiceEndDate = Time.getCalFromDate(invoice.getEnddate());
        Survey survey = Survey.get(invoice.getSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            for (Iterator<Impression> iterator = survey.getImpressions().iterator(); iterator.hasNext();) {
                Impression impression = iterator.next();
                for (Iterator<Impressiondetail> iterator2 = impression.getImpressiondetails().iterator(); iterator.hasNext();) {
                    Impressiondetail impressiondetail = iterator2.next();
                    Calendar impressiondate = Time.getCalFromDate(impressiondetail.getImpressiondate());
                    //If the impressiondate is between the invoice start and end
                    if ( (impressiondate.after(invoiceStartDate)||impressiondate.equals(invoiceStartDate)) && (impressiondate.before(invoiceEndDate)||impressiondate.equals(invoiceEndDate))){
                        impressiondetails.add(impressiondetail);
                    }
                }
            }
        }
        return impressiondetails;
    }


}
