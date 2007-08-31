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
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class PayForSurveyResponsesOncePosted implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
                    //Affect balance for blogger
                    MoveMoneyInAccountBalance.pay(user, survey.getWillingtopayperrespondent(), "Pay for taking survey: '"+survey.getTitle()+"'", true, response.getIsforcharity(), response.getCharityname(), 0, 0, response.getResponseid());
                    //Affect balance for researcher
                    MoveMoneyInAccountBalance.charge(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (survey.getWillingtopayperrespondent()+(survey.getWillingtopayperrespondent()*(SurveyMoneyStatus.DNEEROMARKUPPERCENT/100))), "User "+Jsf.getUserSession().getUser().getFirstname()+" "+Jsf.getUserSession().getUser().getLastname()+" responds to survey '"+survey.getTitle()+"'");
                    //Update paid status
                    response.setIspaid(true);
                    try{response.save();}catch(Exception ex){logger.error(ex);}
                    //@todo Notify user via email that they've been paid... thank them for their effort
                } catch (Exception ex){
                    logger.error(ex);
                }
            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}