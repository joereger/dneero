package com.dneero.money;

import com.dneero.dao.Survey;
import com.dneero.dao.Impression;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 9:37:33 AM
 */
public class SurveyMoneyStatus {

    public static double PERSURVEYCREATIONFEE = 5.00;
    public static double DNEEROMARKUPPERCENT = 25;

    private double maxPossiblePayoutForResponses = 0;
    private double maxPossiblePayoutForImpressions = 0;
    private double maxPossiblePayoutToUsers = 0;
    private double maxPossibledNeeroFee = 0;
    private double maxPossibleSpend = 0;
    private int responsesToDate = 0;
    private double spentOnResponsesToDate = 0;
    private int impressionsToDate = 0;
    private double spentOnImpressionsToDate = 0;
    private double spentToDate = 0;
    private double remainingPossibleSpend = 0;


    public SurveyMoneyStatus(Survey survey){
        maxPossiblePayoutForResponses = (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested());
        maxPossiblePayoutForImpressions = ((survey.getWillingtopaypercpm()*survey.getMaxdisplaystotal())/1000);
        maxPossiblePayoutToUsers = maxPossiblePayoutForResponses + maxPossiblePayoutForImpressions;
        maxPossibledNeeroFee = maxPossiblePayoutToUsers * (DNEEROMARKUPPERCENT/100);
        maxPossibleSpend = maxPossiblePayoutToUsers + maxPossibledNeeroFee + PERSURVEYCREATIONFEE;
        responsesToDate = survey.getResponses().size();
        spentOnResponsesToDate = survey.getWillingtopayperrespondent() * responsesToDate;
        impressionsToDate = 0;
        for (Iterator<Impression> iterator2 = survey.getImpressions().iterator(); iterator2.hasNext();) {
            Impression impression = iterator2.next();
            impressionsToDate = impressionsToDate + impression.getTotalimpressions();
        }
        spentOnImpressionsToDate = (Double.parseDouble(String.valueOf(impressionsToDate)) * survey.getWillingtopaypercpm())/1000;
        spentToDate = spentOnResponsesToDate + spentOnImpressionsToDate;
        remainingPossibleSpend = maxPossibleSpend - spentToDate;    
    }


    public double getMaxPossiblePayoutForResponses() {
        return maxPossiblePayoutForResponses;
    }

    public double getMaxPossiblePayoutForImpressions() {
        return maxPossiblePayoutForImpressions;
    }

    public double getMaxPossiblePayoutToUsers() {
        return maxPossiblePayoutToUsers;
    }

    public double getMaxPossibledNeeroFee() {
        return maxPossibledNeeroFee;
    }

    public double getMaxPossibleSpend() {
        return maxPossibleSpend;
    }

    public int getResponsesToDate() {
        return responsesToDate;
    }

    public double getSpentOnResponsesToDate() {
        return spentOnResponsesToDate;
    }

    public int getImpressionsToDate() {
        return impressionsToDate;
    }

    public double getSpentOnImpressionsToDate() {
        return spentOnImpressionsToDate;
    }

    public double getSpentToDate() {
        return spentToDate;
    }

    public double getRemainingPossibleSpend() {
        return remainingPossibleSpend;
    }


}
