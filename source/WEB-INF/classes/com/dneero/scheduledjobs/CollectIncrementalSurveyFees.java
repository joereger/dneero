package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Researcher;
import com.dneero.dao.User;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.MoveMoneyInRealWorld;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CollectIncrementalSurveyFees implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static double MINPERCENTOFTOTALVALUEAVAILASBALANCE = 10;
    public static double INCREMENTALPERCENTTOCHARGE = 20;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CollectIncrementalSurveyFees called");

//        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
//                               .add( Restrictions.eq("status", Survey.STATUS_OPEN))
//                               .list();

        List surveys = HibernateUtil.getSession().createQuery("from Survey where "+
                                                              "("+
                                                              "status='"+Survey.STATUS_OPEN+"'"+
                                                              "or status='"+Survey.STATUS_WAITINGFORFUNDS+"'"+
                                                              "or status='"+Survey.STATUS_WAITINGFORSTARTDATE+"'"+
                                                              ")").list();

        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            HibernateUtil.getSession().refresh(survey);
            Researcher researcher = Researcher.get(survey.getResearcherid());
            User user = User.get(researcher.getUserid());
            logger.debug("Analyzing surveyid="+survey.getSurveyid());
            double maxresppay = (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested());
            double maximppay = ((survey.getWillingtopaypercpm()*survey.getMaxdisplaystotal())/1000);
            double maxspend =maxresppay + maximppay;
            double dfee = maxspend * .2;
            double maxpossiblespnd = maxspend + dfee;
            int responsesalready = survey.getResponses().size();
            double spentonresponsesalready = survey.getWillingtopayperrespondent() * responsesalready;
            int impressionsalready = 0;
            for (Iterator<Impression> iterator1 = survey.getImpressions().iterator(); iterator1.hasNext();) {
                Impression impression = iterator1.next();
                impressionsalready = impressionsalready + impression.getTotalimpressions();
            }
            double amtspentnimpressions = (Double.parseDouble(String.valueOf(impressionsalready)) * survey.getWillingtopaypercpm())/1000;
            double totalspentalready = spentonresponsesalready + amtspentnimpressions;
            double remainingpossiblespend = maxpossiblespnd - totalspentalready;

            double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);
            boolean needToChargeIncrementalPercent = false;

            //If the currentbalance is zero of course we need to charge
            if (currentbalance==0){
                needToChargeIncrementalPercent = true;
            }

            //If the current balance is less than the threshold percent of the maxpossiblespnd then we need to charge
            if (currentbalance <=(  (MINPERCENTOFTOTALVALUEAVAILASBALANCE /100) * maxpossiblespnd  )) {
                needToChargeIncrementalPercent = true;
            }

            //Figure out how much to charge and charge it
            if (needToChargeIncrementalPercent){
                //Start with the base incremental amount
                double amttocharge = (INCREMENTALPERCENTTOCHARGE/100) * maxpossiblespnd;
                //Override if there's not that much left in the survey
                if (remainingpossiblespend<amttocharge){
                    amttocharge = remainingpossiblespend;
                }
                //Add any negative balance
                if (currentbalance<0){
                    amttocharge = amttocharge + ((-1)*currentbalance);
                }
                //Move the money
                logger.debug("amttocharge:"+amttocharge+" to userid="+user.getUserid());
                if (amttocharge>0){
                    MoveMoneyInRealWorld.charge(user, amttocharge);
                    //@todo email that card has been charged by incremental
                }
            } else {
                logger.debug("no need to charge surveyid:"+survey.getSurveyid()+" to userid="+user.getUserid());
            }


        }

    }

}
