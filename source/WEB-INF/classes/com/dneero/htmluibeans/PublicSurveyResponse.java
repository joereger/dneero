package com.dneero.htmluibeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.htmlui.Pagez;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.systemprops.BaseUrl;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Iterator;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyResponse implements Serializable {

    private String takesurveyhtml;
    private Survey survey;
    private User userwhotooksurvey = null;
    private boolean isuserwhotooksurveysameasloggedinuser;
    private SurveyEnhancer surveyEnhancer;
    private boolean loggedinuserhasalreadytakensurvey;
    private String msg = "";
    private boolean surveytakergavetocharity = false;
    private String charityname = "";
    private String surveyResponseHtml;
    private Response response = null;
    private String htmltoposttoblogflash = "";


    public PublicSurveyResponse(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

//        //Set persistent login cookie, if necessary
//        //Needs to be done because Pagez.sendRedirect() flushes cookies and that code is used after login
//        if (Pagez.getRequest().getParameter("keepmeloggedin")!=null && Pagez.getRequest().getParameter("keepmeloggedin").equals("1")){
//            logger.debug("Setting persistent cookie(s)");
//            //Get all possible cookies to set
//            Cookie[] cookies = PersistentLogin.getPersistentCookies(Pagez.getUserSession().getUser().getUserid(), Pagez.getRequest());
//            //Add a cookies to the response
//            for (int j = 0; j < cookies.length; j++) {
//                Pagez.getResponse().addCookie(cookies[j]);
//            }
//        }

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        } else if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            String[] split = Pagez.getRequest().getParameter("action").split("-");
            if (split.length>=3){
                if (split[1]!=null && Num.isinteger(split[1])){
                    surveyid = Integer.parseInt(split[1]);
                }
            }
        }

        //Set the currentsurveyid
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //Load up the survey
        survey = Survey.get(surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0 || survey==null || survey.getTitle()==null){
            return;
        }

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            return;
        }

        //Userid from url
        int userid = 0;
        if (Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("userid"));
            Pagez.getUserSession().setPendingSurveyReferredbyuserid(userid);
            Pagez.getUserSession().setReferredbyOnlyUsedForSignup(userid);
        } else if (Num.isinteger(Pagez.getRequest().getParameter("u"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("u"));
            Pagez.getUserSession().setPendingSurveyReferredbyuserid(userid);
            Pagez.getUserSession().setReferredbyOnlyUsedForSignup(userid);
        } else if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            String[] split = Pagez.getRequest().getParameter("action").split("-");
            if (split.length>=3){
                if (split[2]!=null && Num.isinteger(split[2])){
                    userid = Integer.parseInt(split[2]);
                    Pagez.getUserSession().setPendingSurveyReferredbyuserid(userid);
                    Pagez.getUserSession().setReferredbyOnlyUsedForSignup(userid);
                }
            }
        }

        //Responseid
        int responseid = 0;

        //Set userwhotooksurvey, first verifying that they've actually taken the survey
        userwhotooksurvey = null;
        if (userid>0){
            User userTmp = User.get(userid);
            if (userTmp.getIsenabled()){
                if (userTmp.getBloggerid()>0){
                    Blogger blogger = Blogger.get(userTmp.getBloggerid());
                    for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                        Response response = iterator.next();
                        if (response.getSurveyid()==survey.getSurveyid()){
                            userwhotooksurvey = userTmp;
                            responseid = response.getResponseid();
                            break;
                        }
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

        //Determine whether the user who's seeing the page is the same person who took it
        if (userwhotooksurvey!=null && Pagez.getUserSession().getIsloggedin() && userwhotooksurvey.getUserid()==Pagez.getUserSession().getUser().getUserid()){
            isuserwhotooksurveysameasloggedinuser = true;
        } else {
            isuserwhotooksurveysameasloggedinuser = false;
        }




        //Responseid from url line
        if (Num.isinteger(Pagez.getRequest().getParameter("responseid"))){
            responseid=Integer.parseInt(Pagez.getRequest().getParameter("responseid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("r"))){
            responseid=Integer.parseInt(Pagez.getRequest().getParameter("r"));
        }

        //Load the response
        if (responseid>0){
            response = Response.get(responseid);
        }

        //Record the impression if we have enough info for it
        if (userid>0 && surveyid>0 && (Pagez.getRequest().getParameter("p")==null || Pagez.getRequest().getParameter("p").equals("0"))){
            RecordImpression.record(Pagez.getRequest());
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



        //Survey enhancer
        logger.debug("calling new SurveyEnhancer(survey) from PublicSurvey");
        surveyEnhancer = new SurveyEnhancer(survey);




        //To display to those looking to take survey
        if (!loggedinuserhasalreadytakensurvey){
            takesurveyhtml = SurveyTemplateProcessor.getHtmlForSurveyTaking(survey, new Blogger(), true, userwhotooksurvey);
        } else if (Pagez.getUserSession().getUser()!=null) {
            takesurveyhtml = SurveyTemplateProcessor.getHtmlForSurveyTaking(survey, Blogger.get(Pagez.getUserSession().getUser().getBloggerid()), true, userwhotooksurvey);
        }


        //surveyResponseHtml
        if (userwhotooksurvey!=null){
            String surveyashtml = SurveyAsHtml.getHtml(survey, userwhotooksurvey, false);
            StringBuffer scrollablediv = new StringBuffer();
//            scrollablediv.append("<style>");
//            scrollablediv.append(".questiontitle{");
//            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 13px; font-weight: bold; margin: 0px; border: 0px solid #8d8d8d; padding: 0px; text-align: left; background: #e6e6e6;");
//            scrollablediv.append("}");
//            scrollablediv.append(".answer{");
//            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; margin: 0px;  padding: 0px; text-align: left;");
//            scrollablediv.append("}");
//            scrollablediv.append(".answer_highlight{");
//            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; font-weight: bold; border: 0px solid #c1c1c1; margin: 0px;  padding: 0px; text-align: left; background: #ffffff;");
//            scrollablediv.append("}");
//            scrollablediv.append("</style>");
//            scrollablediv.append("<div style=\"background : #ffffff; padding: 5px; width: 405px; height: 215px; overflow : auto; text-align: left;\">"+"\n");
            scrollablediv.append(surveyashtml);
//            scrollablediv.append("</div>"+"\n");
            surveyResponseHtml = scrollablediv.toString();
        }



        //If blogger has taken the survey already
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            htmltoposttoblogflash = com.dneero.survey.servlet.v1.SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), userwhotooksurvey.getUserid(), responseid, survey.getPlid(), false, true, false);
        } else {
            htmltoposttoblogflash = com.dneero.survey.servlet.v2.SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false, survey.getPlid()), survey.getSurveyid(), userwhotooksurvey.getUserid(), responseid, survey.getPlid(), false, true, false);
        }




    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUserwhotooksurvey() {
        return userwhotooksurvey;
    }

    public void setUserwhotooksurvey(User userwhotooksurvey) {
        this.userwhotooksurvey = userwhotooksurvey;
    }

    public boolean getIsuserwhotooksurveysameasloggedinuser() {
        return isuserwhotooksurveysameasloggedinuser;
    }

    public void setIsuserwhotooksurveysameasloggedinuser(boolean isuserwhotooksurveysameasloggedinuser) {
        this.isuserwhotooksurveysameasloggedinuser = isuserwhotooksurveysameasloggedinuser;
    }

    public SurveyEnhancer getSurveyEnhancer() {
        return surveyEnhancer;
    }

    public void setSurveyEnhancer(SurveyEnhancer surveyEnhancer) {
        this.surveyEnhancer = surveyEnhancer;
    }

    public boolean getLoggedinuserhasalreadytakensurvey() {
        return loggedinuserhasalreadytakensurvey;
    }

    public void setLoggedinuserhasalreadytakensurvey(boolean loggedinuserhasalreadytakensurvey) {
        this.loggedinuserhasalreadytakensurvey = loggedinuserhasalreadytakensurvey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getSurveytakergavetocharity() {
        return surveytakergavetocharity;
    }

    public void setSurveytakergavetocharity(boolean surveytakergavetocharity) {
        this.surveytakergavetocharity = surveytakergavetocharity;
    }

    public String getCharityname() {
        return charityname;
    }

    public void setCharityname(String charityname) {
        this.charityname = charityname;
    }

    public String getSurveyResponseHtml() {
        return surveyResponseHtml;
    }

    public void setSurveyResponseHtml(String surveyResponseHtml) {
        this.surveyResponseHtml = surveyResponseHtml;
    }



    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    public String getTakesurveyhtml() {
        return takesurveyhtml;
    }

    public void setTakesurveyhtml(String takesurveyhtml) {
        this.takesurveyhtml = takesurveyhtml;
    }

    public String getHtmltoposttoblogflash() {
        return htmltoposttoblogflash;
    }

    public void setHtmltoposttoblogflash(String htmltoposttoblogflash) {
        this.htmltoposttoblogflash = htmltoposttoblogflash;
    }
}