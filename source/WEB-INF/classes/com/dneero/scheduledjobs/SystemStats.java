package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Userpersistentlogin;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.util.Time;
import com.dneero.session.PersistentLogin;
import com.dneero.ui.SurveyEnhancer;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SystemStats implements Job {



    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStats in beans 
    private static int totalusers=0;
    private static int totalbloggers=0;
    private static int totalresearchers=0;
    private static int totalimpressions=0;
    private static int totalsurveystaken=0;
    private static double dollarsavailabletobloggers=0;
    private static double systembalance=0;
    private static double systembalancerealworld=0;
    private static double systembalancetotal=0;
    private static int numberofsurveysopen=0;
    private static int[] spotlightsurveys = new int[10];
    private static Map<String, SurveyEnhancer> spotlightsurveyenhancers = new HashMap<String, SurveyEnhancer>();


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SystemStats called");

            totalusers = NumFromUniqueResult.getInt("select count(*) from User");
            totalbloggers = NumFromUniqueResult.getInt("select count(*) from Blogger");
            totalresearchers = NumFromUniqueResult.getInt("select count(*) from Researcher");
            totalsurveystaken = NumFromUniqueResult.getInt("select count(*) from Response");
            totalimpressions = NumFromUniqueResult.getInt("select sum(impressionstotal) from Survey");
            numberofsurveysopen = NumFromUniqueResult.getInt("select count(*) from Survey where status='"+Survey.STATUS_OPEN+"'");


            dollarsavailabletobloggers = 0;
            List opensurveys = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_OPEN +"'").list();
            if (opensurveys!=null){
                for (Iterator iterator = opensurveys.iterator(); iterator.hasNext();) {
                    Survey survey = (Survey) iterator.next();
                    dollarsavailabletobloggers = dollarsavailabletobloggers + (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested()) + ((survey.getWillingtopaypercpm() * survey.getMaxdisplaystotal())/1000);
                }
            }

            systembalance = NumFromUniqueResult.getDouble("select sum(amt) from Balance");
            systembalancerealworld = (-1)*NumFromUniqueResult.getDouble("select sum(amt) from Balancetransaction where issuccessful=true");
            systembalancetotal = systembalancerealworld - systembalance;

            int surveyindex = 0;
            int numberofsurveystostore = 10;
            spotlightsurveys = new int[10];
            spotlightsurveyenhancers = new HashMap<String, SurveyEnhancer>();
            //Try just getting spotlights
            List surveys = HibernateUtil.getSession().createQuery("from Survey where isspotlight=true and status='"+Survey.STATUS_OPEN+"'").list();
            for (Iterator iterator = surveys.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();
                SurveyEnhancer surveyenhancer = new SurveyEnhancer(survey);
                spotlightsurveys[surveyindex]=survey.getSurveyid();
                spotlightsurveyenhancers.put(String.valueOf(surveyindex), surveyenhancer);
                surveyindex = surveyindex + 1;
            }
            //Fill up the rest with surveys
            if (surveyindex<numberofsurveystostore){
                int needtoaddthismanysurveys = numberofsurveystostore-surveyindex;
                List addlsurveys = HibernateUtil.getSession().createQuery("from Survey where isspotlight=false and status='"+Survey.STATUS_OPEN+"' order by willingtopayperrespondent desc").setMaxResults(needtoaddthismanysurveys).list();
                for (Iterator iterator = addlsurveys.iterator(); iterator.hasNext();) {
                    Survey survey = (Survey) iterator.next();
                    SurveyEnhancer surveyenhancer = new SurveyEnhancer(survey);
                    spotlightsurveys[surveyindex]=survey.getSurveyid();
                    spotlightsurveyenhancers.put(String.valueOf(surveyindex), surveyenhancer);
                    surveyindex = surveyindex + 1;
                }
            }
            //Fill up even more...
            if (surveyindex<numberofsurveystostore){
                int needtoaddthismanysurveys = numberofsurveystostore-spotlightsurveys.length;
                for (int i = 0; i < needtoaddthismanysurveys; i++) {
                    if (surveyindex>0){
                        spotlightsurveys[surveyindex]=spotlightsurveys[0];
                        spotlightsurveyenhancers.put(String.valueOf(surveyindex), spotlightsurveyenhancers.get("0"));
                    } else {
                        spotlightsurveys[surveyindex]=0;
                        spotlightsurveyenhancers.put(String.valueOf(surveyindex), new SurveyEnhancer(new Survey()));
                    }
                    surveyindex = surveyindex + 1;
                }
            }
     

        //} else {
            //logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        //}
    }


    public static int getTotalsurveystaken() {
        return totalsurveystaken;
    }

    public static void setTotalsurveystaken(int totalsurveystaken) {
        //SystemStats.totalsurveystaken=totalsurveystaken;
    }

    public static int getTotalbloggers() {
        return totalbloggers;
    }

    public static void setTotalbloggers(int totalbloggers) {
        //SystemStats.totalbloggers = totalbloggers;
    }

   

    public static int getTotalresearchers() {
        return totalresearchers;
    }

    public static void setTotalresearchers(int totalresearchers) {
        //SystemStats.totalresearchers = totalresearchers;
    }

    public static int getTotalimpressions() {
        return totalimpressions;
    }

    public static void setTotalimpressions(int totalimpressions) {
        //SystemStats.totalimpressions = totalimpressions;
    }


  

    public static double getDollarsavailabletobloggers() {
        return dollarsavailabletobloggers;
    }

    public static void setDollarsavailabletobloggers(double dollarsavailabletobloggers) {
        //SystemStats.dollarsavailabletobloggers = dollarsavailabletobloggers;
    }

    public static double getSystembalance() {
        return systembalance;
    }

    public static void setSystembalance(double systembalance) {
        //SystemStats.systembalance = systembalance;
    }

    public static double getSystembalancerealworld() {
        return systembalancerealworld;
    }

    public static void setSystembalancerealworld(double systembalancerealworld) {
        //SystemStats.systembalancerealworld = systembalancerealworld;
    }

    public static double getSystembalancetotal() {
        return systembalancetotal;
    }

    public static void setSystembalancetotal(double systembalancetotal) {
        //SystemStats.systembalancetotal = systembalancetotal;
    }

    public static int getNumberofsurveysopen() {
        return numberofsurveysopen;
    }

    public static void setNumberofsurveysopen(int numberofsurveysopen) {
        //SystemStats.numberofsurveysopen = numberofsurveysopen;
    }


    public static int[] getSpotlightsurveys() {
        return spotlightsurveys;
    }

    public static void setSpotlightsurveys(int[] spotlightsurveys) {
        SystemStats.spotlightsurveys = spotlightsurveys;
    }

    public static Map<String, SurveyEnhancer> getSpotlightsurveyenhancers() {
        return spotlightsurveyenhancers;
    }

    public static void setSpotlightsurveyenhancers(Map<String, SurveyEnhancer> spotlightsurveyenhancers) {
        //SystemStats.spotlightsurveyenhancers = spotlightsurveyenhancers;
    }

    public static int getTotalusers() {
        return totalusers;
    }

    public static void setTotalusers(int totalusers) {
        //SystemStats.totalusers=totalusers;
    }
}
