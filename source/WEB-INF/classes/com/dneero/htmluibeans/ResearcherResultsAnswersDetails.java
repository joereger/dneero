package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.util.Jsf;
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
public class ResearcherResultsAnswersDetails implements Serializable {

    private Survey survey;
    private String results;
    private Question question;

    public ResearcherResultsAnswersDetails(){

    }




    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                String tmpQuestionid = Pagez.getRequest().getParameter("questionid");
                if (com.dneero.util.Num.isinteger(tmpQuestionid)){
                    logger.debug("beginView called: found tmpQuestionid in request param="+tmpQuestionid);
                    question = Question.get(Integer.parseInt(tmpQuestionid));
                    Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, new Blogger());
                    results = component.getHtmlForResultDetail(Util.setToArrayList(question.getQuestionresponses()));
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


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
