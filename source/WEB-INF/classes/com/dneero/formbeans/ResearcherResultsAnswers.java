package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyResultsDisplay;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResultsAnswers {


    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private String results;

    public ResearcherResultsAnswers(){
        logger.debug("Instanciating");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            results = SurveyResultsDisplay.getHtmlForResults(survey, null);
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }


}
