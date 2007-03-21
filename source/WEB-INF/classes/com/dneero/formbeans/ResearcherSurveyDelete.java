package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;

import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.session.UserSession;
import com.dneero.dao.Survey;
import com.dneero.xmpp.SendXMPPMessage;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDelete implements Serializable {

    private String title;
    private String description;
    private Date startdate;
    private Date enddate;
    private int status;

    public ResearcherSurveyDelete(){

    }


    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called:");
        load();
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            UserSession userSession = Jsf.getUserSession();
            userSession.setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydelete";
    }

    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called for surveyid="+surveyid);
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                logger.debug("survey.canEdit(Jsf.getUserSession().getUser())="+survey.canEdit(Jsf.getUserSession().getUser()));
                title = survey.getTitle();
                description = survey.getDescription();
                startdate = survey.getStartdate();
                enddate = survey.getEnddate();
                status = survey.getStatus();
            }
        }

    }

    public String deleteSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        UserSession userSession = Jsf.getUserSession();
        logger.debug("deleteSurvey() called. status="+status + " userSession.getCurrentSurveyid()="+userSession.getCurrentSurveyid());
        if (status==Survey.STATUS_DRAFT){
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("deleteSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                Survey survey = Survey.get(userSession.getCurrentSurveyid());
                try{
                    logger.debug("deleteSurvey() about to delete survey.getSurveyid()=" + survey.getSurveyid());
                    survey.delete();
                    userSession.setCurrentSurveyid(survey.getSurveyid());
                    logger.debug("deleteSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (Exception gex){
                    logger.error(gex);
                    String message = "deleteSurvey() failed: " + gex.getMessage();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            } else {
                logger.debug("Not deleting because userSession.getCurrentSurveyid() is not less than zero");
            }
        } else {
            logger.debug("Not deleting because status!=Survey.STATUS_DRAFT");
        }
        return "researchersurveylist";
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
