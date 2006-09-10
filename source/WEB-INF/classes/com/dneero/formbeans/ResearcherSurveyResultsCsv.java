package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;

import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyResultsCsv {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private String results;

    public ResearcherSurveyResultsCsv(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            results = "";
            StringBuffer out = new StringBuffer();
            for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                Question question = iterator.next();
                logger.debug("found question.getQuestionid()="+question.getQuestionid());
                Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, new Blogger());
                out.append(component.getName());
                out.append(",");
                for (int i = 0; i < component.getCsvForResult().length; i++) {
                    String s = component.getCsvForResult()[i];
                    out.append(s);
                    out.append(",");
                }
                out.append("\n");


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
