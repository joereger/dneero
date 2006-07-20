package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.session.UserSession;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyResults {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private int totalsurveyresponses = 0;
    private int totalsurveydisplays = 0;
    private int status = 0;

    public ResearcherSurveyResults(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            UserSession userSession = Jsf.getUserSession();
            userSession.setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveyresults";
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            totalsurveyresponses = survey.getResponses().size();
            totalsurveydisplays = 0;
            status = survey.getStatus();
            for (Iterator<Impression> iterator = survey.getImpressions().iterator(); iterator.hasNext();) {
                Impression impression = iterator.next();
                totalsurveydisplays = totalsurveydisplays + impression.getTotalimpressions();
            }

        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public int getTotalsurveyresponses() {
        return totalsurveyresponses;
    }

    public void setTotalsurveyresponses(int totalsurveyresponses) {
        this.totalsurveyresponses = totalsurveyresponses;
    }

    public int getTotalsurveydisplays() {
        return totalsurveydisplays;
    }

    public void setTotalsurveydisplays(int totalsurveydisplays) {
        this.totalsurveydisplays = totalsurveydisplays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
