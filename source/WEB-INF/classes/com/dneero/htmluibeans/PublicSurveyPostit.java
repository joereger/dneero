package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.ui.SurveyEnhancer;


import com.dneero.util.Num;

import com.dneero.survey.servlet.*;
import com.dneero.systemprops.BaseUrl;

import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import java.io.Serializable;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyPostit implements Serializable {



    private Survey survey;
    private SurveyEnhancer surveyEnhancer;
    private boolean loggedinuserhasalreadytakensurvey;
    private String surveyOnBlogPreview;
    private boolean surveytakergavetocharity = false;
    private String charityname = "";
    private User userwhotooksurvey = null;
    private String htmltoposttoblog = "";
    private String htmltoposttoblogflash = "";
    private String htmltoposttoblogflashwithembedandobjecttag = "";
    private String htmltoposttoblogimagelink = "";
    private String htmltoposttobloglink = "";
    private boolean justcompletedsurvey = false;
    private String invitefriendsurl = "";

    public PublicSurveyPostit(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");

        //Surveyid from session or url
        logger.debug("Pagez.getUserSession().getCurrentSurveyid()="+Pagez.getUserSession().getCurrentSurveyid());
        logger.debug("Pagez.getRequest().getParameter(\"surveyid\")="+Pagez.getRequest().getParameter("surveyid"));
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
            Pagez.sendRedirect("/publicsurveylist.jsp");
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            Pagez.sendRedirect("/surveynotopen.jsp");
            return;
        }

        //Userid from url
        int userid = 0;
        if (Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("userid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("u"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("u"));
        }

        //Set userwhotooksurvey, first verifying that they've actually taken the survey
        userwhotooksurvey = null;
        if (userid>0){
            User userTmp = User.get(userid);
            if (userTmp.getBloggerid()>0){
                Blogger blogger = Blogger.get(userTmp.getBloggerid());
                for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        userwhotooksurvey = userTmp;
                        break;
                    }
                }
            }
        }


        //See if logged in user has taken survey yet
        loggedinuserhasalreadytakensurvey = false;
        int responseidOfLoggedinUser = 0;
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    loggedinuserhasalreadytakensurvey = true;
                    responseidOfLoggedinUser = response.getResponseid();
                }
            }
        }

        //If we don't have a userwhotooksurvey yet but the logged-in user has, use the logged-in user
        if (userwhotooksurvey==null && loggedinuserhasalreadytakensurvey){
            if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                userwhotooksurvey = Pagez.getUserSession().getUser();
            }
        }

        //Find charity status
        surveytakergavetocharity = false;
        charityname = "";
        if (userwhotooksurvey!=null && userwhotooksurvey.getBloggerid()>0){
            Blogger blogger = Blogger.get(userwhotooksurvey.getBloggerid());
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    if (response.getIsforcharity()){
                        surveytakergavetocharity = true;
                        charityname = response.getCharityname();
                        break;
                    }
                }
            }
        }

        //Justcompletedsurvey
        justcompletedsurvey = false;
        logger.debug("Pagez.getRequest().getParameter(\"justcompletedsurvey\")="+Pagez.getRequest().getParameter("justcompletedsurvey"));
        if (Pagez.getRequest().getParameter("justcompletedsurvey")!=null && Pagez.getRequest().getParameter("justcompletedsurvey").equals("1")){
            justcompletedsurvey = true;
        }

        //Survey enhancer
        surveyEnhancer = new SurveyEnhancer(survey);



        //Survey on blog preview
        if (loggedinuserhasalreadytakensurvey){
            surveyOnBlogPreview = "";
        } else {
            surveyOnBlogPreview = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), 0, 0, true, true, false);
        }

        //If blogger has taken the survey already
        if (loggedinuserhasalreadytakensurvey){
            htmltoposttoblog = SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, false, true, false);
            htmltoposttoblogflash = SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, true, false);
            htmltoposttoblogflashwithembedandobjecttag = SurveyFlashServlet.getEmbedSyntaxWithObjectTag(BaseUrl.get(false), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, true, false);
            htmltoposttoblogimagelink = SurveyImageServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
            htmltoposttobloglink = SurveyLinkServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
        } else {
            htmltoposttoblog = "";
            htmltoposttoblogflash = "";
            htmltoposttoblogflashwithembedandobjecttag = "";
            htmltoposttoblogimagelink = "";
            htmltoposttobloglink = "";
        }

        //Special Facebook activities
        if (Pagez.getUserSession().getIsfacebookui()){
            //Invite friends link
            FacebookApiWrapper faw = new FacebookApiWrapper(Pagez.getUserSession());
            invitefriendsurl = faw.inviteFriendsToSurvey(survey);
        }


    }

    public void updateFacebookProfile() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            //Update Facebook
            if (Pagez.getUserSession().getUser().getBloggerid()>0){
                FacebookApiWrapper facebookApiWrapper = new FacebookApiWrapper(Pagez.getUserSession());
                List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                                   .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                                   .add(Restrictions.eq("bloggerid", Pagez.getUserSession().getUser().getBloggerid()))
                                                   .setCacheable(false)
                                                   .list();
                for (Iterator<Response> iterator=responses.iterator(); iterator.hasNext();) {
                    Response response=iterator.next();
                    facebookApiWrapper.postSurveyToFacebookMiniFeed(survey, response);
                }
                facebookApiWrapper.updateFacebookProfile(Pagez.getUserSession().getUser());
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public SurveyEnhancer getSurveyEnhancer() {
        return surveyEnhancer;
    }

    public void setSurveyEnhancer(SurveyEnhancer surveyEnhancer) {
        this.surveyEnhancer=surveyEnhancer;
    }

    public boolean isLoggedinuserhasalreadytakensurvey() {
        return loggedinuserhasalreadytakensurvey;
    }

    public void setLoggedinuserhasalreadytakensurvey(boolean loggedinuserhasalreadytakensurvey) {
        this.loggedinuserhasalreadytakensurvey=loggedinuserhasalreadytakensurvey;
    }

    public String getSurveyOnBlogPreview() {
        return surveyOnBlogPreview;
    }

    public void setSurveyOnBlogPreview(String surveyOnBlogPreview) {
        this.surveyOnBlogPreview=surveyOnBlogPreview;
    }

    public boolean getSurveytakergavetocharity() {
        return surveytakergavetocharity;
    }

    public void setSurveytakergavetocharity(boolean surveytakergavetocharity) {
        this.surveytakergavetocharity=surveytakergavetocharity;
    }

    public String getCharityname() {
        return charityname;
    }

    public void setCharityname(String charityname) {
        this.charityname=charityname;
    }

    public User getUserwhotooksurvey() {
        return userwhotooksurvey;
    }

    public void setUserwhotooksurvey(User userwhotooksurvey) {
        this.userwhotooksurvey=userwhotooksurvey;
    }

    public String getHtmltoposttoblog() {
        return htmltoposttoblog;
    }

    public void setHtmltoposttoblog(String htmltoposttoblog) {
        this.htmltoposttoblog=htmltoposttoblog;
    }

    public String getHtmltoposttoblogflash() {
        return htmltoposttoblogflash;
    }

    public void setHtmltoposttoblogflash(String htmltoposttoblogflash) {
        this.htmltoposttoblogflash=htmltoposttoblogflash;
    }

    public String getHtmltoposttoblogflashwithembedandobjecttag() {
        return htmltoposttoblogflashwithembedandobjecttag;
    }

    public void setHtmltoposttoblogflashwithembedandobjecttag(String htmltoposttoblogflashwithembedandobjecttag) {
        this.htmltoposttoblogflashwithembedandobjecttag=htmltoposttoblogflashwithembedandobjecttag;
    }

    public String getHtmltoposttoblogimagelink() {
        return htmltoposttoblogimagelink;
    }

    public void setHtmltoposttoblogimagelink(String htmltoposttoblogimagelink) {
        this.htmltoposttoblogimagelink=htmltoposttoblogimagelink;
    }

    public String getHtmltoposttobloglink() {
        return htmltoposttobloglink;
    }

    public void setHtmltoposttobloglink(String htmltoposttobloglink) {
        this.htmltoposttobloglink=htmltoposttobloglink;
    }


    public boolean getJustcompletedsurvey() {
        return justcompletedsurvey;
    }

    public void setJustcompletedsurvey(boolean justcompletedsurvey) {
        this.justcompletedsurvey=justcompletedsurvey;
    }

    public String getInvitefriendsurl() {
        return invitefriendsurl;
    }

    public void setInvitefriendsurl(String invitefriendsurl) {
        this.invitefriendsurl=invitefriendsurl;
    }
}
