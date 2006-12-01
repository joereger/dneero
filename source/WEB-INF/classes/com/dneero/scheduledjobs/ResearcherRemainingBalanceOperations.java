package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.MoveMoneyInRealWorld;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.systemprops.InstanceProperties;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ResearcherRemainingBalanceOperations implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static double MINAVAILABLEBALANCEBEFORECLOSINGSURVEYS = 5;
    public static double MINPERCENTOFTOTALVALUEAVAILASBALANCE = 10;
    public static double INCREMENTALPERCENTTOCHARGE = 20;


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ResearcherRemainingBalanceOperations called");
            List researchers = HibernateUtil.getSession().createQuery("from Researcher").list();
            for (Iterator iterator = researchers.iterator(); iterator.hasNext();) {
                Researcher researcher = (Researcher) iterator.next();
                processResearcher(researcher);
            }
        }

    }

    public static void processResearcher(Researcher researcher){
        Logger logger = Logger.getLogger(ResearcherRemainingBalanceOperations.class);
        if (researcher!=null){
            User user = User.get(researcher.getUserid());
            //Collect data on this researcher
            List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"' and status<>'"+Survey.STATUS_DRAFT+"'").list();
            double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);
            double totalremainingpossiblespendforallsurveys = 0;
            double totalmaxpossiblespendforallsurveys = 0;
            for (Iterator iterator1 = surveys.iterator(); iterator1.hasNext();) {
                Survey survey = (Survey) iterator1.next();
                SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                totalremainingpossiblespendforallsurveys = totalremainingpossiblespendforallsurveys + sms.getRemainingPossibleSpend();
                totalmaxpossiblespendforallsurveys = totalmaxpossiblespendforallsurveys + sms.getMaxPossibleSpend();
            }

            //Now operate on surveys
            if (currentbalance < ((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)){
                //The current balance is less than 10% of the total value of all surveys so I need to know how much to charge them
                double amttocharge = (INCREMENTALPERCENTTOCHARGE/100) * totalmaxpossiblespendforallsurveys;
                if (amttocharge > totalremainingpossiblespendforallsurveys){
                    //There isn't that much possible spend left in the surveys so charge the remaining total
                    amttocharge = totalremainingpossiblespendforallsurveys;
                }
                //Account for any negative balance
                if (currentbalance<0){
                    amttocharge = amttocharge + ((-1)*currentbalance);
                }
                //Move the money
                logger.debug("amttocharge:"+amttocharge+" to userid="+user.getUserid());
                if (amttocharge>0){
                    MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, (-1)*amttocharge);
                    mmirw.move();
                    //@todo email researcher telling them that their card has been charged by incremental
                }
                //See whether I should shut down any surveys
                if (currentbalance < ((MINAVAILABLEBALANCEBEFORECLOSINGSURVEYS/100) * totalmaxpossiblespendforallsurveys)){
                    //The current balance is less than the shutdown threshold
                    if (totalremainingpossiblespendforallsurveys > ((MINAVAILABLEBALANCEBEFORECLOSINGSURVEYS/100) * totalmaxpossiblespendforallsurveys)){
                        //We're not in the final little piece of the survey
                        logger.debug("Setting survey status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "All surveys are being put into STATUS_WAITINGFORFUNDS for Researcher.researcherid="+researcher.getResearcherid()+" User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+") due to a lack of funds.");
                        xmpp.send();
                        List surveysOpen = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"' and status='"+Survey.STATUS_OPEN+"'").list();
                        for (Iterator iterator1 = surveysOpen.iterator(); iterator1.hasNext();) {
                            Survey survey = (Survey) iterator1.next();
                            logger.debug("operating on surveyid="+survey.getSurveyid());
                            survey.setStatus(Survey.STATUS_WAITINGFORFUNDS);
                            try{survey.save();} catch (GeneralException ex){logger.error(ex);}
                        }
                    }
                }
            } else {
                //Make sure this researcher has no surveys with status = STATUS_WAITINGFORFUNDS
                logger.debug("Making sure there are no surveys in status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                List surveysWaiting = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"' and status='"+Survey.STATUS_WAITINGFORFUNDS+"'").list();
                for (Iterator iterator1 = surveysWaiting.iterator(); iterator1.hasNext();) {
                    Survey survey = (Survey) iterator1.next();
                    logger.debug("operating on surveyid="+survey.getSurveyid());
                    survey.setStatus(Survey.STATUS_OPEN);
                    try{survey.save();} catch (GeneralException ex){logger.error(ex);}
                }
            }
        }
    }

}
