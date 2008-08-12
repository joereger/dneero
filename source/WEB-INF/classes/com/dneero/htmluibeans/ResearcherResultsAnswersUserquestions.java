package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;

import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.SurveyResultsUserQuestionsListitem;
import com.dneero.display.SurveyResultsUserQuestions;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Num;
import com.dneero.cache.html.HtmlCache;
import com.dneero.dbgrid.GridCol;
import com.dneero.dbgrid.Grid;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResultsAnswersUserquestions implements Serializable {

    private Survey survey;
    private String results;

    public ResearcherResultsAnswersUserquestions(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), null, true, false);

            int userquestionspage = 1;
            if (Pagez.getRequest().getParameter("userquestionspage")!=null && Num.isinteger(Pagez.getRequest().getParameter("userquestionspage"))){
                userquestionspage = Integer.parseInt(Pagez.getRequest().getParameter("userquestionspage"));
            }
            String resultsHtmlKey = "results_answers_userquestions.jsp-resultsUserquestionsHtml-surveyid"+survey.getSurveyid()+"-userquestionspage"+userquestionspage;
            if (HtmlCache.isStale(resultsHtmlKey, 6000)){
                //Go get all results for user questions
                ArrayList<SurveyResultsUserQuestionsListitem> sruqli = SurveyResultsUserQuestions.getUserQuestionResults(survey, null, 0, new ArrayList<Integer>(), null);
                //Create a template for the display
                StringBuffer template = new StringBuffer();
                template.append("<font class=\"smallfont\"><b><a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a> wanted to know:</b></font>");
                template.append("<br/>");
                template.append("<b><$question.question$></b>");
                template.append("<br/>");
                template.append("<$htmlForResult$>");
                template.append("<br/>");
                template.append("<br/>");
                //Create a Grid rendering
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("", template.toString(), false, "", "", "background: #ffffff;", ""));
                results = Grid.render(sruqli, cols, 50, "/researcher/results_answers_userquestions.jsp?surveyid="+survey.getSurveyid(), "userquestionspage");
                //Store it in the cache
                HtmlCache.updateCache(resultsHtmlKey, 6000, results);
            } else {
                results = HtmlCache.getFromCache(resultsHtmlKey);
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