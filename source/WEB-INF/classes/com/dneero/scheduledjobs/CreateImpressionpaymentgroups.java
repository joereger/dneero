package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.systemprops.InstanceProperties;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CreateImpressionpaymentgroups implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
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
                    HashMap charityDonations = new HashMap();
                    for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                        Impressiondetail impressiondetail = iterator1.next();
                        if (impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                            Impression impression = Impression.get(impressiondetail.getImpressionid());
                            Survey survey = Survey.get(impression.getSurveyid());
                            //Find the response
                            Response response = null;
                            boolean isforcharity = false;
                            try{
                                for (Iterator<Response> iterator2 = blogger.getResponses().iterator(); iterator2.hasNext();){
                                    Response respTmp = iterator2.next();
                                    if (respTmp.getSurveyid()==survey.getSurveyid()){
                                        response = respTmp;
                                    }
                                }
                                if (response!=null){
                                    if (response.getIsforcharity()){
                                        isforcharity = true;
                                        if (charityDonations.containsKey(response.getCharityname())){
                                            double currentCharityAmt = Double.parseDouble(String.valueOf(charityDonations.get(response.getCharityname())));
                                            charityDonations.put(response.getCharityname(), currentCharityAmt+(survey.getWillingtopaypercpm()/1000));
                                        } else {
                                            charityDonations.put(response.getCharityname(), (survey.getWillingtopaypercpm()/1000));
                                        }
                                    }
                                }
                            } catch (Exception ex){
                                logger.error("Error in charityDonations code.", ex);
                            }
                            //Calculate the amount that this impression is worth
                            if (!isforcharity){
                                amt = amt + (survey.getWillingtopaypercpm()/1000);
                            }
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
                        MoveMoneyInAccountBalance.pay(User.get(blogger.getUserid()), impressionpaymentgroup.getAmt(), "Pay for blog impressions", true, false, "", impressionpaymentgroup.getImpressionpaymentgroupid(), 0, 0);
                    }

                    //Iterate charityDonations
                    try{
                        Iterator keyValuePairs = charityDonations.entrySet().iterator();
                        for (int i = 0; i < charityDonations.size(); i++){
                            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                            String charityname = (String)mapentry.getKey();
                            double amtCharity = Double.parseDouble(String.valueOf(mapentry.getValue()));
                            MoveMoneyInAccountBalance.pay(User.get(blogger.getUserid()), amtCharity, "Pay for blog impressions", false, true, charityname, impressionpaymentgroup.getImpressionpaymentgroupid(), 0, 0);
                        }
                    } catch (Exception ex){
                        logger.error("Error in charity payment code.", ex);
                    }
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

}
