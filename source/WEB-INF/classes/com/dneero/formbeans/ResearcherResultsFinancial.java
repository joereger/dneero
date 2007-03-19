package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.util.Jsf;
import com.dneero.money.SurveyMoneyStatus;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:08:14 AM
 */
public class ResearcherResultsFinancial implements Serializable {

    private Survey survey;
    private SurveyMoneyStatus sms;


    public ResearcherResultsFinancial(){
        load();
    }

    private void load(){
        Survey survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            this.survey = survey;
            sms = new SurveyMoneyStatus(survey);
        }
    }


    public SurveyMoneyStatus getSms() {
        return sms;
    }

    public void setSms(SurveyMoneyStatus sms) {
        this.sms = sms;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
