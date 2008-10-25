package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Time;
import com.dneero.util.Str;

import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

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
                List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                           .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                           .add( Restrictions.gt("impressionstobepaid", 0))
                                           .list();
                logger.debug(impressions.size() + " impressions found.");

                //If we've found any impressions
                if (impressions.size()>0){
                    //Collect a map of UserPayUnits
                    HashMap<Integer, ImpressionPaymentsUserPayUnit> userPayUnits = new HashMap<Integer, ImpressionPaymentsUserPayUnit>();
                    //Increment a total researcher charge
                    double amtSpentOnBehalfOfResearcher = 0;
                    double amtToChargeResearcher = 0;
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
                                    double amtSpentOnThisUserForThisSurvey = survey.getWillingtopaypercpm()/1000;
                                    amtSpentOnBehalfOfResearcher = amtSpentOnBehalfOfResearcher + amtSpentOnThisUserForThisSurvey;
                                    amtToChargeResearcher = amtToChargeResearcher + SurveyMoneyStatus.calculateAmtToChargeResearcher(amtSpentOnThisUserForThisSurvey, survey);
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
                                    //Move paid impressions over, resetting the paid clock
                                    impression.setImpressionspaid(impression.getImpressionspaid()+impression.getImpressionstobepaid());
                                    impression.setImpressionstobepaid(0);
                                    try{impression.save();}catch(Exception ex){logger.error("",ex);}
                                }
                            }
                        }
                    }

                    //Charge the researcher
                    if (amtToChargeResearcher>0){
                        Researcher researcher = Researcher.get(survey.getResearcherid());
                        User user = User.get(researcher.getUserid());
                        MoveMoneyInAccountBalance.charge(user, amtToChargeResearcher, "Charge for impressions on conversation '"+survey.getTitle()+"'", true, false, false, false);
                    }

                    //Affect balance for reseller
                    if (amtSpentOnBehalfOfResearcher>0){
                        //If there's a resellercode entered for this survey
                        if (survey.getResellercode()!=null && !survey.getResellercode().equals("")){
                            //Find the user with this resellercode
                            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                                               .add(Restrictions.eq("resellercode", survey.getResellercode()))
                                                               .setCacheable(true)
                                                               .setMaxResults(1)
                                                               .list();
                            if (userResellers!=null && userResellers.size()>0){
                                User userReseller = userResellers.get(0);
                                if (userReseller!=null){
                                    //Pay them the correct amount... remember, their pay is based on the amount paid to the bloggers
                                    double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(amtSpentOnBehalfOfResearcher, userReseller);
                                    if (amtToPayReseller>0){
                                        MoveMoneyInAccountBalance.pay(userReseller, amtToPayReseller, "Reseller pay for impressions on '"+Str.truncateString(survey.getTitle(), 20)+"'", false, false, "", 0, false, false, false, true);
                                    }
                                }
                            }
                        }      
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
                                    String amtWithDec = "";
                                    try{
                                        NumberFormat formatter = DecimalFormat.getInstance();
                                        formatter.setMinimumFractionDigits(4);
                                        formatter.setMaximumFractionDigits(4);
                                        amtWithDec = " ($"+formatter.format(ipupu.getAmt())+")";
                                    } catch (Exception ex){
                                        logger.error("",ex);
                                    }
                                    MoveMoneyInAccountBalance.pay(user, ipupu.getAmt(), "Pay for impressions on '"+ Str.truncateString(survey.getTitle(), 20) +"': "+ amtWithDec, true, false, "", 0, false, true, false, false);
                                }
                                //If there are charity donations to pay
                                if (ipupu.getCharityDonations()!=null && ipupu.getCharityDonations().size()>0){
                                    try{
                                        Iterator keyValuePairs2 = ipupu.getCharityDonations().entrySet().iterator();
                                        for (int i2 = 0; i2 < ipupu.getCharityDonations().size(); i2++){
                                            Map.Entry mapentry2 = (Map.Entry) keyValuePairs2.next();
                                            String charityname = (String)mapentry2.getKey();
                                            double amtCharity = Double.parseDouble(String.valueOf(mapentry2.getValue()));
                                            MoveMoneyInAccountBalance.pay(user, amtCharity, "Pay charity for impressions on '"+Str.truncateString(survey.getTitle(), 20)+"'", false, true, charityname, 0, false, true, false, false);
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
