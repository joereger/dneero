package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.xmpp.SendXMPPMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyTake {

    private Survey survey;
    private String html;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public PublicSurveyTake(){
        logger.debug("PublicSurveyTake instanciated.");
        survey = new Survey();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            survey = Survey.get(Integer.parseInt(Jsf.getRequestParam("surveyid")));
            html = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger());
        }
    }


    public String takeSurvey(){
        logger.debug("takeSurvey() called");
        try{
            SurveyResponseParser srp = new SurveyResponseParser((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
            createResponse(survey, srp, Jsf.getUserSession().getUser().getBlogger());
        } catch (ComponentException cex){
            Jsf.setFacesMessage(cex.getErrorsAsSingleString());
            return "publicsurveytake";
        }
        return "publicsurveytakefinished";
    }


    public static void createResponse(Survey survey, SurveyResponseParser srp, Blogger blogger) throws ComponentException{
        Logger logger = Logger.getLogger(PublicSurveyTake.class);
        ComponentException allCex = new ComponentException();

        //Make sure each component is validated
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
            logger.debug("found component.getName()="+component.getName());
            try{
                component.validateAnswer(srp);
            } catch (ComponentException cex){
                logger.debug(cex);
                allCex.addErrorsFromAnotherGeneralException(cex);
            }
        }
        //If we are validated
        if (allCex.getErrors().length<=0){
            if (Jsf.getUserSession()!=null && !Jsf.getUserSession().getIsloggedin()){
                //Not logged-in... store this response in memory for now
                Jsf.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Jsf.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing survey response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){
            throw allCex;
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
