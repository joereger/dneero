package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.session.UserSession;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyResultsQuestiondetail {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private String results;

    public ResearcherSurveyResultsQuestiondetail(){
        logger.debug("Instanciating object.");
        loadQuestion(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadQuestion(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            if (survey.canEdit(Jsf.getUserSession().getUser())){
                String tmpQuestionid = Jsf.getRequestParam("questionid");
                if (com.dneero.util.Num.isinteger(tmpQuestionid)){
                    logger.debug("beginView called: found tmpQuestionid in request param="+tmpQuestionid);
                    Question question = Question.get(Integer.parseInt(tmpQuestionid));
                    Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, new Blogger());
                    results = component.getHtmlForResultDetail();
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
