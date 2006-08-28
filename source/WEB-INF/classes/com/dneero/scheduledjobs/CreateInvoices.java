package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;

import java.util.Iterator;
import java.util.Calendar;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 *
 * Generates invoices.
 *
 */
public class CreateInvoices implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CreateInvoices called");

        //@todo check to see if another invoice exists overlapping the time period specified between startDate and endDate

        //Iterate all researchers
        List<Researcher> researchers = HibernateUtil.getSession().createQuery("from Researcher").list();
        for (Iterator<Researcher> iterator = researchers.iterator(); iterator.hasNext();) {
            Researcher researcher = iterator.next();

            //@todo Determine startDate and endDate
            Calendar startDate = XXX;
            Calendar endDate = XXX;
            double amt = 0;
            if (true){
                //Iterate Surveys
                List<Survey> surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"'").list();
                for (Iterator<Survey> iterator2 = surveys.iterator(); iterator2.hasNext();) {
                    Survey survey = iterator2.next();
                    //Iterate Responses
                    //@todo pull up responses with criteria query
                    for (Iterator<Response> iterator3 = survey.getResponses().iterator(); iterator3.hasNext();) {
                        Response response = iterator3.next();
                        Calendar responsedate = Time.getCalFromDate(response.getResponsedate());
                        //If the responsedate is between the invoice start and end
                        if ( (responsedate.after(startDate)||responsedate.equals(startDate)) && (responsedate.before(endDate)||responsedate.equals(endDate))){
                            //response.setInvoiceid(invoice.getInvoiceid());
                            //try{ response.save(); } catch (Exception ex){logger.error(ex);}
                            amt = amt + survey.getWillingtopayperrespondent();
                        }
                    }
                    //Iterate Impressions
                    int counttotal = 0;
                    //@todo pull up impressions with criteria query
                    for (Iterator<Impression> iterator4 = survey.getImpressions().iterator(); iterator4.hasNext();) {
                        Impression impression = iterator4.next();
                        int countperblog = 0;
                        for (Iterator<Impressiondetail> iterator5 = impression.getImpressiondetails().iterator(); iterator.hasNext();) {
                            Impressiondetail impressiondetail = iterator5.next();
                            counttotal = counttotal + 1;
                            countperblog = countperblog + 1;
                            //If the impressiondate is between the invoice start and end
                            Calendar impressiondate = Time.getCalFromDate(impressiondetail.getImpressiondate());
                            if ( (impressiondate.after(startDate)||impressiondate.equals(startDate)) && (impressiondate.before(endDate)||impressiondate.equals(endDate))){
                                //Limit the number of impressions per blog and total
                                //@todo possibly move the determination of qualifiesforpaymentstatus to iao storage methods
                                if (counttotal< survey.getMaxdisplaystotal() && countperblog<survey.getMaxdisplaysperblog()){
                                    //impressiondetail.setInvoiceid(invoice.getInvoiceid());
                                    //impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE);
                                    //try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                                } else {
                                    //impressiondetail.setInvoiceid(invoice.getInvoiceid());
                                    //impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE);
                                    //try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                                }
                            }
                        }
                    }
                    amt = amt + (Double.parseDouble(String.valueOf(counttotal))/1000) * survey.getWillingtopaypercpm();
                }
            }

            //Create Invoice if amt>0
            if (amt>0){
                //Create the invoice
                logger.debug("An invoice will be created");
                Invoice invoice = new Invoice();
                invoice.setStatus(Invoice.STATUS_NOTPAID);
                invoice.setStartdate(startDate.getTime());
                invoice.setEnddate(endDate.getTime());
                double amtbase = amt;
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

                //Iterate Surveys
                List<Survey> surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"'").list();
                for (Iterator<Survey> iterator2 = surveys.iterator(); iterator2.hasNext();) {
                    Survey survey = iterator2.next();
                    //Iterate Responses
                    //@todo pull up responses with criteria query
                    for (Iterator<Response> iterator3 = survey.getResponses().iterator(); iterator3.hasNext();) {
                        Response response = iterator3.next();
                        Calendar responsedate = Time.getCalFromDate(response.getResponsedate());
                        //If the responsedate is between the invoice start and end
                        if ( (responsedate.after(startDate)||responsedate.equals(startDate)) && (responsedate.before(endDate)||responsedate.equals(endDate))){
                            response.setInvoiceid(invoice.getInvoiceid());
                            try{ response.save(); } catch (Exception ex){logger.error(ex);}
                        }
                    }
                    //Iterate Impressions
                    int counttotal = 0;
                    //@todo pull up impressions with criteria query
                    for (Iterator<Impression> iterator4 = survey.getImpressions().iterator(); iterator4.hasNext();) {
                        Impression impression = iterator4.next();
                        int countperblog = 0;
                        for (Iterator<Impressiondetail> iterator5 = impression.getImpressiondetails().iterator(); iterator.hasNext();) {
                            Impressiondetail impressiondetail = iterator5.next();
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

        }


    }

}