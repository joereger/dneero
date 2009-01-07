package com.dneero.htmluibeans;

import org.apache.log4j.Logger;


import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.dao.Questionconfig;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.display.components.Range;
import com.dneero.helpers.UserInputSafe;
import com.dneero.survey.servlet.EmbedCacheFlusher;

import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02range implements Serializable {

    private String title;

    private int questionid = 0;
    private String question;
    private boolean isrequired=true;
    private int componenttype;
    private Survey survey;

    private String mintitle = "Low";
    private double min = 1;
    private double step = 1;
    private double max = 5;
    private String maxtitle = "High";



    public ResearcherSurveyDetail02range(){

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
                    if (questionconfig.getName().equals("mintitle")){
                        this.mintitle = questionconfig.getValue();
                    } else if (questionconfig.getName().equals("min")){
                        this.min = Double.parseDouble(questionconfig.getValue());
                    } else if (questionconfig.getName().equals("step")){
                        this.step = Double.parseDouble(questionconfig.getValue());
                    } else if (questionconfig.getName().equals("max")){
                        this.max = Double.parseDouble(questionconfig.getValue());
                    } else if (questionconfig.getName().equals("maxtitle")){
                        this.maxtitle = questionconfig.getValue();
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

            Question question = new Question();
            if (questionid>0){
                question = Question.get(questionid);
                logger.debug("questionid = "+questionid);
            }

            question.setSurveyid(survey.getSurveyid());
            question.setQuestion(this.question);
            question.setIsrequired(isrequired);
            question.setComponenttype(Range.ID);
            question.setIsuserquestion(false);
            question.setUserid(0);
            question.setIsresearcherreviewed(true);
            question.setIssysadminreviewed(false);
            question.setIsresearcherrejected(false);
            question.setIssysadminrejected(false);
            question.setScorebyresearcher(0);
            question.setScorebysysadmin(0);


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
            qc1.setName("mintitle");
            qc1.setValue(mintitle);
            question.getQuestionconfigs().add(qc1);

            Questionconfig qc2 = new Questionconfig();
            qc2.setQuestionid(question.getQuestionid());
            qc2.setName("min");
            qc2.setValue(String.valueOf(min));
            question.getQuestionconfigs().add(qc2);

            Questionconfig qc3 = new Questionconfig();
            qc3.setQuestionid(question.getQuestionid());
            qc3.setName("step");
            qc3.setValue(String.valueOf(step));
            question.getQuestionconfigs().add(qc3);

            Questionconfig qc4 = new Questionconfig();
            qc4.setQuestionid(question.getQuestionid());
            qc4.setName("max");
            qc4.setValue(String.valueOf(max));
            question.getQuestionconfigs().add(qc4);

            Questionconfig qc5 = new Questionconfig();
            qc5.setQuestionid(question.getQuestionid());
            qc5.setName("maxtitle");
            qc5.setValue(maxtitle);
            question.getQuestionconfigs().add(qc5);

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

    public String getMintitle() {
        return mintitle;
    }

    public void setMintitle(String mintitle) {
        this.mintitle = mintitle;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMin(String min) {
        if (Num.isdouble(min)){
            this.min = Double.parseDouble(min);
        }
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setStep(String step) {
        if (Num.isdouble(step)){
            this.step = Double.parseDouble(step);
        }
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMax(String max) {
        if (Num.isdouble(max)){
            this.max = Double.parseDouble(max);
        }
    }

    public String getMaxtitle() {
        return maxtitle;
    }

    public void setMaxtitle(String maxtitle) {
        this.maxtitle = maxtitle;
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
}
