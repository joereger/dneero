package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.money.CurrentBalanceCalculator;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CloseSurveysByFundsAvailability implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() CloseSurveysByFundsAvailability called");

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                               .add( Restrictions.eq("status", Survey.STATUS_OPEN))
                               .list();

        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            Researcher researcher = Researcher.get(survey.getResearcherid());
            User user = User.get(researcher.getUserid());

            double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);

            if (currentbalance<=0){
                survey.setStatus(Survey.STATUS_WAITINGFORFUNDS);
                try{
                    survey.save();
                } catch (GeneralException ex){
                    logger.error(ex);
                }
            }

        }

    }

}
