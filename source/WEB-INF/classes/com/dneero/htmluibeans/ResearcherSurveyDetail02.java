package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.display.components.*;
import com.dneero.display.SurveyTakerDisplay;

import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02 implements Serializable {

    private int newquestioncomponenttype = Textbox.ID;
    private String surveyForTakers;
    private int status;
    private String title;
    private Survey survey;


    public ResearcherSurveyDetail02(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
                status = survey.getStatus();
            }
        }
    }

    public String saveSurveyAsDraft() throws ValidationException {
        String save = saveSurvey();
        if (save!=null){
            Pagez.sendRedirect("/researcher/index.jsp");
            return "";
        } else {
            return save;
        }
    }

    public String previousStep() throws ValidationException {
        String save = saveSurvey();
        if (save!=null){
            Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp");
            return "";
        } else {
            return save;
        }
    }

    public String saveSurvey() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }

            if (survey.getQuestions()==null || survey.getQuestions().size()==0){
                Pagez.getUserSession().setMessage("You must add at least one question to continue.");
                return null;   
            }

            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                //survey.setResearcherid(userSession.getUser().getResearcherid());
                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    Pagez.getUserSession().setMessage(message);
                    return null;
                }

                //Refresh
                survey.refresh();
                initBean();
            }
        }

        Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp");
        return "";
    }





    public String deleteQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String tmpQuestionid = Pagez.getRequest().getParameter("questionid");
        int questionid = 0;
        if (com.dneero.util.Num.isinteger(tmpQuestionid)){
            logger.debug("deleteQuestion called: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
        }

        UserSession userSession = Pagez.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
        }

        //survey.setResearcherid(userSession.getUser().getResearcherid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question1 = iterator.next();
            if (question1.getQuestionid()==questionid){
                iterator.remove();
            }
        }

        //Have to delete the question tag from the template
        logger.debug("About to update template to remove deleted question from the template. Looking for <$question_"+questionid+"$>");
        if (survey.getTemplate()!=null && survey.getTemplate().indexOf("<$question_"+questionid+"$>")>-1){
            logger.debug("found an instance of <$question_"+questionid+"$>");
            String template = survey.getTemplate();
            template = template.replaceAll("<p><\\$question_"+questionid+"\\$></p>", "");
            template = template.replaceAll("<\\$question_"+questionid+"\\$>", "");
            logger.debug(template);
            survey.setTemplate(template);
        } else {
            if (survey.getTemplate()==null){
                logger.debug("none found, survey.getTemplate() is null");   
            } else {
                logger.debug("none found, survey.getTemplate().indexOf(\"<$question_"+questionid+"$>\")="+survey.getTemplate().indexOf("<$question_"+questionid+"$>"));
            }
        }

        try{
            logger.debug("deleteQuestion() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("deleteQuestion() done saving survey.getSurveyid()=" + survey.getSurveyid());
        } catch (GeneralException gex){
            logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
            String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
            Pagez.getUserSession().setMessage(message);
            return null;
        }

        //Refresh
        survey.refresh();

        Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp");
        return "";
    }

    public int getNewquestioncomponenttype() {
        return newquestioncomponenttype;
    }

    public void setNewquestioncomponenttype(int newquestioncomponenttype) {
        this.newquestioncomponenttype = newquestioncomponenttype;
    }

    public String getSurveyForTakers() {
        return surveyForTakers;
    }

    public void setSurveyForTakers(String surveyForTakers) {
        this.surveyForTakers = surveyForTakers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
