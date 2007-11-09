package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyResultsDisplay;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResultsAnswers implements Serializable {

    private Survey survey;
    private String results;

    public ResearcherResultsAnswers(){

    }

    public String beginView(){
        loadSurvey(Pagez.getUserSession().getCurrentSurveyid());
        return "researcherresultsanswers";    
    }


    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        survey = Survey.get(surveyid);
        if (survey!=null){
            results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>());
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
