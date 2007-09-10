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
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;

import java.util.List;
import java.util.Iterator;

/**
 * Handles pre-pay for surveys at 20% intervals.
 */
public class ResearcherRemainingBalanceOperations implements Job {



    public static double MINAVAILABLEBALANCEBEFORECLOSINGSURVEYS = 5;
    public static double MINPERCENTOFTOTALVALUEAVAILASBALANCE = 10;
    public static double INCREMENTALPERCENTTOCHARGE = 20;

    private int researcherid = 0;


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ResearcherRemainingBalanceOperations called");
            String rschStr = "";
            if (researcherid>0){
                rschStr = " where researcherid='"+researcherid+"'";
            }
            List researchers = HibernateUtil.getSession().createQuery("from Researcher" + rschStr).setCacheable(false).list();
            for (Iterator iterator = researchers.iterator(); iterator.hasNext();) {
                Researcher researcher = (Researcher) iterator.next();
                processResearcher(researcher);
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

    public static void processResearcher(Researcher researcher){
        Logger logger = Logger.getLogger(ResearcherRemainingBalanceOperations.class);
        if (researcher!=null){
            logger.debug("--------------");
            User user = User.get(researcher.getUserid());
            //Collect data on this researcher
            List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"'").setCacheable(false).list();
            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
            double currentbalance = cbc.getCurrentbalance();
            double totalremainingpossiblespendforallsurveys = 0;
            double totalmaxpossiblespendforallsurveys = 0;
            logger.debug("userid="+user.getUserid()+ " ("+user.getFirstname() + " "+user.getLastname()+")");
            logger.debug("researcherid="+researcher.getResearcherid());
            logger.debug("surveys.size()="+surveys.size());
            logger.debug("start iterating surveys for researcherid="+researcher.getResearcherid());
            for (Iterator iterator1 = surveys.iterator(); iterator1.hasNext();) {
                Survey survey = (Survey) iterator1.next();
                logger.debug("found survey for researcher... surveyid="+survey.getSurveyid());
                if (survey.getStatus()!=Survey.STATUS_DRAFT){
                    logger.debug("surveyid="+survey.getSurveyid()+" survey is not draft");
                    SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                    logger.debug("surveyid="+survey.getSurveyid()+" sms.getRemainingPossibleSpend()="+sms.getRemainingPossibleSpend());
                    logger.debug("surveyid="+survey.getSurveyid()+" sms.getMaxPossibleSpend()="+sms.getMaxPossibleSpend());
                    totalremainingpossiblespendforallsurveys = totalremainingpossiblespendforallsurveys + sms.getRemainingPossibleSpend();
                    totalmaxpossiblespendforallsurveys = totalmaxpossiblespendforallsurveys + sms.getMaxPossibleSpend();
                }
            }
            logger.debug("end iterating surveys for researcherid="+researcher.getResearcherid());
            logger.debug("currentbalance="+currentbalance);
            logger.debug("totalremainingpossiblespendforallsurveys="+totalremainingpossiblespendforallsurveys);
            logger.debug("totalmaxpossiblespendforallsurveys="+totalmaxpossiblespendforallsurveys);

            //Now operate on surveys
            if (currentbalance < ((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)){
                logger.debug("current balance is less than MINPERCENTOFTOTALVALUEAVAILASBALANCE of totalmaxpossiblespendforallsurveys ("+((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)+")");
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
                        List surveysOpen = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"'").setCacheable(false).list();
                        boolean shutDownASurvey = false;
                        for (Iterator iterator1 = surveysOpen.iterator(); iterator1.hasNext();) {
                            Survey survey = (Survey) iterator1.next();
                            if (survey.getStatus()==Survey.STATUS_OPEN){
                                shutDownASurvey = true;
                                logger.debug("operating on surveyid="+survey.getSurveyid());
                                survey.setStatus(Survey.STATUS_WAITINGFORFUNDS);
                                try{survey.save();} catch (GeneralException ex){logger.error(ex);}
                            }
                        }
                        if (shutDownASurvey){
                            logger.debug("Setting survey status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "All surveys are being put into STATUS_WAITINGFORFUNDS for Researcher.researcherid="+researcher.getResearcherid()+" User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+") due to a lack of funds.");
                            xmpp.send();
                        }
                    }
                }
            } else {
                logger.debug("current balance is greater than MINPERCENTOFTOTALVALUEAVAILASBALANCE of totalmaxpossiblespendforallsurveys("+((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)+")");
                //Make sure this researcher has no surveys with status = STATUS_WAITINGFORFUNDS
                logger.debug("making sure there are no surveys in status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                List surveysWaiting = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"'").setCacheable(false).list();
                logger.debug("surveysWaiting.size()="+surveysWaiting.size());
                for (Iterator iterator1 = surveysWaiting.iterator(); iterator1.hasNext();) {
                    Survey survey = (Survey) iterator1.next();
                    logger.debug("found surveyid="+survey.getSurveyid());
                    if (survey.getStatus()==Survey.STATUS_WAITINGFORFUNDS){
                        logger.debug("operating on surveyid="+survey.getSurveyid());
                        survey.setStatus(Survey.STATUS_OPEN);
                        try{survey.save();} catch (GeneralException ex){logger.error(ex);}
                        //InstantNotify
                        InstantNotifyOfNewSurvey inons = new InstantNotifyOfNewSurvey(survey.getSurveyid());
                        inons.sendNotifications();
                    }
                }
            }
            logger.debug("--------------");
        }
    }

    public int getResearcherid() {
        return researcherid;
    }

    public void setResearcherid(int researcherid) {
        this.researcherid = researcherid;
    }
}
