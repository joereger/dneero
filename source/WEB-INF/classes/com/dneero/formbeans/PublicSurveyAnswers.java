package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyAnswers {

    private Survey survey;
    private String html;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public PublicSurveyAnswers(){
        logger.debug("PublicSurveyAnswers instanciated.");
        survey = new Survey();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
        }
        if(Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            html = SurveyResultsDisplay.getHtmlForResults(survey, null);
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }




    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
