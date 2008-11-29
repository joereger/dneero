package com.dneero.ui;

import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.hibernate.NumFromUniqueResultImpressions;
import com.dneero.util.Time;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.helpers.SlotsRemainingInConvo;

import java.util.Calendar;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Sep 27, 2006
 * Time: 10:15:47 AM
 */
public class SurveyEnhancer implements Serializable {

    private String startdate;
    private String enddate;
    private String slotsremaining;
    private String impressionsalreadygotten;
    private String willingtopayforresponse;
    private String willingtopayforcpm;
    private String daysuntilend;
    private double maxEarningCPMDbl;
    private String maxEarningCPM;
    private String numberofquestions;
    private String descriptiontruncated;

    public SurveyEnhancer(Survey survey){
        if (survey!=null && survey.getStartdate()!=null && survey.getEnddate()!=null){
            startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));

            enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));

            slotsremaining= String.valueOf(SlotsRemainingInConvo.getSlotsRemaining(survey));
            impressionsalreadygotten = String.valueOf(NumFromUniqueResult.getInt("select sum(impressionstotal) from Response where surveyid='"+survey.getSurveyid()+"'"));

            willingtopayforresponse = survey.getIncentive().getShortSummary();

            willingtopayforcpm = "$"+ Str.formatForMoney(survey.getWillingtopaypercpm());

            int daysleft = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Calendar.getInstance());
            if (daysleft==0){
                daysuntilend = "Ends today!";
            } else if (daysleft==1){
                daysuntilend = "One day left!";
            } else {
                daysuntilend = daysleft + " days left!";
            }



            maxEarningCPMDbl = (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000;
            maxEarningCPM = "$"+Str.formatForMoney(maxEarningCPMDbl);

            if (survey.getQuestions()!=null){
                numberofquestions = String.valueOf(survey.getQuestions().size());
            } else {
                numberofquestions = "0";
            }

            descriptiontruncated = Str.truncateString(survey.getDescription(), 100);

        }

    }


    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }



    public String getWillingtopayforresponse() {
        return willingtopayforresponse;
    }

    public void setWillingtopayforresponse(String willingtopayforresponse) {
        this.willingtopayforresponse = willingtopayforresponse;
    }

    public String getWillingtopayforcpm() {
        return willingtopayforcpm;
    }

    public void setWillingtopayforcpm(String willingtopayforcpm) {
        this.willingtopayforcpm = willingtopayforcpm;
    }

    public String getDaysuntilend() {
        return daysuntilend;
    }

    public void setDaysuntilend(String daysuntilend) {
        this.daysuntilend = daysuntilend;
    }

    public String getSlotsremaining() {
        return slotsremaining;
    }

    public void setSlotsremaining(String slotsremaining) {
        this.slotsremaining=slotsremaining;
    }

    public String getImpressionsalreadygotten() {
        return impressionsalreadygotten;
    }

    public void setImpressionsalreadygotten(String impressionsalreadygotten) {
        this.impressionsalreadygotten = impressionsalreadygotten;
    }

    public String getNumberofquestions() {
        return numberofquestions;
    }

    public void setNumberofquestions(String numberofquestions) {
        this.numberofquestions = numberofquestions;
    }

    public double getMaxEarningCPMDbl() {
        return maxEarningCPMDbl;
    }

    public void setMaxEarningCPMDbl(double maxEarningCPMDbl) {
        this.maxEarningCPMDbl=maxEarningCPMDbl;
    }

    public String getMaxEarningCPM() {
        return maxEarningCPM;
    }

    public void setMaxEarningCPM(String maxEarningCPM) {
        this.maxEarningCPM=maxEarningCPM;
    }

    public String getDescriptiontruncated() {
        return descriptiontruncated;
    }

    public void setDescriptiontruncated(String descriptiontruncated) {
        this.descriptiontruncated = descriptiontruncated;
    }
}
