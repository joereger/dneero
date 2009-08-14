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
import com.dneero.display.components.Checkboxes;
import com.dneero.helpers.UserInputSafe;
import com.dneero.helpers.QuestionOrder;
import com.dneero.survey.servlet.EmbedCacheFlusher;

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
    private Survey survey;
    private String image;
    private String audio;
    private String video;



    public ResearcherSurveyDetail02checkboxes(){

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
                    if (questionconfig.getName().equals("options")){
                        this.options = questionconfig.getValue();
                    } else if (questionconfig.getName().equals("image")){
                        this.image = questionconfig.getValue();
                    } else if (questionconfig.getName().equals("audio")){
                        this.audio = questionconfig.getValue();
                    } else if (questionconfig.getName().equals("video")){
                        this.video = questionconfig.getValue();
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

            //Validate that at most one image/audio/video is set
            int iavCount = 0;
            if (image!=null && image.length()>0){iavCount++;}
            if (audio!=null && audio.length()>0){iavCount++;}
            if (video!=null && video.length()>0){iavCount++;}
            if (iavCount>1){ throw new ValidationException("Sorry, you can set at most one Image/Audio/Video URL"); }

            Question question = new Question();
            if (questionid>0){
                question = Question.get(questionid);
                logger.debug("questionid = "+questionid);
            }

            question.setSurveyid(survey.getSurveyid());
            question.setQuestion(this.question);
            question.setIsrequired(isrequired);
            question.setComponenttype(Checkboxes.ID);
            question.setIsuserquestion(false);
            question.setUserid(0);
            question.setIsresearcherreviewed(true);
            question.setIssysadminreviewed(false);
            question.setIsresearcherrejected(false);
            question.setIssysadminrejected(false);
            question.setScorebyresearcher(0);
            question.setScorebysysadmin(0);
            question.setQuestionorder(QuestionOrder.calculateNewQuestionOrder(question));

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
            qc1.setName("options");
            qc1.setValue(options);
            question.getQuestionconfigs().add(qc1);

            if (image!=null && image.length()>0){
                Questionconfig qc = new Questionconfig();
                qc.setQuestionid(question.getQuestionid());
                qc.setName("image");
                qc.setValue(image);
                question.getQuestionconfigs().add(qc);
            }
            if (audio!=null && audio.length()>0){
                Questionconfig qc = new Questionconfig();
                qc.setQuestionid(question.getQuestionid());
                qc.setName("audio");
                qc.setValue(audio);
                question.getQuestionconfigs().add(qc);
            }
            if (video!=null && video.length()>0){
                Questionconfig qc = new Questionconfig();
                qc.setQuestionid(question.getQuestionid());
                qc.setName("video");
                qc.setValue(video);
                question.getQuestionconfigs().add(qc);
            }

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

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image=image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio=audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video=video;
    }
}
