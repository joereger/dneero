package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.*;
import com.dneero.util.Str;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail05 implements Serializable {

    private String title;

    private double willingtopayperrespondent = 2.5;
    private int numberofrespondentsrequested = 1000;
    private double willingtopaypercpm = 10;
    private int maxdisplaysperblog = 500;
    private int maxdisplaystotal = 100000;
    private int status;
    private boolean ischarityonly = false;
    private boolean isresultshidden = false;
    private Survey survey;

    public ResearcherSurveyDetail05(){

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
                willingtopayperrespondent = survey.getWillingtopayperrespondent();
                numberofrespondentsrequested = survey.getNumberofrespondentsrequested();
                willingtopaypercpm = survey.getWillingtopaypercpm();
                maxdisplaysperblog = survey.getMaxdisplaysperblog();
                maxdisplaystotal = survey.getMaxdisplaystotal();
                status = survey.getStatus();
                ischarityonly = survey.getIscharityonly();
                isresultshidden = survey.getIsresultshidden();
            }

        }

    }

    public String saveSurveyAsDraft(){
        String save = saveSurvey();
        if (save!=null){
            Pagez.sendRedirect("/jsp/researcher/index.jsp");
            return "";
        } else {
            return save;
        }
    }

    public String previousStep(){
        String save = saveSurvey();
        if (save!=null){
            Pagez.sendRedirect("/jsp/researcher/researchersurveydetail_04.jsp");
            return "";
        } else {
            return save;
        }
    }

    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();


            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

                //Validation
                boolean haveError = false;
                //Maxdisplaystotal must be above threshold
                try{
                    int mindisplays = (numberofrespondentsrequested * maxdisplaysperblog)/4;
                    if (maxdisplaystotal < mindisplays){
                        Pagez.getUserSession().setMessage("Max Displays Total must be at least 25% of Number of Respondents Requested multiplied by Max Displays Per Blog (in this case it's "+mindisplays+").  We've already adjusted the value accordingly.");
                        maxdisplaystotal = mindisplays;
                        haveError = true;
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
                //Validation return
                if (haveError){
                    return null;
                }


                //Save
                survey.setWillingtopayperrespondent(willingtopayperrespondent);
                survey.setNumberofrespondentsrequested(numberofrespondentsrequested);
                survey.setWillingtopaypercpm(willingtopaypercpm);
                survey.setMaxdisplaysperblog(maxdisplaysperblog);
                survey.setMaxdisplaystotal(maxdisplaystotal);
                survey.setIscharityonly(ischarityonly);
                survey.setIsresultshidden(isresultshidden);

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
        }

        Pagez.sendRedirect("/jsp/researcher/researchersurveydetail_06.jsp");
        return "";
    }



    public double getWillingtopayperrespondent() {
        return willingtopayperrespondent;
    }

    public void setWillingtopayperrespondent(double willingtopayperrespondent) {
        this.willingtopayperrespondent = willingtopayperrespondent;
    }

    public int getNumberofrespondentsrequested() {
        return numberofrespondentsrequested;
    }

    public void setNumberofrespondentsrequested(int numberofrespondentsrequested) {
        this.numberofrespondentsrequested = numberofrespondentsrequested;
    }

    public double getWillingtopaypercpm() {
        return willingtopaypercpm;
    }

    public void setWillingtopaypercpm(double willingtopaypercpm) {
        this.willingtopaypercpm = willingtopaypercpm;
    }

    public int getMaxdisplaysperblog() {
        return maxdisplaysperblog;
    }

    public void setMaxdisplaysperblog(int maxdisplaysperblog) {
        this.maxdisplaysperblog = maxdisplaysperblog;
    }

    public int getMaxdisplaystotal() {
        return maxdisplaystotal;
    }

    public void setMaxdisplaystotal(int maxdisplaystotal) {
        this.maxdisplaystotal = maxdisplaystotal;
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

    public boolean getIscharityonly() {
        return ischarityonly;
    }

    public void setIscharityonly(boolean ischarityonly) {
        this.ischarityonly = ischarityonly;
    }

    public boolean isIsresultshidden() {
        return isresultshidden;
    }

    public void setIsresultshidden(boolean isresultshidden) {
        this.isresultshidden = isresultshidden;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }
}
