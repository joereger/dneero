package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.survey.servlet.SurveyFlashServlet;
import com.dneero.survey.servlet.SurveyJavascriptServlet;
import com.dneero.survey.servlet.SurveyImageServlet;
import com.dneero.survey.servlet.SurveyLinkServlet;
import com.dneero.helpers.UserInputSafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail03 implements Serializable {


    private String templateFromDb;
    private String template;
    private String embedjavascriptsyntax;
    private String embedflashsyntax;
    private String embedimagesyntax;
    private String embedlinksyntax;
    private String templateAutoGenerated;
    private int status;
    private String title;
    private boolean embedjavascript = true;
    private boolean embedflash = true;
    private boolean embedlink = true;
    private ArrayList questions = new ArrayList();
    private Survey survey;

    public ResearcherSurveyDetail03(){

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
                templateFromDb = survey.getTemplate();
                template = SurveyTemplateProcessor.appendExtraQuestionsIfNecessary(survey, survey.getTemplate());
                templateAutoGenerated = SurveyTemplateProcessor.getAutoGeneratedTemplateForSurvey(survey);
                if (template.equals("")){
                   template = templateAutoGenerated;
                }
                embedjavascript = survey.getEmbedjavascript();
                embedflash = survey.getEmbedflash();
                embedlink = survey.getEmbedlink();
                embedjavascriptsyntax = SurveyJavascriptServlet.getEmbedSyntax("/", survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), 0, true, true, true, true);
                embedflashsyntax = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), 0, true, false, true);
                embedimagesyntax = SurveyImageServlet.getEmbedSyntax("/", survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), 0, true);
                embedlinksyntax = SurveyLinkServlet.getEmbedSyntax("/", survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), 0, true);
                status = survey.getStatus();
                questions=new ArrayList();
                if (survey!=null && survey.getQuestions()!=null){
                    for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        questions.add(question);
                    }
                }
            }
        }
    }

    public String saveSurveyAsDraft(){
        Pagez.sendRedirect("/researcher/index.jsp");
        return "";
    }

    public String previousStep(){
        Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp");
        return "";
    }

    public String resetFormatting() throws ValidationException {
        template = "";
        return saveSurvey();
    }

    public String saveSurvey() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();

            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                if (!template.equals(templateAutoGenerated) && !template.equals(templateFromDb)){
                    survey.setTemplate(UserInputSafe.cleanJavascript(SurveyTemplateProcessor.appendExtraQuestionsIfNecessary(survey, template)));
                }
                if (!embedjavascript && !embedflash && !embedlink){
                    embedjavascript = true;
                }
                survey.setEmbedjavascript(embedjavascript);
                survey.setEmbedflash(embedflash);
                survey.setEmbedlink(embedlink);
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

                initBean();
 
                //Refresh
                survey.refresh();
            }
        }
        Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp");
        return "";
    }

    public String continueToNext() throws ValidationException {
        if(saveSurvey()!=null){
            Pagez.sendRedirect("/researcher/researchersurveydetail_04.jsp");
            return "";
        }
        return null;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getEmbedjavascriptsyntax() {
        return embedjavascriptsyntax;
    }

    public void setEmbedjavascriptsyntax(String embedjavascriptsyntax) {
        this.embedjavascriptsyntax = embedjavascriptsyntax;
    }


    public String getEmbedlinksyntax() {
        return embedlinksyntax;
    }

    public void setEmbedlinksyntax(String embedlinksyntax) {
        this.embedlinksyntax = embedlinksyntax;
    }

    public String getEmbedflashsyntax() {
        return embedflashsyntax;
    }

    public void setEmbedflashsyntax(String embedflashsyntax) {
        this.embedflashsyntax = embedflashsyntax;
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


    public boolean getEmbedjavascript() {
        return embedjavascript;
    }

    public void setEmbedjavascript(boolean embedjavascript) {
        this.embedjavascript = embedjavascript;
    }

    public boolean getEmbedflash() {
        return embedflash;
    }

    public void setEmbedflash(boolean embedflash) {
        this.embedflash = embedflash;
    }

    public boolean getEmbedlink() {
        return embedlink;
    }

    public void setEmbedlink(boolean embedlink) {
        this.embedlink = embedlink;
    }


    public ArrayList getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList questions) {
        this.questions = questions;
    }

    public String getEmbedimagesyntax() {
        return embedimagesyntax;
    }

    public void setEmbedimagesyntax(String embedimagesyntax) {
        this.embedimagesyntax = embedimagesyntax;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }
}
