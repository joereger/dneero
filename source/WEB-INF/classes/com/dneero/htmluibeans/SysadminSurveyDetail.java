package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.util.DateDiff;
import com.dneero.dao.*;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.survey.servlet.SurveyFlashServlet;
import com.dneero.display.SurveyTakerDisplay;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminSurveyDetail implements Serializable {

    private Survey survey;
    private SurveyEnhancer surveyEnhancer;
    private String surveyForTakers;
    private String surveyOnBlogPreview;
    private SurveyMoneyStatus sms;
    private Researcher researcher;
    private User user;
    private String surveyCriteriaAsHtml;
    private int dayssinceclose = 0;

    public SysadminSurveyDetail(){
        beginView();
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpSurveyid = Pagez.getRequest().getParameter("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            load(Integer.parseInt(tmpSurveyid));
        }
        return "sysadminsurveydetail";
    }

    private void load(int surveyid){
        survey = Survey.get(surveyid);
        if (survey!=null && survey.getSurveyid()>0){
            surveyOnBlogPreview = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), 0, 0, true, true, false);
            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
            sms = new SurveyMoneyStatus(survey);
            researcher = Researcher.get(survey.getResearcherid());
            user = User.get(researcher.getUserid());
            SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
            surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
            dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
        }
    }


    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");

        //Validation
        boolean isValidData = true;
        Calendar beforeMinusDay = Time.xDaysAgoStart(Calendar.getInstance(), 0);
        if (survey.getStartdate().after(survey.getEnddate())){
            isValidData = false;
            Jsf.setFacesMessage("surveyedit:enddate", "The End Date must be after the Start Date.");
            logger.debug("valdation error - startdate is after end date.");
        }
        if (isValidData){
            try{
                logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                survey.save();
                Jsf.setFacesMessage("Survey saved!");
                logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
            } catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                Jsf.setFacesMessage(message);
                return null;
            }
        } else {
            Jsf.setFacesMessage("There was an error.");
            return null;
        }
        load(survey.getSurveyid());
        return "sysadminsurveydetail";
    }

    public LinkedHashMap getStatuses(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Draft", Survey.STATUS_DRAFT);
        out.put("Waiting for Start Date", Survey.STATUS_WAITINGFORSTARTDATE);
        out.put("Waiting for Funds", Survey.STATUS_WAITINGFORFUNDS);
        out.put("Open", Survey.STATUS_OPEN);
        out.put("Closed", Survey.STATUS_CLOSED);
        return out;
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public SurveyEnhancer getSurveyEnhancer() {
        return surveyEnhancer;
    }

    public void setSurveyEnhancer(SurveyEnhancer surveyEnhancer) {
        this.surveyEnhancer = surveyEnhancer;
    }

    public String getSurveyForTakers() {
        return surveyForTakers;
    }

    public void setSurveyForTakers(String surveyForTakers) {
        this.surveyForTakers = surveyForTakers;
    }

    public String getSurveyOnBlogPreview() {
        return surveyOnBlogPreview;
    }

    public void setSurveyOnBlogPreview(String surveyOnBlogPreview) {
        this.surveyOnBlogPreview = surveyOnBlogPreview;
    }

    public SurveyMoneyStatus getSms() {
        return sms;
    }

    public void setSms(SurveyMoneyStatus sms) {
        this.sms = sms;
    }


    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getSurveyCriteriaAsHtml() {
        return surveyCriteriaAsHtml;
    }

    public void setSurveyCriteriaAsHtml(String surveyCriteriaAsHtml) {
        this.surveyCriteriaAsHtml = surveyCriteriaAsHtml;
    }

    public int getDayssinceclose() {
        return dayssinceclose;
    }

    public void setDayssinceclose(int dayssinceclose) {
        this.dayssinceclose = dayssinceclose;
    }
}
