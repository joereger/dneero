package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.LostPasswordSend;
import com.dneero.email.EmailActivationSend;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.survey.servlet.SurveyJavascriptServlet;
import com.dneero.survey.servlet.SurveyFlashServlet;
import com.dneero.survey.servlet.SurveyImagelinkServlet;
import com.dneero.systemprops.BaseUrl;
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
    private String surveycriteriaasstring;

    public SysadminSurveyDetail(){


    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            load(Integer.parseInt(tmpSurveyid));
        }
        return "sysadminsurveydetail";
    }

    private void load(int surveyid){
        survey = Survey.get(surveyid);
        if (survey!=null && survey.getSurveyid()>0){
            surveyOnBlogPreview = SurveyJavascriptServlet.getEmbedSyntax("/", survey.getSurveyid(), 0, true, true);
            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
            sms = new SurveyMoneyStatus(survey);
            researcher = Researcher.get(survey.getResearcherid());
            user = User.get(researcher.getUserid());
            SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
            surveycriteriaasstring = surveyCriteriaXML.getSurveyCriteriaAsString();
        }
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

    public String getSurveycriteriaasstring() {
        return surveycriteriaasstring;
    }

    public void setSurveycriteriaasstring(String surveycriteriaasstring) {
        this.surveycriteriaasstring = surveycriteriaasstring;
    }
}
