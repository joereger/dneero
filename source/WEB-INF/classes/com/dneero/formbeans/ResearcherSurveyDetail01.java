package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.session.UserSession;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;

import oracle.adf.view.faces.model.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail01 {


    private String title;
    private String description;
    private Date startdate;
    private Date enddate;
    private String template;



    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail01(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentResearcherSurveyDetailSurveyid());
    }

    public String beginViewNewSurvey(){
        Jsf.getUserSession().setCurrentResearcherSurveyDetailSurveyid(0);
        return "researchersurveydetail_01";
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            UserSession userSession = Jsf.getUserSession();
            userSession.setCurrentResearcherSurveyDetailSurveyid(Integer.parseInt(tmpSurveyid));
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydetail_01";
    }

    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());

            title = survey.getTitle();
            description = survey.getDescription();
            startdate = survey.getStartdate();
            enddate = survey.getEnddate();
            template = survey.getTemplate();
        }

    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");

        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentResearcherSurveyDetailSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentResearcherSurveyDetailSurveyid());
            survey = Survey.get(userSession.getCurrentResearcherSurveyDetailSurveyid());
        }

        survey.setResearcherid(userSession.getUser().getResearcher().getResearcherid());
        survey.setTitle(title);
        survey.setDescription(description);
        survey.setStartdate(startdate);
        survey.setEnddate(enddate);
        survey.setTemplate(template);

        try{
            logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            userSession.setCurrentResearcherSurveyDetailSurveyid(survey.getSurveyid());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
