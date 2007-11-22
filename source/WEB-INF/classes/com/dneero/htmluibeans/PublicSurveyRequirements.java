package com.dneero.htmluibeans;

import com.dneero.dao.*;


import com.dneero.util.Num;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;

import org.apache.log4j.Logger;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyRequirements implements Serializable {



    private Survey survey;
    private String surveyCriteriaAsHtml;

    public PublicSurveyRequirements(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        }
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            Pagez.sendRedirect("/jsp/publicsurveylist.jsp");
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            Pagez.sendRedirect("/jsp/surveynotopen.jsp"); 
            return;
        }



        //Criteria for survey
        SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
        surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();


    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public String getSurveyCriteriaAsHtml() {
        return surveyCriteriaAsHtml;
    }

    public void setSurveyCriteriaAsHtml(String surveyCriteriaAsHtml) {
        this.surveyCriteriaAsHtml=surveyCriteriaAsHtml;
    }
}
