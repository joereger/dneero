package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.util.GeneralException;
import com.dneero.survey.servlet.*;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.session.SurveysTakenToday;
import com.dneero.systemprops.BaseUrl;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.facebook.FacebookUser;
import com.dneero.scheduledjobs.SurveydisplayActivityObjectQueue;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyWhotookit implements Serializable {

    private Survey survey;
    private List<Impression> impressions;
    private List<PublicSurveyRespondentsListitem> respondents;


    public PublicSurveyWhotookit(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        }
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            try{Pagez.sendRedirect("/publicsurveylist.jsf"); return;}catch(Exception ex){logger.error("",ex);}
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            try{Pagez.sendRedirect("/surveynotopen.jsf"); return;}catch(Exception ex){logger.error("",ex);}
        }



        //Load impressions
        impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+survey.getSurveyid()+"' and referer<>'' order by impressionstotal desc").setCacheable(true).list();


        //Load respondents
        respondents = new ArrayList();
        List resp = HibernateUtil.getSession().createQuery("from Response where surveyid='"+survey.getSurveyid()+"' order by responseid desc").setCacheable(true).list();
        for (Iterator iterator = resp.iterator(); iterator.hasNext();) {
            Response response = (Response) iterator.next();
            PublicSurveyRespondentsListitem psrli = new PublicSurveyRespondentsListitem();
            psrli.setResponse(response);
            psrli.setUser(User.get(Blogger.get(response.getBloggerid()).getUserid()));
            respondents.add(psrli);
        }

    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public List<Impression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<Impression> impressions) {
        this.impressions=impressions;
    }

    public List<PublicSurveyRespondentsListitem> getRespondents() {
        return respondents;
    }

    public void setRespondents(List<PublicSurveyRespondentsListitem> respondents) {
        this.respondents=respondents;
    }
}
