package com.dneero.htmluibeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.systemprops.BaseUrl;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;


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
    private String postresponseinstructions = "";


    public PublicSurveyPostit(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

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
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
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
            logger.debug("got a blogger... bloggerid="+blogger.getBloggerid());
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                logger.debug("--- found a response responseid="+response.getResponseid());
                if (response.getSurveyid()==survey.getSurveyid()){
                    loggedinuserhasalreadytakensurvey=true;
                    responseidOfLoggedinUser = response.getResponseid();
                    logger.debug("success: response.getSurveyid()("+response.getSurveyid()+")==survey.getSurveyid()("+survey.getSurveyid()+")");
                    logger.debug("loggedinuserhasalreadytakensurvey=true");
                } else {
                    logger.debug("response.getSurveyid()("+response.getSurveyid()+")!=survey.getSurveyid()("+survey.getSurveyid()+")");
                }
            }
        } else {
            logger.debug("Not logged in or user==null or user.bloggerid<=0");
        }

        //Set the post-response instructions
        if (loggedinuserhasalreadytakensurvey && responseidOfLoggedinUser>0){
            Response response = Response.get(responseidOfLoggedinUser);
            postresponseinstructions = response.getIncentive().getInstructionsAfterResponse(Response.get(responseidOfLoggedinUser));
        } else {
            postresponseinstructions = "";
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
            if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
                surveyOnBlogPreview = com.dneero.survey.servlet.v1.SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), 0, 0, survey.getPlid(), true, true, false);
            } else {
                surveyOnBlogPreview = com.dneero.survey.servlet.v2.SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), 0, 0, survey.getPlid(), true, true, false);   
            }
        }

        //If blogger has taken the survey already
        if (loggedinuserhasalreadytakensurvey){
            if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
                htmltoposttoblog = com.dneero.survey.servlet.v1.SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, false, true, false);
                htmltoposttoblogflash = com.dneero.survey.servlet.v1.SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, survey.getPlid(), false, true, false);
                htmltoposttoblogflashwithembedandobjecttag = com.dneero.survey.servlet.v1.SurveyFlashServlet.getEmbedSyntaxWithObjectTag(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, survey.getPlid(), false, true, false);
                htmltoposttoblogimagelink = com.dneero.survey.servlet.v1.SurveyImageServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
                htmltoposttobloglink = com.dneero.survey.servlet.v1.SurveyLinkServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
            } else {
                htmltoposttoblog = com.dneero.survey.servlet.v2.SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, false, true, false);
                htmltoposttoblogflash = com.dneero.survey.servlet.v2.SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, survey.getPlid(), false, true, false);
                htmltoposttoblogflashwithembedandobjecttag = com.dneero.survey.servlet.v2.SurveyFlashServlet.getEmbedSyntaxWithObjectTag(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, survey.getPlid(), false, true, false);
                htmltoposttoblogimagelink = com.dneero.survey.servlet.v2.SurveyImageServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
                htmltoposttobloglink = com.dneero.survey.servlet.v2.SurveyLinkServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), Pagez.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
            }
        } else {
            htmltoposttoblog = "";
            htmltoposttoblogflash = "";
            htmltoposttoblogflashwithembedandobjecttag = "";
            htmltoposttoblogimagelink = "";
            htmltoposttobloglink = "";
        }

        //Special Facebook activities
        if (Pagez.getUserSession().getIsfacebookui()){

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
                    facebookApiWrapper.postToFeed(survey, response);
                }
                facebookApiWrapper.updateProfile(Pagez.getUserSession().getUser());
            }
        } catch (Exception ex){
            logger.error("",ex);
            throw new ValidationException("The Facebook server returned some sort of error... please try again or contact support.  We apologize for the inconvenience.");
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

    public boolean getLoggedinuserhasalreadytakensurvey() {
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

    public String getPostresponseinstructions() {
        return postresponseinstructions;
    }

    public void setPostresponseinstructions(String postresponseinstructions) {
        this.postresponseinstructions=postresponseinstructions;
    }
}
