package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.*;
import com.dneero.util.Str;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail05 implements Serializable {

    private Survey survey;
    private String title;

    private double willingtopayperrespondent = 2.5;
    private int numberofrespondentsrequested = 1000;
    private double willingtopaypercpm = 10;
    private int maxdisplaysperblog = 500;
    private int maxdisplaystotal = 100000;
    private int status;
    private boolean ischarityonly = false;
    private boolean isresultshidden = false;
    private String charitycustom = "";
    private String charitycustomurl = "";
    private boolean charityonlyallowcustom=false;

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
                charitycustom = survey.getCharitycustom();
                charitycustomurl = survey.getCharitycustomurl();
                charityonlyallowcustom = survey.getCharityonlyallowcustom();
            }

        }

    }



    public void saveSurvey() throws ValidationException {
        ValidationException vex = new ValidationException();
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
                        vex.addValidationError("Max Displays Total must be at least 25% of Number of Respondents Requested multiplied by Max Displays Per Blog (in this case it's "+mindisplays+").  We've already adjusted the value accordingly.");
                        maxdisplaystotal = mindisplays;
                        haveError = true;
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
                if (willingtopayperrespondent<.1){
                    vex.addValidationError("Willing to Pay Per Respondent must be at least $0.10.");
                }
                if (numberofrespondentsrequested<1){
                    vex.addValidationError("Number of Respondents Requested must be at least 1.");
                }
                if (willingtopaypercpm<.25){
                    vex.addValidationError("Willing to Pay Per Thousand Displays to Peers must be at least $0.25.");
                }
                if (maxdisplaysperblog<1000){
                    vex.addValidationError("Max Displays Per Account must be at least 1000.");
                }
                if (charityonlyallowcustom){
                    if (charitycustom==null || charitycustom.equals("") || charitycustomurl==null || charitycustomurl.equals("")){
                        vex.addValidationError("You've specified that respondents can only choose from your custom charity but that custom charity is not properly specified.");
                    }
                }
                if (charitycustom!=null && !charitycustom.equals("")){
                    if (charitycustomurl==null || charitycustomurl.equals("")){
                        vex.addValidationError("You've specified a custom charity but no url where respondents can learn about it.");
                    }
                }
                if (charitycustomurl!=null && !charitycustomurl.equals("")){
                    if (charitycustom==null || charitycustom.equals("")){
                        vex.addValidationError("You've specified a custom charity url but no charity name.");
                    }
                }
                //Validation return
                if (vex.getErrors()!=null && vex.getErrors().length>0){
                    throw vex;
                }


                //Save
                survey.setWillingtopayperrespondent(willingtopayperrespondent);
                survey.setNumberofrespondentsrequested(numberofrespondentsrequested);
                survey.setWillingtopaypercpm(willingtopaypercpm);
                survey.setMaxdisplaysperblog(maxdisplaysperblog);
                survey.setMaxdisplaystotal(maxdisplaystotal);
                survey.setIscharityonly(ischarityonly);
                survey.setIsresultshidden(isresultshidden);
                survey.setCharitycustom(charitycustom);
                survey.setCharitycustomurl(charitycustomurl);
                survey.setCharityonlyallowcustom(charityonlyallowcustom);

                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Refresh
                survey.refresh();
                
            }
        }
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

    public boolean getIsresultshidden() {
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

    public String getCharitycustom() {
        return charitycustom;
    }

    public void setCharitycustom(String charitycustom) {
        this.charitycustom = charitycustom;
    }

    public String getCharitycustomurl() {
        return charitycustomurl;
    }

    public void setCharitycustomurl(String charitycustomurl) {
        this.charitycustomurl = charitycustomurl;
    }

    public boolean getCharityonlyallowcustom() {
        return charityonlyallowcustom;
    }

    public void setCharityonlyallowcustom(boolean charityonlyallowcustom) {
        this.charityonlyallowcustom = charityonlyallowcustom;
    }
}
