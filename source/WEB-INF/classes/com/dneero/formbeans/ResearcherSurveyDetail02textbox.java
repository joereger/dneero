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
import com.dneero.display.components.Textbox;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02textbox {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int questionid = 0;
    private String question;
    private boolean isrequired=true;
    private int componenttype;



    public ResearcherSurveyDetail02textbox(){
        logger.debug("Instanciating object");
        String tmpQuestionid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionid");
        String tmpIsnewquestion = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isnewquestion");
        if (questionid==0 && com.dneero.util.Num.isinteger(tmpQuestionid) && (tmpIsnewquestion==null || !tmpIsnewquestion.equals("1"))){
            logger.debug("constructor: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
            loadQuestion();
        }
    }

//    public String beginView(){
//        //logger.debug("beginView called:");
//        String tmpQuestionid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionid");
//        if (questionid==0 && com.dneero.util.Num.isinteger(tmpQuestionid)){
//            logger.debug("beginView called: found questionid in param="+tmpQuestionid);
//            questionid = Integer.parseInt(tmpQuestionid);
//            loadQuestion();
//        }
//        return "researchersurveydetail_02_textbox";
//    }

    public void loadQuestion(){
        logger.debug("loadQuestion called questionid="+questionid);
        Question question = Question.get(questionid);
        if (question!=null){
            logger.debug("Found question in db: question.getQuestionid()="+question.getQuestionid()+" question.getQuestion()="+question.getQuestion());
            this.questionid = question.getQuestionid();
            this.question = question.getQuestion();
            this.isrequired = question.getIsrequired();
            this.componenttype = question.getComponenttype();
        }
    }

    public String saveQuestion(){
        logger.debug("saveQuestion() called. - questionid="+questionid);

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
        question.setComponenttype(Textbox.ID);

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

        return "researchersurveydetail_02";
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
