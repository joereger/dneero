package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;

import com.dneero.dao.*;
import com.dneero.util.*;
import com.dneero.session.UserSession;
import com.dneero.finders.FindBloggersForSurvey;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail06 {

    private int status;
    private int numberofbloggersqualifiedforthissurvey = 0;
    private int daysinsurveyperiod = 0;
    private String startdate;
    private String enddate;
    private String maxrespondentpayments = "";
    private String maximpressionpayments = "";
    private String maxincentive = "0";
    private String dneerofee = "0";
    private String maxpossiblespend = "0";
    private int numberofquestions = 0;
    private String averagebloggerwillbepaid = "0";
    private boolean warningtimeperiodtooshort = false;
    private boolean warningnumberofbloggerslessthanrequested = false;
    private boolean warningnumberrequestedratiotoobig = false;
    private boolean warningtoomanyquestions = false;
    private boolean warningnoquestions = false;



    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail06(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }



    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                status = survey.getStatus();
                FindBloggersForSurvey fb = new FindBloggersForSurvey(survey);
                numberofbloggersqualifiedforthissurvey = fb.getBloggers().size();

                daysinsurveyperiod = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getStartdate()), Time.getCalFromDate(survey.getEnddate()));

                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));
                enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));

                double maxresppay = (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested());
                maxrespondentpayments = "$"+Str.formatForMoney(maxresppay);

                double maximppay = ((survey.getWillingtopaypercpm()*survey.getMaxdisplaystotal())/1000);
                maximpressionpayments = "$"+Str.formatForMoney(maximppay);


                double maxspend =maxresppay + maximppay;
                maxincentive = "$"+Str.formatForMoney(maxspend);

                double dfee = maxspend * .2;
                dneerofee = "$"+Str.formatForMoney(dfee);

                maxpossiblespend = "$"+Str.formatForMoney(maxspend + dfee);

                numberofquestions = survey.getQuestions().size();

                int avgbloggerimpressions = 250;
                if (avgbloggerimpressions>survey.getMaxdisplaysperblog()){
                    avgbloggerimpressions = survey.getMaxdisplaysperblog();
                }
                double avgbloggerpaid =
                (survey.getWillingtopayperrespondent())
                + ((survey.getWillingtopaypercpm()*avgbloggerimpressions)/1000);
                averagebloggerwillbepaid = "$"+Str.formatForMoney(avgbloggerpaid);

                //Warning: time period too short
                if (DateDiff.dateDiff("day", Time.getCalFromDate(survey.getStartdate()), Time.getCalFromDate(survey.getEnddate()))<7){
                    warningtimeperiodtooshort = true;
                }

                //Warning: number of bloggers less than requested
                if (numberofbloggersqualifiedforthissurvey < survey.getNumberofrespondentsrequested()){
                    warningnumberofbloggerslessthanrequested = true;
                }

                //Warning: number of bloggers requested is over 50% of those qualified
                if (numberofbloggersqualifiedforthissurvey>0){
                    double percent = survey.getNumberofrespondentsrequested() / numberofbloggersqualifiedforthissurvey;
                    if (percent > .5){
                        warningnumberrequestedratiotoobig = true;
                    }
                } else {
                    warningnumberrequestedratiotoobig = true;
                }

                //Warning: too many questions
                if (numberofquestions>20){
                    warningtoomanyquestions = true;
                }

                //Warning: no questions
                if (numberofquestions<=0){
                    warningnoquestions = true;   
                }

            }
        }

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
                Calendar startdate = Time.getCalFromDate(survey.getStartdate());
                Calendar now = Calendar.getInstance();
                logger.debug("now="+Time.dateformatfordb(now));
                logger.debug("startdate="+Time.dateformatfordb(startdate));
                logger.debug("startdate.before(now)="+startdate.before(now));
                logger.debug("startdate.after(now)="+startdate.after(now));



                if (startdate.before(now)){
                    survey.setStatus(Survey.STATUS_OPEN);
                } else {
                    survey.setStatus(Survey.STATUS_WAITINGFORSTARTDATE);
                }


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

        return "success";
    }

    public String saveSurveyAsDraft(){
        logger.debug("saveSurvey() called.");
        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
        }

        if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
            survey.setStatus(Survey.STATUS_DRAFT);
            try{survey.save();} catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                return null;
            }


            //Refresh
            survey.refresh();
        }
        return "success";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumberofbloggersqualifiedforthissurvey() {
        return numberofbloggersqualifiedforthissurvey;
    }

    public void setNumberofbloggersqualifiedforthissurvey(int numberofbloggersqualifiedforthissurvey) {
        this.numberofbloggersqualifiedforthissurvey = numberofbloggersqualifiedforthissurvey;
    }

    public int getDaysinsurveyperiod() {
        return daysinsurveyperiod;
    }

    public void setDaysinsurveyperiod(int daysinsurveyperiod) {
        this.daysinsurveyperiod = daysinsurveyperiod;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(String maxpossiblespend) {
        this.maxpossiblespend = maxpossiblespend;
    }

    public int getNumberofquestions() {
        return numberofquestions;
    }

    public void setNumberofquestions(int numberofquestions) {
        this.numberofquestions = numberofquestions;
    }

    public String getAveragebloggerwillbepaid() {
        return averagebloggerwillbepaid;
    }

    public void setAveragebloggerwillbepaid(String averagebloggerwillbepaid) {
        this.averagebloggerwillbepaid = averagebloggerwillbepaid;
    }

    public boolean isWarningtimeperiodtooshort() {
        return warningtimeperiodtooshort;
    }

    public void setWarningtimeperiodtooshort(boolean warningtimeperiodtooshort) {
        this.warningtimeperiodtooshort = warningtimeperiodtooshort;
    }

    public boolean isWarningnumberofbloggerslessthanrequested() {
        return warningnumberofbloggerslessthanrequested;
    }

    public void setWarningnumberofbloggerslessthanrequested(boolean warningnumberofbloggerslessthanrequested) {
        this.warningnumberofbloggerslessthanrequested = warningnumberofbloggerslessthanrequested;
    }

    public boolean isWarningnumberrequestedratiotoobig() {
        return warningnumberrequestedratiotoobig;
    }

    public void setWarningnumberrequestedratiotoobig(boolean warningnumberrequestedratiotoobig) {
        this.warningnumberrequestedratiotoobig = warningnumberrequestedratiotoobig;
    }

    public boolean isWarningtoomanyquestions() {
        return warningtoomanyquestions;
    }

    public void setWarningtoomanyquestions(boolean warningtoomanyquestions) {
        this.warningtoomanyquestions = warningtoomanyquestions;
    }

    public String getDneerofee() {
        return dneerofee;
    }

    public void setDneerofee(String dneerofee) {
        this.dneerofee = dneerofee;
    }

    public String getMaxincentive() {
        return maxincentive;
    }

    public void setMaxincentive(String maxincentive) {
        this.maxincentive = maxincentive;
    }

    public String getMaxrespondentpayments() {
        return maxrespondentpayments;
    }

    public void setMaxrespondentpayments(String maxrespondentpayments) {
        this.maxrespondentpayments = maxrespondentpayments;
    }

    public String getMaximpressionpayments() {
        return maximpressionpayments;
    }

    public void setMaximpressionpayments(String maximpressionpayments) {
        this.maximpressionpayments = maximpressionpayments;
    }

    public boolean isWarningnoquestions() {
        return warningnoquestions;
    }

    public void setWarningnoquestions(boolean warningnoquestions) {
        this.warningnoquestions = warningnoquestions;
    }
}
