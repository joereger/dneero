package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Iterator;

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
public class ResearcherSurveyDetail04 {


    private String billingname;
    private String billingaddress1;
    private String billingaddress2;
    private String billingcity;
    private String billingstate;
    private String billingzip;
    private int billingpaymentmethod;
    private String ccnum;
    private int ccexpmonth;
    private int ccexpyear;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail04(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentResearcherSurveyDetailSurveyid());
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydetail_04";
    }

    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null && survey.getResearcherbillingid()>0){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            Researcherbilling researcherBilling = Researcherbilling.get(survey.getResearcherbillingid());
            if (researcherBilling!=null){
                billingname = researcherBilling.getBillingname();
                billingaddress1 = researcherBilling.getBillingaddress1();
                billingaddress2 = researcherBilling.getBillingaddress2();
                billingcity = researcherBilling.getBillingcity();
                billingstate = researcherBilling.getBillingstate();
                billingzip = researcherBilling.getBillingzip();
                billingpaymentmethod = researcherBilling.getBillingpaymentmethod();
                ccnum = researcherBilling.getCcnum();
                ccexpmonth = researcherBilling.getCcexpmonth();
                ccexpyear = researcherBilling.getCcexpyear();
            }


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

        Researcherbilling researcherBilling = new Researcherbilling();
        if (survey!=null && survey.getResearcherbillingid()>0){
            logger.debug("Found researcherbilling in db: survey.getResearcherbillingid()="+survey.getResearcherbillingid());
            researcherBilling = Researcherbilling.get(survey.getResearcherbillingid());
        }

        researcherBilling.setResearcherid(userSession.getUser().getResearcher().getResearcherid());
        researcherBilling.setBillingname(billingname);
        researcherBilling.setBillingaddress1(billingaddress1);
        researcherBilling.setBillingaddress2(billingaddress2);
        researcherBilling.setBillingcity(billingcity);
        researcherBilling.setBillingstate(billingstate);
        researcherBilling.setBillingzip(billingzip);
        researcherBilling.setBillingpaymentmethod(billingpaymentmethod);
        researcherBilling.setCcnum(ccnum);
        researcherBilling.setCcexpmonth(ccexpmonth);
        researcherBilling.setCcexpyear(ccexpyear);
        try{
            researcherBilling.save();
        } catch (GeneralException gex){
            logger.debug("Save research billing failed: " + gex.getErrorsAsSingleString());
            String message = "New Survey creation failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        survey.setResearcherbillingid(researcherBilling.getResearcherbillingid());

        //Final save
        try{
            logger.debug("saveSurvey() about to save (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("saveSurvey() done saving (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
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


    public String getBillingname() {
        return billingname;
    }

    public void setBillingname(String billingname) {
        this.billingname = billingname;
    }

    public String getBillingaddress1() {
        return billingaddress1;
    }

    public void setBillingaddress1(String billingaddress1) {
        this.billingaddress1 = billingaddress1;
    }

    public String getBillingaddress2() {
        return billingaddress2;
    }

    public void setBillingaddress2(String billingaddress2) {
        this.billingaddress2 = billingaddress2;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingstate() {
        return billingstate;
    }

    public void setBillingstate(String billingstate) {
        this.billingstate = billingstate;
    }

    public String getBillingzip() {
        return billingzip;
    }

    public void setBillingzip(String billingzip) {
        this.billingzip = billingzip;
    }

    public int getBillingpaymentmethod() {
        return billingpaymentmethod;
    }

    public void setBillingpaymentmethod(int billingpaymentmethod) {
        this.billingpaymentmethod = billingpaymentmethod;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCcexpmonth() {
        return ccexpmonth;
    }

    public void setCcexpmonth(int ccexpmonth) {
        this.ccexpmonth = ccexpmonth;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }
}
