package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.MoveMoneyInRealWorld;
import com.dneero.money.TwitaskMoneyStatus;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;

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
            //Warning: this query must include Survey.STATUS_WAITINGFORFUNDS because surveys is used in two sections of this code
            List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"' and status<>'"+Survey.STATUS_DRAFT+"'").setCacheable(true).list();
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
                    //If we're still paying out
                    int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
                    if (dayssinceclose<=SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS){
                        logger.debug("surveyid="+survey.getSurveyid()+" survey is not draft");
                        SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                        logger.debug("surveyid="+survey.getSurveyid()+" sms.getRemainingPossibleSpend()="+sms.getRemainingPossibleSpend());
                        logger.debug("surveyid="+survey.getSurveyid()+" sms.getMaxPossibleSpend()="+sms.getMaxPossibleSpend());
                        totalremainingpossiblespendforallsurveys = totalremainingpossiblespendforallsurveys + sms.getRemainingPossibleSpend();
                        totalmaxpossiblespendforallsurveys = totalmaxpossiblespendforallsurveys + sms.getMaxPossibleSpend();
                    }
                }
            }
            logger.debug("end iterating surveys for researcherid="+researcher.getResearcherid());
            logger.debug("totalremainingpossiblespendforallsurveys="+totalremainingpossiblespendforallsurveys);
            logger.debug("totalmaxpossiblespendforallsurveys="+totalmaxpossiblespendforallsurveys);

            //Collect TwitAsk data on this researcher
            //Warning: this query must include Survey.STATUS_WAITINGFORFUNDS because surveys is used in two sections of this code
            List twitasks = HibernateUtil.getSession().createQuery("from Twitask where userid='"+researcher.getUserid()+"' and status<>'"+Twitask.STATUS_DRAFT+"'").setCacheable(true).list();
            double totalremainingpossiblespendforalltwitasks = 0;
            double totalmaxpossiblespendforalltwitasks = 0;
            logger.debug("userid="+user.getUserid()+ " ("+user.getFirstname() + " "+user.getLastname()+")");
            logger.debug("researcherid="+researcher.getResearcherid());
            logger.debug("twitasks.size()="+twitasks.size());
            logger.debug("start iterating twitasks for researcherid="+researcher.getResearcherid());
            for (Iterator iterator1 = twitasks.iterator(); iterator1.hasNext();) {
                Twitask twitask= (Twitask) iterator1.next();
                logger.debug("found twitask for researcher... twitaskid="+ twitask.getTwitaskid());
                if (twitask.getStatus()!=Twitask.STATUS_DRAFT){
                    //If we're still paying out
                    int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(twitask.getClosedintwitterdate()));
                    if (dayssinceclose<=5){
                        logger.debug("twitaskid="+ twitask.getTwitaskid()+" twitask is not draft");
                        TwitaskMoneyStatus sms = new TwitaskMoneyStatus(twitask);
                        logger.debug("twitaskid="+ twitask.getTwitaskid()+" sms.getRemainingPossibleSpend()="+sms.getRemainingPossibleSpend());
                        logger.debug("twitaskid="+ twitask.getTwitaskid()+" sms.getMaxPossibleSpend()="+sms.getMaxPossibleSpend());
                        totalremainingpossiblespendforalltwitasks = totalremainingpossiblespendforalltwitasks + sms.getRemainingPossibleSpend();
                        totalmaxpossiblespendforalltwitasks = totalmaxpossiblespendforalltwitasks + sms.getMaxPossibleSpend();
                    }
                }
            }
            logger.debug("end iterating twitasks for researcherid="+researcher.getResearcherid());
            logger.debug("totalremainingpossiblespendforalltwitasks="+totalremainingpossiblespendforalltwitasks);
            logger.debug("totalmaxpossiblespendforalltwitasks="+totalmaxpossiblespendforalltwitasks);


            //Now add the Twitask stuff to the Survey stuff
            totalremainingpossiblespendforallsurveys = totalremainingpossiblespendforallsurveys + totalremainingpossiblespendforalltwitasks;
            totalmaxpossiblespendforallsurveys = totalmaxpossiblespendforallsurveys + totalmaxpossiblespendforalltwitasks;


            //Now operate on surveys
            double amttocharge = 0.0;
            //CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
            double currentbalance = user.getCurrentbalance();
            if (currentbalance < ((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)){
                logger.debug("current balance is less than MINPERCENTOFTOTALVALUEAVAILASBALANCE of totalmaxpossiblespendforallsurveys ("+((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)+")");
                //The current balance is less than 10% of the total value of all surveys so I need to know how much to charge them
                amttocharge = (INCREMENTALPERCENTTOCHARGE/100) * totalmaxpossiblespendforallsurveys;
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
                    MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, (-1)*amttocharge, true, false, false, false);
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
                                try{survey.save();} catch (GeneralException ex){logger.error("",ex);}
                            }
                        }
                        //Shut down Twitasks
                        List twitasksOpen = HibernateUtil.getSession().createQuery("from Twitask where userid='"+researcher.getUserid()+"'").setCacheable(true).list();
                        boolean shutDownATwitask = false;
                        for (Iterator iterator1 = twitasksOpen.iterator(); iterator1.hasNext();) {
                            Twitask twitask = (Twitask) iterator1.next();
                            if (twitask.getStatus()==Twitask.STATUS_OPEN){
                                shutDownATwitask = true;
                                logger.debug("operating on twitaskid="+twitask.getTwitaskid());
                                twitask.setStatus(Twitask.STATUS_WAITINGFORFUNDS);
                                try{twitask.save();} catch (GeneralException ex){logger.error("",ex);}
                            }
                        }
                        //Notify
                        if (shutDownASurvey){
                            logger.debug("Setting survey status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "All surveys are being put into STATUS_WAITINGFORFUNDS for Researcher.researcherid="+researcher.getResearcherid()+" User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+") due to a lack of funds.");
                            xmpp.send();
                        }
                        if (shutDownATwitask){
                            logger.debug("Setting twitask status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "All twitasks are being put into STATUS_WAITINGFORFUNDS for Researcher.researcherid="+researcher.getResearcherid()+" User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+") due to a lack of funds.");
                            xmpp.send();
                        }
                    }


                }
            } else {
                logger.debug("current balance is greater than MINPERCENTOFTOTALVALUEAVAILASBALANCE of totalmaxpossiblespendforallsurveys("+((MINPERCENTOFTOTALVALUEAVAILASBALANCE/100) * totalmaxpossiblespendforallsurveys)+")");
                //Make sure this researcher has no surveys with status = STATUS_WAITINGFORFUNDS
                logger.debug("making sure there are no surveys in status=STATUS_WAITINGFORFUNDS for researcherid="+researcher.getResearcherid());
                //List twitasksWaiting = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+researcher.getResearcherid()+"' and status='"+Survey.STATUS_WAITINGFORFUNDS+"'").setCacheable(false).list();
                //Just use list of surveys from above
                List surveysWaiting = surveys;
                logger.debug("twitasksWaiting.size()="+surveysWaiting.size());
                for (Iterator iterator1 = surveysWaiting.iterator(); iterator1.hasNext();) {
                    Survey survey = (Survey) iterator1.next();
                    logger.debug("found surveyid="+survey.getSurveyid());
                    if (survey.getStatus()==Survey.STATUS_WAITINGFORFUNDS){
                        logger.debug("operating on surveyid="+survey.getSurveyid());
                        survey.setStatus(Survey.STATUS_OPEN);
                        try{survey.save();} catch (GeneralException ex){logger.error("",ex);}
                        //InstantNotify
                        InstantNotifyOfNewSurvey inons = new InstantNotifyOfNewSurvey(survey.getSurveyid());
                        inons.sendNotifications();
                    }
                }
                //Open Twitasks
                List twitasksWaiting= twitasks;
                logger.debug("twitasksWaiting.size()="+ twitasksWaiting.size());
                for (Iterator iterator1 = twitasksWaiting.iterator(); iterator1.hasNext();) {
                    Twitask twitask = (Twitask) iterator1.next();
                    logger.debug("found twitaskid="+twitask.getTwitaskid());
                    if (twitask.getStatus()==Twitask.STATUS_WAITINGFORFUNDS){
                        logger.debug("operating on twitaskid="+twitask.getTwitaskid());
                        twitask.setStatus(Twitask.STATUS_WAITINGFORSTARTDATE);
                        try{twitask.save();} catch (GeneralException ex){logger.error("",ex);}
                    }
                }
            }
            //Update researcher nums
            double percentofmax = 0;
            try{
                if (totalmaxpossiblespendforallsurveys>0){
                    percentofmax = (100)*currentbalance/totalmaxpossiblespendforallsurveys;
                }
            } catch (Exception ex){
                logger.error("",ex);
            }
            //Determine whether I should save by manually checking vars
            boolean shouldsave = false;
            if (researcher.getNotaccurateamttocharge()!=amttocharge){
                shouldsave = true;
            }
            if (researcher.getNotaccuratecurrbalance()!=currentbalance){
                shouldsave = true;
            }
            if (researcher.getNotaccuratemaxpossspend()!=totalmaxpossiblespendforallsurveys){
                shouldsave = true;
            }
            if (researcher.getNotaccurateremainingpossspend()!=totalremainingpossiblespendforallsurveys){
                shouldsave = true;
            }
            if (researcher.getNotaccuratepercentofmax()!=percentofmax){
                shouldsave = true;
            }
            //Only save if necessary
            if (shouldsave){
                logger.debug("will call researcher.save()");
                researcher.setNotaccurateamttocharge(amttocharge);
                researcher.setNotaccuratecurrbalance(currentbalance);
                researcher.setNotaccuratemaxpossspend(totalmaxpossiblespendforallsurveys);
                researcher.setNotaccurateremainingpossspend(totalremainingpossiblespendforallsurveys);
                researcher.setNotaccuratepercentofmax(percentofmax);
                try{researcher.save();}catch(Exception ex){logger.error("",ex);}
            } else {
                logger.debug("will not call researcher.save() because nothing changed");
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
