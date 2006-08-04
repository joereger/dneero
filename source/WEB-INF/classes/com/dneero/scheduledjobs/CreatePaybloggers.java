package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.invoice.InvoiceCreator;
import com.dneero.invoice.InvoiceCostCalculator;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CreatePaybloggers implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CloseSurveysByDate called");

        List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();

            //Responses
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.gt("invoiceid", 0))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .list();

            //Impressiondetails
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.gt("invoiceid", 0))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .add( Restrictions.eq("qualifiesforpaymentstatus", Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE))
                                       .list();

            //Revshares
            List<Revshare> revshares = HibernateUtil.getSession().createCriteria(Revshare.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .list();

            //Calculate amt
            double amt = 0;

            for (Iterator<Response> iterator1 = responses.iterator(); iterator1.hasNext();) {
                Response response = iterator1.next();
                Invoice invoice = Invoice.get(response.getInvoiceid());
                if (invoice.getStatus()==Invoice.STATUS_PAID){
                    Survey survey = Survey.get(response.getSurveyid());
                    amt = amt + survey.getWillingtopayperrespondent();
                }
            }
            for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                Impressiondetail impressiondetail = iterator1.next();
                Invoice invoice = Invoice.get(impressiondetail.getInvoiceid());
                if (invoice.getStatus()==Invoice.STATUS_PAID){
                    Impression impression = Impression.get(impressiondetail.getImpressionid());
                    Survey survey = Survey.get(impression.getSurveyid());
                    amt = amt + (survey.getWillingtopaypercpm()/1000);
                }
            }
            for (Iterator<Revshare> iterator1 = revshares.iterator(); iterator1.hasNext();) {
                Revshare revshare = iterator1.next();
                amt = amt + revshare.getAmt();
            }

            //Create payblogger
            Payblogger payblogger = new Payblogger();
            payblogger.setBloggerid(blogger.getBloggerid());
            payblogger.setDate(new Date());
            payblogger.setStatus(Payblogger.STATUS_OWED);
            payblogger.setAmt(amt);
            try{
                payblogger.save();
            } catch (Exception ex){
                logger.error(ex);
            }

            //@todo where are revshares created?  how do i make sure they don't iterate up the chain... i.e. revshare shouldn't be paid on revshare payouts

            //Mark all elements as tied to this payblogger
            for (Iterator<Response> iterator1 = responses.iterator(); iterator1.hasNext();) {
                Response response = iterator1.next();
                Invoice invoice = Invoice.get(response.getInvoiceid());
                if (invoice.getStatus()==Invoice.STATUS_PAID){
                    response.setPaybloggerid(payblogger.getPaybloggerid());
                    try{ response.save(); } catch (Exception ex){logger.error(ex);}
                }
            }
            for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                Impressiondetail impressiondetail = iterator1.next();
                Invoice invoice = Invoice.get(impressiondetail.getInvoiceid());
                if (invoice.getStatus()==Invoice.STATUS_PAID){
                    impressiondetail.setPaybloggerid(payblogger.getPaybloggerid());
                    try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                }
            }
            for (Iterator<Revshare> iterator1 = revshares.iterator(); iterator1.hasNext();) {
                Revshare revshare = iterator1.next();
                revshare.setPaybloggerid(payblogger.getPaybloggerid());
                try{ revshare.save(); } catch (Exception ex){logger.error(ex);}
            }


        }






    }

}
