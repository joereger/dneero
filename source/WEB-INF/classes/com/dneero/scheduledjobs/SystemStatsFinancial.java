package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.survey.servlet.ImpressionsByDayUtil;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SystemStatsFinancial implements Job {

    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStatsFinancial in beans
    private static int unpaidresponses=0;
    private static double unpaidresponsesamt=0.0;
    private static HashMap<Integer, Double> amtpendingbynumberofdayswithimpressions= new HashMap<Integer, Double>();


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SystemStats called");

            unpaidresponses= 0;
            unpaidresponsesamt= 0.0;
            amtpendingbynumberofdayswithimpressions= new HashMap<Integer, Double>();
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                               .add(Restrictions.eq("ispaid", false))
                                               .add(Restrictions.ne("poststatus", Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED))
                                               .setCacheable(true)
                                               .list();
            if (responses!=null && responses.size()>0){
                unpaidresponses= responses.size();
            }
            for (Iterator<Response> iterator=responses.iterator(); iterator.hasNext();) {
                Response response=iterator.next();
                //Survey survey = Survey.get(response.getSurveyid());
                unpaidresponsesamt= unpaidresponsesamt + response.getIncentive().getBloggerEarningsPerResponse();

                //Get the impressions by day for this response
                ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil("");
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                                   .add(Restrictions.eq("responseid", response.getResponseid()))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Impression> iterator1=impressions.iterator(); iterator1.hasNext();) {
                    Impression impression=iterator1.next();
                    ImpressionsByDayUtil ibduTmp = new ImpressionsByDayUtil(impression.getImpressionsbyday());
                    ibdu.add(ibduTmp);
                }
                //Calculate the number of days with impressions within the allotted impression period
                int numberofdayswithimpressions = 0;
                for(int i=0; i<UpdateResponsePoststatus.MAXPOSTINGPERIODINDAYS; i++){
                    if (ibdu.getImpressionsForParticularDay(i)>0){
                        numberofdayswithimpressions = numberofdayswithimpressions + 1;
                    }
                }
                //If this number is greater than the number required, assume that this just hasn't yet been marked and lump it into the highest day
                if (numberofdayswithimpressions>UpdateResponsePoststatus.DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD){
                    numberofdayswithimpressions = UpdateResponsePoststatus.DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD;
                }
                //Add it to the hashmap keyed by day
                if (amtpendingbynumberofdayswithimpressions.containsKey(numberofdayswithimpressions)){
                    amtpendingbynumberofdayswithimpressions.put(numberofdayswithimpressions, amtpendingbynumberofdayswithimpressions.get(numberofdayswithimpressions)+response.getIncentive().getBloggerEarningsPerResponse());
                } else {
                    amtpendingbynumberofdayswithimpressions.put(numberofdayswithimpressions, response.getIncentive().getBloggerEarningsPerResponse());
                }
                //@todo expand this to include impression pay reporting
            }

        //} else {
            //logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        //}
    }


    public static int getUnpaidresponses() {
        return unpaidresponses;
    }

    public static void setUnpaidresponses(int unpaidresponses) {
        SystemStatsFinancial.unpaidresponses=unpaidresponses;
    }

    public static double getUnpaidresponsesamt() {
        return unpaidresponsesamt;
    }

    public static void setUnpaidresponsesamt(double unpaidresponsesamt) {
        SystemStatsFinancial.unpaidresponsesamt=unpaidresponsesamt;
    }

    public static HashMap<Integer, Double> getAmtpendingbynumberofdayswithimpressions() {
        return amtpendingbynumberofdayswithimpressions;
    }

    public static void setAmtpendingbynumberofdayswithimpressions(HashMap<Integer, Double> amtpendingbynumberofdayswithimpressions) {
        SystemStatsFinancial.amtpendingbynumberofdayswithimpressions=amtpendingbynumberofdayswithimpressions;
    }
}
