package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CreateImpressionpaymentgroups implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CreateImpressionpaymentgroups called +++++++++++++++++++++");

        List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            logger.debug("Begin bloggerid="+blogger.getBloggerid());

            //Impressiondetails, all of them, even those that we aren't going to pay on
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.le("impressionpaymentgroupid", 0))
                                       .list();
            logger.debug(impressiondetails.size() + " Impressiondetails found.");

            //If we've found any impressions
            if (impressiondetails.size()>0){

                //Calculate amt, only for those that are marked for pay
                double amt = 0;
                for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                    Impressiondetail impressiondetail = iterator1.next();
                    if (impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                        Impression impression = Impression.get(impressiondetail.getImpressionid());
                        Survey survey = Survey.get(impression.getSurveyid());
                        amt = amt + (survey.getWillingtopaypercpm()/1000);
                    }
                }

                //Create impressionpaymentgroup
                Impressionpaymentgroup impressionpaymentgroup = new Impressionpaymentgroup();
                impressionpaymentgroup.setBloggerid(blogger.getBloggerid());
                impressionpaymentgroup.setDate(new Date());
                impressionpaymentgroup.setAmt(amt);
                try{impressionpaymentgroup.save();} catch (Exception ex){logger.error(ex);}

                //Mark all elements as tied to this impressionpaymentgroup
                for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                    Impressiondetail impressiondetail = iterator1.next();
                    impressiondetail.setImpressionpaymentgroupid(impressionpaymentgroup.getImpressionpaymentgroupid());
                    try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                }

                //If there's an amount to be paid, pay it
                if (amt>0){
                    //Update the account balance for the blogger
                    MoveMoneyInAccountBalance.pay(User.get(blogger.getUserid()), impressionpaymentgroup.getAmt(), "Pay for blog impressions", true, impressionpaymentgroup.getImpressionpaymentgroupid(), 0);
                }

            }

        }

    }

}