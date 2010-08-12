package com.dneero.htmluibeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.survey.servlet.SurveyDisplayCacheFlusher;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Aug 11, 2010
 * Time: 10:17:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PublicSurveySingleUserquestion implements Serializable {

    private Survey survey;
    private Question question;
    private boolean loggedinuserhasalreadytakensurvey;
    private Response response;
    private Blogger blogger;

    public PublicSurveySingleUserquestion(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        } else if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            String[] split = Pagez.getRequest().getParameter("action").split("-");
            if (split.length>=3){
                if (split[1]!=null && Num.isinteger(split[1])){
                    surveyid = Integer.parseInt(split[1]);
                }
            }
        }

        //Set the currentsurveyid
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //Load up the survey
        survey = Survey.get(surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0 || survey==null || survey.getTitle()==null){
            return;
        }

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            return;
        }

        //Get the question to answer
        if (Num.isinteger(Pagez.getRequest().getParameter("questionid"))){
            question = Question.get(Integer.parseInt(Pagez.getRequest().getParameter("questionid")));
        }

        
        //See if logged in user has taken survey yet
        loggedinuserhasalreadytakensurvey = false;
        this.response = null;
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    loggedinuserhasalreadytakensurvey = true;
                    this.response = response;
                }
            }
        }


    }

    public void answerQuestion() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        ComponentException allCex = new ComponentException();
        SurveyResponseParser srp = new SurveyResponseParser(Pagez.getRequest());
        logger.debug("start processing questionid="+question.getQuestionid()+" "+question.getQuestion());
        //Delete any existing question responses
        HibernateUtil.getSession().createQuery("delete Questionresponse q where q.questionid='"+question.getQuestionid()+"' and q.bloggerid='"+blogger.getBloggerid()+"' and q.responseid='"+response.getResponseid()+"'").executeUpdate();                
        Component component = ComponentTypes.getComponentByType(question.getComponenttype(), question,  blogger);
        try{component.processAnswer(srp, response);} catch (ComponentException cex){allCex.addErrorsFromAnotherGeneralException(cex);}
        logger.debug("end processing questionid="+question.getQuestionid()+" "+question.getQuestion());
        if (allCex.getErrors()!=null && allCex.getErrors().length>0){
            ValidationException vex = new ValidationException();
            vex.addValidationError(allCex.getErrorsAsSingleString());
            throw vex;
        }  
        SurveyDisplayCacheFlusher.flush(survey.getSurveyid());
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean getLoggedinuserhasalreadytakensurvey() {
        return loggedinuserhasalreadytakensurvey;
    }

    public void setLoggedinuserhasalreadytakensurvey(boolean loggedinuserhasalreadytakensurvey) {
        this.loggedinuserhasalreadytakensurvey = loggedinuserhasalreadytakensurvey;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
