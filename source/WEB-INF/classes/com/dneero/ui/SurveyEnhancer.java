package com.dneero.ui;

import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.util.Time;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;

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
    private String responsesalreadygotten;
    private String impressionsalreadygotten;
    private String willingtopayforresponse;
    private String willingtopayforcpm;
    private String daysuntilend;
    private double minearningDbl;
    private String minearning;
    private double maxearningDbl;
    private String maxearning;
    private String numberofquestions;
    private String descriptiontruncated;

    public SurveyEnhancer(Survey survey){
        if (survey!=null && survey.getStartdate()!=null && survey.getEnddate()!=null){
            startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));

            enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));

            responsesalreadygotten = String.valueOf(survey.getResponses().size());
            impressionsalreadygotten = String.valueOf(NumFromUniqueResult.getInt("select sum(impressionstotal) from Impression where surveyid='"+survey.getSurveyid()+"'"));

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

            minearningDbl = survey.getIncentive().getBloggerEarningsPerResponse();
            minearning = "$"+Str.formatForMoney(minearningDbl);

            maxearningDbl = survey.getIncentive().getBloggerEarningsPerResponse()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );
            maxearning = "$"+Str.formatForMoney(maxearningDbl);

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

    public String getMinearning() {
        return minearning;
    }

    public void setMinearning(String minearning) {
        this.minearning = minearning;
    }

    public String getMaxearning() {
        return maxearning;
    }

    public void setMaxearning(String maxearning) {
        this.maxearning = maxearning;
    }

    public String getResponsesalreadygotten() {
        return responsesalreadygotten;
    }

    public void setResponsesalreadygotten(String responsesalreadygotten) {
        this.responsesalreadygotten = responsesalreadygotten;
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


    public double getMinearningDbl() {
        return minearningDbl;
    }

    public void setMinearningDbl(double minearningDbl) {
        this.minearningDbl = minearningDbl;
    }

    public double getMaxearningDbl() {
        return maxearningDbl;
    }

    public void setMaxearningDbl(double maxearningDbl) {
        this.maxearningDbl = maxearningDbl;
    }

    public String getDescriptiontruncated() {
        return descriptiontruncated;
    }

    public void setDescriptiontruncated(String descriptiontruncated) {
        this.descriptiontruncated = descriptiontruncated;
    }
}
