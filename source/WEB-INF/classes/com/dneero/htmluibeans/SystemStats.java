package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.ui.SurveyEnhancer;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Feb 21, 2007
 * Time: 11:26:03 AM
 */
public class SystemStats implements Serializable {

    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStats in scheduledjobs
    //These need to be here because of direct POJO injection by the JSF framework
    private int totalusers=com.dneero.scheduledjobs.SystemStats.getTotalusers();
    private int totalbloggers=com.dneero.scheduledjobs.SystemStats.getTotalbloggers();
    private int totalresearchers=com.dneero.scheduledjobs.SystemStats.getTotalresearchers();
    private int totalsurveystaken=com.dneero.scheduledjobs.SystemStats.getTotalsurveystaken();
    private int totalimpressions=com.dneero.scheduledjobs.SystemStats.getTotalimpressions();
    private double dollarsavailabletobloggers=com.dneero.scheduledjobs.SystemStats.getDollarsavailabletobloggers();
    private double systembalance=com.dneero.scheduledjobs.SystemStats.getSystembalance();
    private double systembalancerealworld=com.dneero.scheduledjobs.SystemStats.getSystembalancerealworld();
    private double systembalancetotal=com.dneero.scheduledjobs.SystemStats.getSystembalancetotal();
    private int numberofsurveysopen=com.dneero.scheduledjobs.SystemStats.getNumberofsurveysopen();

    public SystemStats(){}

    public void initBean(){
        
    }

    public int getTotalusers() {
        return com.dneero.scheduledjobs.SystemStats.getTotalusers();
    }

    public void setTotalusers(int totalusers) {

    }

    public int getTotalbloggers() {
        return com.dneero.scheduledjobs.SystemStats.getTotalbloggers();
    }

    public void setTotalbloggers(int totalbloggers) {

    }

    public int getTotalresearchers() {
        return com.dneero.scheduledjobs.SystemStats.getTotalresearchers();
    }

    public void setTotalresearchers(int totalresearchers) {

    }

    public int getTotalimpressions() {
        return com.dneero.scheduledjobs.SystemStats.getTotalimpressions();
    }

    public void setTotalimpressions(int totalimpressions) {

    }


    public int getTotalsurveystaken() {
        return com.dneero.scheduledjobs.SystemStats.getTotalsurveystaken();
    }

    public void setTotalsurveystaken(int totalsurveystaken) {

    }

    public double getDollarsavailabletobloggers() {
        return com.dneero.scheduledjobs.SystemStats.getDollarsavailabletobloggers();
    }

    public void setDollarsavailabletobloggers(double dollarsavailabletobloggers) {

    }

    public double getSystembalance() {
        return com.dneero.scheduledjobs.SystemStats.getSystembalance();
    }

    public void setSystembalance(double systembalance) {

    }

    public double getSystembalancerealworld() {
        return com.dneero.scheduledjobs.SystemStats.getSystembalancerealworld();
    }

    public void setSystembalancerealworld(double systembalancerealworld) {

    }

    public double getSystembalancetotal() {
        return com.dneero.scheduledjobs.SystemStats.getSystembalancetotal();
    }

    public void setSystembalancetotal(double systembalancetotal) {

    }

    public int getNumberofsurveysopen() {
        return com.dneero.scheduledjobs.SystemStats.getNumberofsurveysopen();
    }

    public void setNumberofsurveysopen(int numberofsurveysopen) {

    }


//    public Map<String, Survey> getSpotlightsurveys() {
//        //Logger logger = Logger.getLogger(com.dneero.formbeans.SystemStats.class);
//        //logger.debug("getSpotlightsurveys() called");
//        return com.dneero.scheduledjobs.SystemStats.getSpotlightsurveys();
//    }
//
//    public void setSpotlightsurveys(Map<String, Survey> spotlightsurveys) {
//
//    }
//
//    public Map<String, SurveyEnhancer> getSpotlightsurveyenhancers() {
//        return com.dneero.scheduledjobs.SystemStats.getSpotlightsurveyenhancers();
//    }
//
//    public void setSpotlightsurveyenhancers(Map<String, SurveyEnhancer> spotlightsurveyenhancers) {
//
//    }
}
