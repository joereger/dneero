package com.dneero.invoice;

import com.dneero.dao.*;
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

    public static void createInvoice(Survey survey, Calendar startDate, Calendar endDate){
        Logger logger = Logger.getLogger(InvoiceCreator.class);

        //@todo check to see if another invoice exists overlapping the time period specified between startDate and endDate

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

        //Mark responses as part of this invoice
        for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            Calendar responsedate = Time.getCalFromDate(response.getResponsedate());
            //If the responsedate is between the invoice start and end
            if ( (responsedate.after(startDate)||responsedate.equals(startDate)) && (responsedate.before(endDate)||responsedate.equals(endDate))){
                response.setInvoiceid(invoice.getInvoiceid());
                try{ response.save(); } catch (Exception ex){logger.error(ex);}
            }
        }

        //Mark impressiondetails as part of this invoice
        int counttotal = 0;
        for (Iterator<Impression> iterator = survey.getImpressions().iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            int countperblog = 0;
            for (Iterator<Impressiondetail> iterator2 = impression.getImpressiondetails().iterator(); iterator.hasNext();) {
                Impressiondetail impressiondetail = iterator2.next();
                counttotal = counttotal + 1;
                countperblog = countperblog + 1;
                //If the impressiondate is between the invoice start and end
                Calendar impressiondate = Time.getCalFromDate(impressiondetail.getImpressiondate());
                if ( (impressiondate.after(startDate)||impressiondate.equals(startDate)) && (impressiondate.before(endDate)||impressiondate.equals(endDate))){
                    //Limit the number of impressions per blog and total
                    //@todo possibly move the determination of qualifiesforpaymentstatus to iao storage methods
                    if (counttotal< survey.getMaxdisplaystotal() && countperblog<survey.getMaxdisplaysperblog()){
                        impressiondetail.setInvoiceid(invoice.getInvoiceid());
                        impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE);
                        try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                    } else {
                        impressiondetail.setInvoiceid(invoice.getInvoiceid());
                        impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE);
                        try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                    }
                }
            }
        }
    }


}
