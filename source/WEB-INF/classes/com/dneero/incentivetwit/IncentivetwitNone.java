package com.dneero.incentivetwit;

import com.dneero.dao.Twitanswer;
import com.dneero.dao.Twitaskincentive;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:39:06 PM
 */
public class IncentivetwitNone implements Incentivetwit {

    public static int ID = 3;
    public static String name = "No Incentive";
    private Twitaskincentive twitaskincentive;


    public IncentivetwitNone(Twitaskincentive twitaskincentive){
        this.twitaskincentive = twitaskincentive;
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

    public void doAwardIncentive(Twitanswer twitanswer) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("doAwardIncentive called!");

    }

    public void doRemoveIncentive(Twitanswer twitanswer) {
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

    public Twitaskincentive getTwitaskincentive() {
        return this.twitaskincentive;
    }

    public void doImmediatelyAfterResponse(Twitanswer twitanswer) {

    }

    public String getInstructionsAfterResponse(Twitanswer twitanswer) {
        return "";
    }

    public String getInstructionsAfterAward(Twitanswer twitanswer) {


        return "";
    }
}