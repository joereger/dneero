package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.dao.Questionconfig;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;

import com.dneero.util.GeneralException;
import com.dneero.display.components.Checkboxes;
import com.dneero.helpers.UserInputSafe;

import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02checkboxes implements Serializable {


    private int questionid = 0;
    private String question;
    private boolean isrequired=true;
    private int componenttype;
    private String options = "";
    private String title;



    public ResearcherSurveyDetail02checkboxes(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciating object");
        String tmpQuestionid = Pagez.getRequest().getParameter("questionid");
        String tmpIsnewquestion = Pagez.getRequest().getParameter("isnewquestion");
        if (questionid==0 && com.dneero.util.Num.isinteger(tmpQuestionid) && (tmpIsnewquestion==null || !tmpIsnewquestion.equals("1"))){
            logger.debug("constructor: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
            loadQuestion();
        }
    }


    public void loadQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadQuestion called questionid="+questionid);
        Question question = Question.get(questionid);
        if (question!=null){
            logger.debug("Found question in db: question.getQuestionid()="+question.getQuestionid()+" question.getQuestion()="+question.getQuestion());
            if (question.canEdit(Pagez.getUserSession().getUser())){
                this.questionid = question.getQuestionid();
                this.question = question.getQuestion();
                this.isrequired = question.getIsrequired();
                this.componenttype = question.getComponenttype();

                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("options")){
                        this.options = questionconfig.getValue();
                    }
                }
            }
        }
        Survey survey = new Survey();
        if (Pagez.getUserSession().getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+Pagez.getUserSession().getCurrentSurveyid());
            survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
            title = survey.getTitle();
        }
    }

    public String saveQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveQuestion() called. - questionid="+questionid);

        UserSession userSession = Pagez.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
        }

        if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

            Question question = new Question();
            if (questionid>0){
                question = Question.get(questionid);
                logger.debug("questionid = "+questionid);
            }

            question.setSurveyid(survey.getSurveyid());
            question.setQuestion(UserInputSafe.clean(this.question));
            question.setIsrequired(isrequired);
            question.setComponenttype(Checkboxes.ID);

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

            for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                Questionconfig questionconfig = iterator.next();
                iterator.remove();
            }

            Questionconfig qc1 = new Questionconfig();
            qc1.setQuestionid(question.getQuestionid());
            qc1.setName("options");
            qc1.setValue(UserInputSafe.clean(options));
            question.getQuestionconfigs().add(qc1);

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
        }
        Pagez.sendRedirect("/jsp/researcher/researchersurveydetail_02.jsp");
        return "";
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

    public boolean getIsrequired() {
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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}