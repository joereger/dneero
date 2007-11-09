package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;

import com.dneero.money.SurveyMoneyStatus;
import com.dneero.htmlui.Pagez;

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
       
    }



    public void initBean(){
        Survey survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
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
