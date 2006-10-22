package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.dao.Questionconfig;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.display.components.Textbox;
import com.dneero.display.components.Matrix;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02matrix {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int questionid = 0;
    private String question;
    private String rows;
    private String cols;
    private boolean respondentcanselectmany=false;
    private boolean isrequired=true;
    private int componenttype;



    public ResearcherSurveyDetail02matrix(){
        logger.debug("Instanciating object");
        String tmpQuestionid = Jsf.getRequestParam("questionid");
        String tmpIsnewquestion = Jsf.getRequestParam("isnewquestion");
        if (questionid==0 && com.dneero.util.Num.isinteger(tmpQuestionid) && (tmpIsnewquestion==null || !tmpIsnewquestion.equals("1"))){
            logger.debug("constructor: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
            loadQuestion();
        }
        
    }


    public void loadQuestion(){
        logger.debug("loadQuestion called questionid="+questionid);
        Question question = Question.get(questionid);
        if (question!=null){
            logger.debug("Found question in db: question.getQuestionid()="+question.getQuestionid()+" question.getQuestion()="+question.getQuestion());
            if (question.canEdit(Jsf.getUserSession().getUser())){
                this.questionid = question.getQuestionid();
                this.question = question.getQuestion();
                this.isrequired = question.getIsrequired();
                this.componenttype = question.getComponenttype();

                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("rows")){
                        this.rows = questionconfig.getValue();
                    }
                    if (questionconfig.getName().equals("cols")){
                        this.cols = questionconfig.getValue();
                    }
                    if (questionconfig.getName().equals("respondentcanselectmany")){
                        this.respondentcanselectmany = false;
                        if (questionconfig.getValue().equals("1")){
                            this.respondentcanselectmany = true;
                        }
                    }
                }
            }
        }
    }

    public String saveQuestion(){
        logger.debug("saveQuestion() called. - questionid="+questionid);

        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
        }

        if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){

            Question question = new Question();
            if (questionid>0){
                question = Question.get(questionid);
                logger.debug("questionid = "+questionid);
            }

            question.setSurveyid(survey.getSurveyid());
            question.setQuestion(this.question);
            question.setIsrequired(isrequired);
            question.setComponenttype(Matrix.ID);

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
            qc1.setName("rows");
            qc1.setValue(rows);
            question.getQuestionconfigs().add(qc1);

            Questionconfig qc2 = new Questionconfig();
            qc2.setQuestionid(question.getQuestionid());
            qc2.setName("cols");
            qc2.setValue(cols);
            question.getQuestionconfigs().add(qc2);

            Questionconfig qc3 = new Questionconfig();
            qc3.setQuestionid(question.getQuestionid());
            qc3.setName("respondentcanselectmany");
            if (respondentcanselectmany){
                qc3.setValue("1");
            } else {
                qc3.setValue("0");
            }
            question.getQuestionconfigs().add(qc3);

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

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public boolean getRespondentcanselectmany() {
        return respondentcanselectmany;
    }

    public void setRespondentcanselectmany(boolean respondentcanselectmany) {
        this.respondentcanselectmany = respondentcanselectmany;
    }
}
