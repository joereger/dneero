package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;

import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
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




    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Survey survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("survey.canEdit(Pagez.getUserSession().getUser())="+survey.canEdit(Pagez.getUserSession().getUser()));
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
        UserSession userSession = Pagez.getUserSession();
        logger.debug("deleteSurvey() called. status="+status + " userSession.getCurrentSurveyid()="+userSession.getCurrentSurveyid());
        if (status==Survey.STATUS_DRAFT){
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("deleteSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                Survey survey = Survey.get(userSession.getCurrentSurveyid());
                survey.refresh();
                try{
                    logger.debug("deleteSurvey() about to delete survey.getSurveyid()=" + survey.getSurveyid());
                    survey.delete();
                    Jsf.setFacesMessage("Survey deleted.");
                    userSession.setCurrentSurveyid(0);
                    logger.debug("deleteSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (Exception gex){
                    logger.error(gex);
                    String message = "deleteSurvey() failed: " + gex.getMessage();
                    Jsf.setFacesMessage("Sorry, there was an error. Please click the Delete button again.");
                    return null;
                }
            } else {
                logger.debug("Not deleting because userSession.getCurrentSurveyid() is not less than zero");
            }
        } else {
            Jsf.setFacesMessage("Survey could not be deleted because it is not in draft mode.");
            logger.debug("Not deleting because status!=Survey.STATUS_DRAFT");
        }

        Pagez.sendRedirect("/jsp/researcher/index.jsp");
        return "";
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
