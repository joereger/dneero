package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;

import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.SurveyResultsUserQuestionsListitem;
import com.dneero.display.SurveyResultsUserQuestions;
import com.dneero.htmlui.Pagez;
import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.dbgrid.GridCol;
import com.dneero.dbgrid.Grid;
import com.dneero.util.Num;

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


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            //results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), null, true, false);

            int page = 1;
            if (Pagez.getRequest().getParameter("page")!=null && Num.isinteger(Pagez.getRequest().getParameter("page"))){
                page = Integer.parseInt(Pagez.getRequest().getParameter("page"));
            }

            String resultsHtmlKey = "results_answers.jsp-results-surveyid"+survey.getSurveyid()+"-page"+page;
            String group = "ResearcherResultsAnswers.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(resultsHtmlKey, group);
            if (fromCache!=null){
                try{results = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                //Go get all results for user questions
                results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), null, true, false);
                //Store it in the cache
                DbcacheexpirableCache.put(resultsHtmlKey, group, results, DbcacheexpirableCache.expireSurveyInXHrs(survey, 1));
            }

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
