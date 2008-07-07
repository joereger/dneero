package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.ErrorDissect;
import com.dneero.util.Str;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.email.EmailTemplateProcessor;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class PayForSurveyResponsesOncePosted implements Job {

 

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CloseSurveysByDate called");
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                   .add(Restrictions.eq("poststatus", Response.POSTATUS_POSTED))
                                   .add(Restrictions.ne("issysadminrejected", true))
                                   .add(Restrictions.eq("ispaid", false))
                                   .setCacheable(false)
                                   .list();
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                logger.debug("response.getResponseid()="+response.getResponseid());

                try{
                    Survey survey = Survey.get(response.getSurveyid());
                    User user = User.get(Blogger.get(response.getBloggerid()).getUserid());
                    try{
                        //One extra check to make sure this isn't rejected
                        if (!response.getIssysadminrejected()){
                            //Affect balance for blogger
                            MoveMoneyInAccountBalance.pay(user, survey.getWillingtopayperrespondent(), "Pay for responding to: '"+survey.getTitle()+"'", true, response.getIsforcharity(), response.getCharityname(), response.getResponseid(), false, true, false, false);
                            //Affect balance for researcher
                            MoveMoneyInAccountBalance.charge(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (SurveyMoneyStatus.calculateAmtToChargeResearcher(survey.getWillingtopayperrespondent(), survey)), "User "+user.getFirstname()+" "+user.getLastname()+" responds to '"+survey.getTitle()+"'", true, false, false, false);
                            //Affect balance for reseller
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
                                        double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(survey.getWillingtopayperrespondent(), userReseller);
                                        if (amtToPayReseller>0){
                                            MoveMoneyInAccountBalance.pay(userReseller, amtToPayReseller, "Reseller pay for response to '"+Str.truncateString(survey.getTitle(), 20)+"'", false, false, "", 0, false, false, false, true);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex){
                        logger.error("",ex);
                        ex.printStackTrace();
                        //Notify sales group
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "BIG ERROR IN PayForSurveyResponsesOncePosted:  "+ex.getMessage());
                        xmpp.send();
                        //Send error via email
                        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Error paying "+user.getFirstname()+" "+user.getLastname()+" userid="+user.getUserid(), ErrorDissect.dissect(ex));
                    } finally {
                        //Update paid status
                        response.setIspaid(true);
                        try{response.save();}catch(Exception ex){logger.error("",ex);}
                        //@todo Notify user via email that they've been paid... thank them for their effort
                        if (user.getEmail()!=null && !user.getEmail().equals("")){
                                
                        }
                        //Update responsehtml
                        UpdateResponsePoststatus.processSingleResponse(response);
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                    ex.printStackTrace();
                }
            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
