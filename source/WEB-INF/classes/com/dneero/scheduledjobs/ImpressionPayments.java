package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ImpressionPayments implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionPayments called +++++++++++++++++++++");



            //20 days added so that we don't miss any impressions
            Date datethreshold = Time.xDaysAgoStart(Calendar.getInstance(), SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS+20).getTime();
            logger.debug("datethreshold="+Time.dateformatfordb(Time.getCalFromDate(datethreshold)));
            List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                               .add(Restrictions.gt("enddate", datethreshold))
                                               .setCacheable(true)
                                               .list();
            logger.debug("surveys.size()="+surveys);
            for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
                Survey survey = iterator.next();
                logger.debug("Begin surveyid="+survey.getSurveyid()+" - "+survey.getTitle());

                //Find impressions for this survey
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                           .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                           .add( Restrictions.gt("impressionstobepaid", 0))
                                           .list();
                logger.debug(impressions.size() + " impressions found.");

                //If we've found any impressions
                if (impressions.size()>0){
                    //Collect a map of UserPayUnits
                    HashMap<Integer, ImpressionPaymentsUserPayUnit> userPayUnits = new HashMap<Integer, ImpressionPaymentsUserPayUnit>();
                    //Increment a total researcher charge
                    double amtResearcherOwes = 0;
                    for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                        Impression impression = iterator1.next();
                        //Get the response that this impression relates to
                        Response response = Response.get(impression.getResponseid());
                        if (response!=null && response.getResponseid()>0){
                            //Figure out who's gettin' paid for this impression
                            Blogger blogger = Blogger.get(response.getBloggerid());
                            if (blogger!=null && blogger.getBloggerid()>0){
                                User user = User.get(blogger.getUserid());
                                if (user!=null && user.getUserid()>0){
                                    //Increment researcher amount
                                    amtResearcherOwes = amtResearcherOwes + (survey.getWillingtopaypercpm()/1000);
                                    //Get a UserPayUnit to work with
                                    ImpressionPaymentsUserPayUnit ipupu = new ImpressionPaymentsUserPayUnit();
                                    if (userPayUnits.containsKey(user.getUserid())){
                                        ipupu = (ImpressionPaymentsUserPayUnit)userPayUnits.get(user.getUserid());
                                    }
                                    //Set the amount, but only if it's not a charity
                                    if (!response.getIsforcharity()){
                                        ipupu.setAmt(ipupu.getAmt() + (survey.getWillingtopaypercpm()/1000));
                                    }
                                    //If it's charity, add it as a charity
                                    if (response.getIsforcharity()){
                                        ipupu.addCharityDonation(response.getCharityname(), (survey.getWillingtopaypercpm()/1000));
                                    }
                                    //Put back into main hashmap of UserPayUnits
                                    userPayUnits.put(user.getUserid(), ipupu);
                                }
                            }
                        }
                    }

                    //Charge the researcher, applying the dNeero markup
                    if (amtResearcherOwes>0){
                        double amttocharge = amtResearcherOwes + (amtResearcherOwes*(SurveyMoneyStatus.DNEEROMARKUPPERCENT/100));
                        Researcher researcher = Researcher.get(survey.getResearcherid());
                        User user = User.get(researcher.getUserid());
                        MoveMoneyInAccountBalance.charge(user, amttocharge, "Charge for blog impressions on survey '"+survey.getTitle()+"'");
                    }

                    //Pay bloggers if we have any UserPayUnits to consider
                    if (userPayUnits!=null && userPayUnits.size()>0){

                        Iterator keyValuePairs = userPayUnits.entrySet().iterator();
                        for (int i = 0; i < userPayUnits.size(); i++){
                            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                            Integer userid = (Integer)mapentry.getKey();
                            ImpressionPaymentsUserPayUnit ipupu = (ImpressionPaymentsUserPayUnit)mapentry.getValue();
                            User user = User.get(userid);
                            if (user!=null && user.getUserid()>0){
                                //If there's an amount to pay
                                if (ipupu.getAmt()>0){
                                    MoveMoneyInAccountBalance.pay(user, ipupu.getAmt(), "Pay for impressions", true, false, "", 0);
                                }
                                //If there are charity donations to pay
                                if (ipupu.getCharityDonations()!=null && ipupu.getCharityDonations().size()>0){
                                    try{
                                        Iterator keyValuePairs2 = ipupu.getCharityDonations().entrySet().iterator();
                                        for (int i2 = 0; i2 < ipupu.getCharityDonations().size(); i2++){
                                            Map.Entry mapentry2 = (Map.Entry) keyValuePairs2.next();
                                            String charityname = (String)mapentry2.getKey();
                                            double amtCharity = Double.parseDouble(String.valueOf(mapentry2.getValue()));
                                            MoveMoneyInAccountBalance.pay(user, amtCharity, "Pay charity for blog impressions", false, true, charityname, 0);
                                        }
                                    } catch (Exception ex){
                                        logger.error("Error in charity payment code.", ex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

}
