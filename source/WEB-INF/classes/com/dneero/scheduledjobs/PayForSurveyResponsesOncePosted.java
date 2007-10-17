package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.ErrorDissect;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.email.EmailTemplateProcessor;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.io.PrintWriter;

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
                                   .add( Restrictions.eq("poststatus", Response.POSTATUS_POSTED))
                                   .add( Restrictions.eq("ispaid", false))
                                   .setCacheable(false)
                                   .list();
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                logger.debug("response.getResponseid()="+response.getResponseid());
                try{
                    Survey survey = Survey.get(response.getSurveyid());
                    User user = User.get(Blogger.get(response.getBloggerid()).getUserid());
                    try{
                        //Affect balance for blogger
                        MoveMoneyInAccountBalance.pay(user, survey.getWillingtopayperrespondent(), "Pay for taking survey: '"+survey.getTitle()+"'", true, response.getIsforcharity(), response.getCharityname(), response.getResponseid());
                        //Affect balance for researcher
                        MoveMoneyInAccountBalance.charge(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (survey.getWillingtopayperrespondent()+(survey.getWillingtopayperrespondent()*(SurveyMoneyStatus.DNEEROMARKUPPERCENT/100))), "User "+user.getFirstname()+" "+user.getLastname()+" responds to survey '"+survey.getTitle()+"'");
                    } catch (Exception ex){
                        logger.error(ex);
                        ex.printStackTrace();
                        //Notify sales group
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "BIG ERROR IN PayForSurveyResponsesOncePosted:  "+ex.getMessage());
                        xmpp.send();
                        //Send error via email
                        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Error paying "+user.getFirstname()+" "+user.getLastname()+" userid="+user.getUserid(), ErrorDissect.dissect(ex));
                    } finally {
                        //Update paid status
                        response.setIspaid(true);
                        try{response.save();}catch(Exception ex){logger.error(ex);}
                        //@todo Notify user via email that they've been paid... thank them for their effort
                        if (user.getEmail()!=null && !user.getEmail().equals("")){
                                
                        }
                        //Update responsehtml
                        UpdateResponsePoststatus.processSingleResponse(response);
                    }
                } catch (Exception ex){
                    logger.error(ex);
                    ex.printStackTrace();
                }
            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
