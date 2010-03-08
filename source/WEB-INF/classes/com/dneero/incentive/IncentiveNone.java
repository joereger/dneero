package com.dneero.incentive;

import com.dneero.dao.Response;
import com.dneero.dao.Surveyincentive;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:39:06 PM
 */
public class IncentiveNone implements Incentive {

    public static int ID = 3;
    public static String name = "No Incentive";
    private Surveyincentive surveyincentive;

    public IncentiveNone(Surveyincentive surveyincentive){
        this.surveyincentive = surveyincentive;
    }

    public int getID() {
        return ID;
    }

    public String getSystemName() {
        return name;
    }

    public double getResearcherCostPerResponse() {
        double out = 0.0;
        return out;
    }

    public double getBloggerEarningsPerResponse() {
        double out = 0.0;
        return out;
    }

    public double getEstimatedMaxValueOfEarnings(){
        double out = 0.0;
        return out;
    }

    public void doAwardIncentive(Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public void doRemoveIncentive(Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        return out.toString();
    }

    public String getFullSummaryHtml() {
        StringBuffer out = new StringBuffer();
        return out.toString();
    }

    public String getInstructions() {
        StringBuffer out = new StringBuffer();
        return out.toString();
    }

    public Surveyincentive getSurveyincentive() {
        return this.surveyincentive;
    }

    public void doImmediatelyAfterResponse(Response response) {

    }

    public String getInstructionsAfterResponse(Response response) {
        return "";
    }

    public String getInstructionsAfterAward(Response response) {
        return "";
    }
}