package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.invoice.RevshareLevelPercentageCalculator;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CreatePaybloggers implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CreatePaybloggers called");

        List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            logger.debug("Begin bloggerid="+blogger.getBloggerid());

            //Responses
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.gt("invoiceid", 0))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .list();
            logger.debug(responses.size() + " Responses found.");


            //Impressiondetails
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.gt("invoiceid", 0))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .add( Restrictions.eq("qualifiesforpaymentstatus", Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE))
                                       .list();
            logger.debug(impressiondetails.size() + " Impressiondetails found.");

            //Revshares
            List<Revshare> revshares = HibernateUtil.getSession().createCriteria(Revshare.class)
                                       .add( Restrictions.eq("targetbloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.le("paybloggerid", 0))
                                       .list();
            logger.debug(revshares.size() + " Revshares found.");

            //Calculate amt
            double amt = 0;
            double revshareAmt = 0;

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
                revshareAmt = revshareAmt + revshare.getAmt();
                amt = amt + revshare.getAmt();
            }

            if (amt>0){
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

                //Create revshare
                //Start with the user who's getting paid
                User user = User.get(blogger.getUserid());
                double amtRevsharebasedon = amt - revshareAmt;
                for(int levelsup=1; levelsup<=5; levelsup++){
                    if (user.getReferredbyuserid()>0){
                        //Switch one level up
                        user = User.get(user.getReferredbyuserid());
                        if (user.getBlogger()!=null && user.getBlogger().getBloggerid()>0){
                            //Only pay if they're qualified for the revshare program
                            if (user.getIsqualifiedforrevshare()){
                                //Calculate the revshare
                                double amttoshare = RevshareLevelPercentageCalculator.getAmountToShare(amtRevsharebasedon, levelsup);
                                //Store the revshare in the database
                                Revshare revshare = new Revshare();
                                revshare.setSourcebloggerid(blogger.getBloggerid());
                                revshare.setTargetbloggerid(user.getBlogger().getBloggerid());
                                revshare.setAmt(amttoshare);
                                revshare.setDate(new Date());
                                revshare.setPaybloggerid(payblogger.getPaybloggerid());
                                try{
                                    revshare.save();
                                } catch (Exception ex){
                                    logger.error(ex);
                                }
                            }
                        }
                    }
                }

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

}
