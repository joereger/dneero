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
public class PublicSurveyDiscuss implements Serializable {


    private Survey survey;

    private List<PublicSurveyDiscussListitem> surveydiscusses;
    private String discussSubject="";
    private String discussComment="";


    public PublicSurveyDiscuss(){
        load();
    }

    private void load(){
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
            try{Jsf.redirectResponse("/publicsurveylist.jsf"); return;}catch(Exception ex){logger.error("",ex);}
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            try{Jsf.redirectResponse("/surveynotopen.jsf"); return;}catch(Exception ex){logger.error("",ex);}
        }


        //Load discussion items
        surveydiscusses = new ArrayList();
        List sds = HibernateUtil.getSession().createQuery("from Surveydiscuss where surveyid='"+survey.getSurveyid()+"' and isapproved=true order by surveydiscussid asc").setCacheable(true).list();
        for (Iterator iterator = sds.iterator(); iterator.hasNext();) {
            Surveydiscuss surveydiscuss = (Surveydiscuss) iterator.next();
            PublicSurveyDiscussListitem psdli = new PublicSurveyDiscussListitem();
            psdli.setSurveydiscuss(surveydiscuss);
            psdli.setUser(User.get(surveydiscuss.getUserid()));
            surveydiscusses.add(psdli);
        }
    }



    public String newComment(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        boolean haveError = false;
        if (discussSubject==null || discussSubject.equals("")){
            haveError = true;
            Jsf.setFacesMessage("survey:discussSubject", "Oops! Subject is required.");
        }
        if (discussComment==null || discussComment.equals("")){
            haveError = true;
            Jsf.setFacesMessage("survey:discussComment", "Oops! Comment is required.");
        }
        if (haveError){
            return null;
        }
        if (Pagez.getUserSession().getIsloggedin()){
            Surveydiscuss surveydiscuss = new Surveydiscuss();
            surveydiscuss.setSurveyid(survey.getSurveyid());
            surveydiscuss.setIsapproved(true);
            surveydiscuss.setSubject(UserInputSafe.clean(discussSubject));
            surveydiscuss.setComment(UserInputSafe.clean(discussComment));
            surveydiscuss.setDate(new Date());
            surveydiscuss.setUserid(Pagez.getUserSession().getUser().getUserid());
            try{
                surveydiscuss.save();
                Jsf.setFacesMessage("Your comment has been posted!");
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Sorry, there was an error: " + gex.getErrorsAsSingleString());
                logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
                return null;
            }
            //load();
        }
        //Return from survey new comment in a way that retains the survey url
        try{Jsf.redirectResponse("/surveydiscuss.jsf?surveyid="+Pagez.getUserSession().getCurrentSurveyid()); return null;}catch(Exception ex){logger.error("",ex);}
        return "publicsurveydiscuss";
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public List<PublicSurveyDiscussListitem> getSurveydiscusses() {
        return surveydiscusses;
    }

    public void setSurveydiscusses(List<PublicSurveyDiscussListitem> surveydiscusses) {
        this.surveydiscusses=surveydiscusses;
    }

    public String getDiscussSubject() {
        return discussSubject;
    }

    public void setDiscussSubject(String discussSubject) {
        this.discussSubject=discussSubject;
    }

    public String getDiscussComment() {
        return discussComment;
    }

    public void setDiscussComment(String discussComment) {
        this.discussComment=discussComment;
    }
}
