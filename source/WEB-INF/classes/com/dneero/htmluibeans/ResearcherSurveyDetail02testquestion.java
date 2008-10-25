package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Questionconfig;
import com.dneero.util.Num;
import com.dneero.util.GeneralException;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.helpers.UserInputSafe;
import com.dneero.display.components.TestQuestion;
import com.dneero.survey.servlet.EmbedCacheFlusher;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02testquestion implements Serializable {

    private int questionid = 0;
    private String question;
    private boolean isrequired=true;
    private int componenttype;
    private String title;
    private Survey survey;
    private String answermustcontain;



    public ResearcherSurveyDetail02testquestion(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciating object");
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        String tmpQuestionid = Pagez.getRequest().getParameter("questionid");
        String tmpIsnewquestion = Pagez.getRequest().getParameter("isnewquestion");
        if (questionid==0 && Num.isinteger(tmpQuestionid) && (tmpIsnewquestion==null || !tmpIsnewquestion.equals("1"))){
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
                    if (questionconfig.getName().equals("answermustcontain")){
                        this.answermustcontain = questionconfig.getValue();
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

    public void saveQuestion() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveQuestion() called. - questionid="+questionid);

        if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

            if (this.question!=null  && this.question.length()>1000){
                throw new ValidationException("The Question is too long.  Please choose a shorter one.");
            }

            if (this.answermustcontain==null || this.answermustcontain.length()<=0){
                throw new ValidationException("The Answer is required.");
            }

            Question question = new Question();
            if (questionid>0){
                question = Question.get(questionid);
                logger.debug("questionid = "+questionid);
            }

            question.setSurveyid(survey.getSurveyid());
            question.setQuestion(this.question);
            question.setIsrequired(isrequired);
            question.setComponenttype(TestQuestion.ID);
            question.setIsuserquestion(false);
            question.setUserid(0);
            question.setIsresearcherreviewed(true);
            question.setIssysadminreviewed(false);
            question.setIsresearcherrejected(false);
            question.setIssysadminrejected(false);

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
                EmbedCacheFlusher.flushCache(survey.getSurveyid());
                logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
            } catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                Pagez.getUserSession().setMessage(message);
                return;
            }

            for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                Questionconfig questionconfig = iterator.next();
                iterator.remove();
            }

            Questionconfig qc1 = new Questionconfig();
            qc1.setQuestionid(question.getQuestionid());
            qc1.setName("answermustcontain");
            qc1.setValue(answermustcontain);
            question.getQuestionconfigs().add(qc1);

            try{
                logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                survey.save();
                EmbedCacheFlusher.flushCache(survey.getSurveyid());
                logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
            } catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                Pagez.getUserSession().setMessage(message);
                return;
            }

            //Refresh
            survey.refresh();
        }

        return;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public String getAnswermustcontain() {
        return answermustcontain;
    }

    public void setAnswermustcontain(String answermustcontain) {
        this.answermustcontain=answermustcontain;
    }
}
