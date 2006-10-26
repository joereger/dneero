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
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ReopenSurveysByFundsAvailability implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() ReopenSurveysByFundsAvailability called");

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                               .add( Restrictions.eq("status", Survey.STATUS_WAITINGFORFUNDS))
                               .list();

        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            Researcher researcher = Researcher.get(survey.getResearcherid());
            User user = User.get(researcher.getUserid());
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

            //If the current balance is less than the threshold percent of the maxpossiblespnd then we need to charge
            if (currentbalance>=remainingpossiblespend || currentbalance >=( (CollectIncrementalSurveyFees.MINPERCENTOFTOTALVALUEAVAILASBALANCE /100) * maxpossiblespnd  )) {
                survey.setStatus(Survey.STATUS_OPEN);
                try{
                    survey.save();
                } catch (GeneralException ex){
                    logger.error(ex);
                }
            }




        }

    }

}
