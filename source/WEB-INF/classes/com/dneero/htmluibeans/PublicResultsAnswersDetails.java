package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import com.dneero.util.Util;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class PublicResultsAnswersDetails implements Serializable {

    private Survey survey;
    private String results;

    public PublicResultsAnswersDetails(){

    }



    public void initBean(){
        loadQuestion(Pagez.getUserSession().getCurrentSurveyid());
    }

    public void loadQuestion(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        survey = Survey.get(surveyid);
        if (survey!=null){
            String tmpQuestionid = Pagez.getRequest().getParameter("questionid");
            if (com.dneero.util.Num.isinteger(tmpQuestionid)){
                logger.debug("beginView called: found tmpQuestionid in request param="+tmpQuestionid);
                Question question = Question.get(Integer.parseInt(tmpQuestionid));
                Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, new Blogger());
                try{
                    results = "<font class=\"mediumfont\">Answers to the question: "+question.getQuestion()+"</font><br/><br/>"+component.getHtmlForResultDetail(Util.setToArrayList(question.getQuestionresponses()));
                } catch (Exception ex){
                    logger.error("", ex);
                    results = "";
                }
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
