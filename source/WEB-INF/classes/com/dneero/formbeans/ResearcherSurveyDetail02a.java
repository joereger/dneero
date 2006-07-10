package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.session.UserSession;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02a {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int questionid;
    private String question;
    private boolean isrequired=true;
    private int componenttype;



    public ResearcherSurveyDetail02a(){
        logger.debug("Instanciating object.");
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpQuestionid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionid");
        if (com.dneero.util.Num.isinteger(tmpQuestionid)){
            logger.debug("beginView called: found questionid in param="+tmpQuestionid);
            loadQuestion(Integer.parseInt(tmpQuestionid));
        }
        return "researchersurveydetail_02a";
    }

    public void loadQuestion(int questionid){
        logger.debug("loadQuestion called");
        Question question = Question.get(questionid);
        if (question!=null){
            logger.debug("Found question in db: question.getQuestionid()="+question.getQuestionid()+" question.getQuestion()="+question.getQuestion());
        }
        this.questionid = question.getQuestionid();
        this.question = question.getQuestion();
        this.isrequired = question.getIsrequired();
        this.componenttype = question.getComponenttype();
    }

    public String saveQuestion(){
        logger.debug("saveQuestion() called.");

        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentResearcherSurveyDetailSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentResearcherSurveyDetailSurveyid());
            survey = Survey.get(userSession.getCurrentResearcherSurveyDetailSurveyid());
        }

        Question question = new Question();
        if (questionid>0){
            question = Question.get(questionid);
            logger.debug("questionid = "+questionid);
        }

        question.setSurveyid(survey.getSurveyid());
        question.setQuestion(this.question);
        question.setIsrequired(isrequired);
        question.setComponenttype(1);

        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question1 = iterator.next();
            if (question1.getQuestionid()==questionid){
                iterator.remove();
            }
        }

        survey.getQuestions().add(question);

        try{
            logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
        } catch (GeneralException gex){
            logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
            String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        //Refresh
        survey.refresh();

        return "success";
    }


    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isIsrequired() {
        return isrequired;
    }

    public void setIsrequired(boolean isrequired) {
        this.isrequired = isrequired;
    }

    public int getComponenttype() {
        return componenttype;
    }

    public void setComponenttype(int componenttype) {
        this.componenttype = componenttype;
    }


}
