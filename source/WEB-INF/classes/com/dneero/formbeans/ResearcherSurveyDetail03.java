package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.session.UserSession;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.display.SurveyTakerDisplay;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail03 {


    Logger logger = Logger.getLogger(this.getClass().getName());

    private String templateFromDb;
    private String template;
    private String surveyForTakers;
    private String surveyForReaders;
    private String templateAutoGenerated;
    private int status;
    private String title;

    public ResearcherSurveyDetail03(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                templateFromDb = survey.getTemplate();
                template = SurveyTemplateProcessor.appendExtraQuestionsIfNecessary(survey, survey.getTemplate());
                templateAutoGenerated = SurveyTemplateProcessor.getAutoGeneratedTemplateForSurvey(survey);
                if (template.equals("")){
                   template = templateAutoGenerated;
                }
                //surveyForReaders = SurveyAsHtml.getHtml(survey, Jsf.getUserSession().getUser());
                //Have to now remove the \\' that the javascript version uses for " (double quotes)
                //surveyForReaders = surveyForReaders.replaceAll("\\\\'", "'");

                String url = "<script src=\"/s?s="+survey.getSurveyid()+"&u="+Jsf.getUserSession().getUser().getUserid()+"&ispreview=1\"></script>";
                surveyForReaders = url;


                surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger());
                status = survey.getStatus();
            }
        }
    }

    public String saveSurveyAsDraft(){
        return "researchersurveylist";
    }

    public String previousStep(){
        return "researchersurveydetail_02";
    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_WAITINGFORSTARTDATE){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                if (!template.equals(templateAutoGenerated) && !template.equals(templateFromDb)){
                    survey.setTemplate(SurveyTemplateProcessor.appendExtraQuestionsIfNecessary(survey, template));

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

                    loadSurvey(survey.getSurveyid());
                }
 
                //Refresh
                survey.refresh();
            }
        }
        return "researchersurveydetail_03";
    }

    public String continueToNext(){
        return "success";
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSurveyForReaders() {
        return surveyForReaders;
    }

    public void setSurveyForReaders(String surveyForReaders) {
        this.surveyForReaders = surveyForReaders;
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
}
