package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.*;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.session.UserSession;

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

    public ResearcherSurveyDetail05(){

    }

    public String beginView(){
        load();
        return "researchersurveydetail_05";
    }

    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
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
            ResearcherIndex bean = (ResearcherIndex)Jsf.getManagedBean("researcherIndex");
            return bean.beginView();
        } else {
            return save;
        }
    }

    public String previousStep(){
        String save = saveSurvey();
        if (save!=null){
            ResearcherSurveyDetail04 bean = (ResearcherSurveyDetail04)Jsf.getManagedBean("researcherSurveyDetail04");
            return bean.beginView();
            //return "researchersurveydetail_04";
        } else {
            return save;
        }
    }

    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }


            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){

                //Validation
                boolean haveError = false;
                //Maxdisplaystotal must be above threshold
                try{
                    int mindisplays = (numberofrespondentsrequested * maxdisplaysperblog)/4;
                    if (maxdisplaystotal < mindisplays){
                        Jsf.setFacesMessage("surveyedit:maxdisplaystotal", "Max Displays Total must be at least 25% of Number of Respondents Requested multiplied by Max Displays Per Blog (in this case it's "+mindisplays+").  We've already adjusted the value accordingly.");
                        maxdisplaystotal = mindisplays;
                        haveError = true;
                    }
                } catch (Exception ex){
                    logger.error(ex);
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

        ResearcherSurveyDetail06 bean = (ResearcherSurveyDetail06)Jsf.getManagedBean("researcherSurveyDetail06");
        return bean.beginView();
        //return "researchersurveydetail_06";
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
}
