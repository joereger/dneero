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
        logger.debug("execute() CreateImpressionpaymentgroups called");

        List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            logger.debug("Begin bloggerid="+blogger.getBloggerid());

            //@todo add one layer of iteration... by survey

            //Impressiondetails
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                       .add( Restrictions.le("impressionpaymentgroupid", 0))
                                       .list();
            logger.debug(impressiondetails.size() + " Impressiondetails found.");


            //Calculate amt
            double amt = 0;

            for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                Impressiondetail impressiondetail = iterator1.next();
                Impression impression = Impression.get(impressiondetail.getImpressionid());
                Survey survey = Survey.get(impression.getSurveyid());
                amt = amt + (survey.getWillingtopaypercpm()/1000);
            }

            if (amt>0){
                //Create impressionpaymentgroup
                Impressionpaymentgroup impressionpaymentgroup = new Impressionpaymentgroup();
                impressionpaymentgroup.setBloggerid(blogger.getBloggerid());
                impressionpaymentgroup.setDate(new Date());
                impressionpaymentgroup.setAmt(amt);
                try{
                    impressionpaymentgroup.save();
                } catch (Exception ex){
                    logger.error(ex);
                }

                //Update the account balance for the blogger
                MoveMoneyInAccountBalance.pay(User.get(blogger.getUserid()), impressionpaymentgroup.getAmt(), "Pay for blog impressions", impressionpaymentgroup.getImpressionpaymentgroupid(), 0);

                //Create revshare
                //Start with the user who's getting paid
//                User user = User.get(blogger.getUserid());
//                double amtRevsharebasedon = amt - revshareAmt;
//                for(int levelsup=1; levelsup<=5; levelsup++){
//                    if (user.getReferredbyuserid()>0){
//                        //Switch one level up
//                        user = User.get(user.getReferredbyuserid());
//                        if (user.getBlogger()!=null && user.getBlogger().getBloggerid()>0){
//                            //Only pay if they're qualified for the revshare program
//                            if (user.getIsqualifiedforrevshare()){
//                                //Calculate the revshare
//                                double amttoshare = RevshareLevelPercentageCalculator.getAmountToShare(amtRevsharebasedon, levelsup);
//                                //Store the revshare in the database
//                                Revshare revshare = new Revshare();
//                                revshare.setSourcebloggerid(blogger.getBloggerid());
//                                revshare.setTargetbloggerid(user.getBlogger().getBloggerid());
//                                revshare.setAmt(amttoshare);
//                                revshare.setDate(new Date());
//                                revshare.setImpressionpaymentgroupid(impressionpaymentgroup.getImpressionpaymentgroupid());
//                                try{
//                                    revshare.save();
//                                } catch (Exception ex){
//                                    logger.error(ex);
//                                }
//                            }
//                        }
//                    }
//                }

                //Mark all elements as tied to this impressionpaymentgroup
                for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                    Impressiondetail impressiondetail = iterator1.next();
                    impressiondetail.setImpressionpaymentgroupid(impressionpaymentgroup.getImpressionpaymentgroupid());
                    try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                }


            }

        }

    }

}
