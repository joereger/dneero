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
public class PublicSurvey implements Serializable {


    private String takesurveyhtml;
    private boolean haveerror = false;
    private Survey survey;
    private User userwhotooksurvey = null;
    private boolean isuserwhotooksurveysameasloggedinuser;
    private SurveyEnhancer surveyEnhancer;
    private boolean loggedinuserhasalreadytakensurvey;
    private String socialbookmarklinks = "";
    private boolean qualifiesforsurvey = true;
    private String msg = "";
    private boolean bloggerhastakentoomanysurveysalreadytoday = false;
    private List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
    private TreeMap<String, String> facebookuserswhodidnottakesurvey = new TreeMap<String, String>();
    private String DNEERO_REQUEST_PARAM_IDENTIFIER = SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER;
    private boolean surveytakergavetocharity = false;
    private String charityname = "";
    private String invitefriendsurl = "";
    private String surveyResponseHtml;
    private String surveyResponseFlashEmbed;


    public PublicSurvey(){

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

        //Userid from url
        int userid = 0;
        if (Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("userid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("u"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("u"));
        }
        Pagez.getUserSession().setPendingSurveyReferredbyuserid(userid);
        Pagez.getUserSession().setReferredbyOnlyUsedForSignup(userid);

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

        //Determine whether the user who's seeing the page is the same person who took it
        if (userwhotooksurvey!=null && Pagez.getUserSession().getIsloggedin() && userwhotooksurvey.getUserid()==Pagez.getUserSession().getUser().getUserid()){
            isuserwhotooksurveysameasloggedinuser = true;
        } else {
            isuserwhotooksurveysameasloggedinuser = false;
        }

        //Responseid
        int responseid = 0;
        if (Num.isinteger(Pagez.getRequest().getParameter("responseid"))){
            responseid=Integer.parseInt(Pagez.getRequest().getParameter("responseid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("r"))){
            responseid=Integer.parseInt(Pagez.getRequest().getParameter("r"));
        }

        //Record the impression if we have enough info for it
        if (userid>0 && surveyid>0 && (Pagez.getRequest().getParameter("p")==null || Pagez.getRequest().getParameter("p").equals("0"))){
            RecordImpression.record(Jsf.getHttpServletRequest());
        }


        //Turn on the correct tab
        if (survey.getStatus()!=Survey.STATUS_OPEN){
            //redirect to results
            //try{Pagez.sendRedirect("/surveyresults.jsf?surveyid="+surveyid); return;}catch(Exception ex){logger.error("",ex);}
        }
        if (loggedinuserhasalreadytakensurvey && isuserwhotooksurveysameasloggedinuser){
            //redirect to postit
            //try{Pagez.sendRedirect("/surveypostit.jsf?surveyid="+surveyid); return;}catch(Exception ex){logger.error("",ex);}
        }
        if (Pagez.getRequest().getParameter("show")!=null && Pagez.getRequest().getParameter("show").equals("results")){
            //redirect to results
            try{Pagez.sendRedirect("/surveyresults.jsf?surveyid="+surveyid+"&userid="+userid); return;}catch(Exception ex){logger.error("",ex);}
        }
        if (Pagez.getRequest().getParameter("show")!=null && Pagez.getRequest().getParameter("show").equals("disclosure")){
            //redirect to disclosure
            try{Pagez.sendRedirect("/surveydisclosure.jsf?surveyid="+surveyid); return;}catch(Exception ex){logger.error("",ex);}
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

        //Social bookmark links
        socialbookmarklinks = SocialBookmarkLinks.getSocialBookmarkLinks(survey);

        //Survey enhancer
        surveyEnhancer = new SurveyEnhancer(survey);



        //Max surveys per day
        if (Pagez.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
            bloggerhastakentoomanysurveysalreadytoday = true;
        } else {
            bloggerhastakentoomanysurveysalreadytoday = false;
        }

        //See if blogger is qualified to take survey
        qualifiesforsurvey = true;
        if (!loggedinuserhasalreadytakensurvey && Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
                qualifiesforsurvey = false;
            }
        }

        //To display to those looking to take survey
        if (!loggedinuserhasalreadytakensurvey){
            takesurveyhtml = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
        } else {
            takesurveyhtml = "";
        }

        //The main survey flash embed
        if (userwhotooksurvey!=null){
            surveyResponseFlashEmbed = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), userwhotooksurvey.getUserid(), responseid, true, true, false);
        }

        //surveyResponseHtml
        if (userwhotooksurvey!=null){
            String surveyashtml = SurveyAsHtml.getHtml(survey, userwhotooksurvey, false);
            StringBuffer scrollablediv = new StringBuffer();
            scrollablediv.append("<style>");
            scrollablediv.append(".questiontitle{");
            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 13px; font-weight: bold; margin: 0px; border: 0px solid #8d8d8d; padding: 0px; text-align: left; background: #e6e6e6;");
            scrollablediv.append("}");
            scrollablediv.append(".answer{");
            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; margin: 0px;  padding: 0px; text-align: left;");
            scrollablediv.append("}");
            scrollablediv.append(".answer_highlight{");
            scrollablediv.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; font-weight: bold; border: 0px solid #c1c1c1; margin: 0px;  padding: 0px; text-align: left; background: #ffffff;");
            scrollablediv.append("}");
            scrollablediv.append("</style>");
            scrollablediv.append("<div style=\"background : #ffffff; padding: 5px; width: 405px; height: 215px; overflow : auto; text-align: left;\">"+"\n");
            scrollablediv.append(surveyashtml);
            scrollablediv.append("</div>"+"\n");
            surveyResponseHtml = scrollablediv.toString();
        }

        

        //Special Facebook activities
        if (Pagez.getUserSession().getIsfacebookui()){
            //Load facebook users
            loadFacebookUsers();

            //Invite friends link
            FacebookApiWrapper faw = new FacebookApiWrapper(Pagez.getUserSession());
            invitefriendsurl = faw.inviteFriendsToSurvey(survey);
        }




        //Record the survey display using the display cache
        SurveydisplayActivityObject sdao = new SurveydisplayActivityObject();
        sdao.setSurveyid(survey.getSurveyid());
        SurveydisplayActivityObjectQueue.addSdao(sdao);


    }


    public String takeSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("takeSurvey() called");
        survey = Survey.get(survey.getSurveyid());

        //If the user's logged in and is a blogger make sure they're qualified for this survey
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(Blogger.get(Pagez.getUserSession().getUser().getBloggerid()), survey)){
                Jsf.setFacesMessage("Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey.");
                return "publicsurvey";
            }
        }

        //If the user's already taken too many surveys
        if (Pagez.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
            //Jsf.setFacesMessage("Sorry, you've already taken the maximum number of surveys today ("+SurveysTakenToday.MAXSURVEYSPERDAY+").  Wait until tomorrow (defined in U.S. Eastern Standard Time) and try again.");
            return "publicsurvey";
        }


        //Validate the response and put into memory
        SurveyResponseParser srp;
        try{
            srp = new SurveyResponseParser((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
            createResponse(survey, srp, null);
            haveerror = false;
        } catch (ComponentException cex){
            haveerror = true;
            Jsf.setFacesMessage(cex.getErrorsAsSingleString());
            return "publicsurvey";
        }

        //If the user is logged-in but has not created a blogger profile, store a pending response
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()<=0){
            //Pending survey save
            //Note: this code also on Login and Registration
            Responsepending responsepending = new Responsepending();
            responsepending.setUserid(Pagez.getUserSession().getUser().getUserid());
            responsepending.setResponseasstring(Pagez.getUserSession().getPendingSurveyResponseAsString());
            responsepending.setReferredbyuserid(Pagez.getUserSession().getPendingSurveyReferredbyuserid());
            responsepending.setSurveyid(Pagez.getUserSession().getPendingSurveyResponseSurveyid());
            try{responsepending.save();}catch(Exception ex){logger.error("",ex);}
            Pagez.getUserSession().setPendingSurveyResponseSurveyid(0);
            Pagez.getUserSession().setPendingSurveyReferredbyuserid(0);
            Pagez.getUserSession().setPendingSurveyResponseAsString("");

        }

        //If the user's logged in and is a blogger
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            try{
                BloggerIndex.storeResponseInDb(survey, srp, blogger, Pagez.getUserSession().getPendingSurveyReferredbyuserid());
            }catch (ComponentException cex){
                haveerror = true;
                Jsf.setFacesMessage(cex.getErrorsAsSingleString());
                return "publicsurvey";
            }catch(Exception ex){
                logger.error("",ex);
            }
        }

        //Where to send afterwards?
        if (Pagez.getUserSession().getIsloggedin()){
            if (Pagez.getUserSession().getUser().getBloggerid()>0){
                //load();
                logger.debug("redirecting, will add justcompletedsurvey=1");
                try{Pagez.sendRedirect("/surveypostit.jsf?surveyid="+survey.getSurveyid()+"&justcompletedsurvey=1");}catch(Exception ex){logger.error("",ex);}
                return "publicsurvey";
            } else {
                AccountIndex bean = (AccountIndex)Jsf.getManagedBean("accountIndex");
                return bean.beginView();
            }
        }
        Registration bean = (Registration)Jsf.getManagedBean("registration");
        return bean.beginView();
    }


    public static void createResponse(Survey survey, SurveyResponseParser srp, Blogger blogger) throws ComponentException{
        Logger logger = Logger.getLogger(PublicSurvey.class);
        ComponentException allCex = new ComponentException();

        //Make sure each component is validated
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
            logger.debug("found component.getName()="+component.getName());
            try{
                component.validateAnswer(srp);
            } catch (ComponentException cex){
                logger.debug(cex);
                allCex.addErrorsFromAnotherGeneralException(cex);
            }
        }
        //If we are validated
        if (allCex.getErrors().length<=0){
            if (Pagez.getUserSession()!=null){
                //Store this response in memory for now
                Pagez.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Pagez.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing survey response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){
            throw allCex;
        }
    }



    

    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
        facebookuserswhodidnottakesurvey = new TreeMap<String, String>();
        if (survey!=null){
            //Go to facebook and get a list of the logged-in user's friends
            FacebookApiWrapper faw = new FacebookApiWrapper(Pagez.getUserSession());
            ArrayList<FacebookUser> friends = faw.getFriends();
            if (friends.size()>0){
                //Build sql to pull up those users that are in the dneero db
                StringBuffer sql = new StringBuffer();
                sql.append(" ( ");
                for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                    FacebookUser facebookUser = iterator.next();
                    sql.append(" facebookuserid='"+UserInputSafe.clean(facebookUser.getUid())+"' ");
                    if(iterator.hasNext()){
                        sql.append(" OR ");
                    }
                }
                sql.append(" ) ");
                List users = HibernateUtil.getSession().createQuery("from User WHERE "+sql).setCacheable(true).list();
                //Now I have a list of all friends from facebook and a list of users who are friends from dneero
                //I need to create lists of those who've taken the survey (and therefore must be dneero users) and a list of those who haven't (and may be dneero users)
                //Iterate all facebook users because they'll fall into one of the two camps
                for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                    FacebookUser facebookUser = iterator.next();
                    //See if this facebookUser is a dneero user and if they've taken the survey
                    boolean isdneerouser = false;
                    int userid = 0;
                    boolean hastakensurvey = false;
                    int responseid = 0;
                    for (Iterator iterator2 = users.iterator(); iterator2.hasNext();) {
                        User user = (User) iterator2.next();
                        if (user.getFacebookuserid()>0 && String.valueOf(user.getFacebookuserid()).equals(facebookUser.getUid())){
                            Blogger blogger = Blogger.get(user.getBloggerid());
                            for (Iterator<Response> iterator1 = blogger.getResponses().iterator(); iterator1.hasNext();) {
                                Response response = iterator1.next();
                                if (response.getSurveyid()==survey.getSurveyid()){
                                    hastakensurvey = true;
                                    responseid = response.getResponseid();
                                    break;
                                }
                            }
                            isdneerouser = true;
                            userid = user.getUserid();
                            break;
                        }
                    }
                    //If they've taken the survey
                    if (hastakensurvey){
                        PublicSurveyFacebookFriendListitem psffli = new PublicSurveyFacebookFriendListitem();
                        psffli.setFacebookUser(facebookUser);
                        psffli.setUserid(userid);
                        psffli.setResponseid(responseid);
                        facebookuserswhotooksurvey.add(psffli);
                    }
                    //Otherwise they've not taken the survey
                    if (!hastakensurvey){
                        facebookuserswhodidnottakesurvey.put(facebookUser.getFirst_name()+" "+facebookUser.getLast_name(), facebookUser.getUid());
                    }
                }
            }
        }
    }


    public String getTakesurveyhtml() {
        return takesurveyhtml;
    }

    public void setTakesurveyhtml(String takesurveyhtml) {
        this.takesurveyhtml=takesurveyhtml;
    }

    public boolean getHaveerror() {
        return haveerror;
    }

    public void setHaveerror(boolean haveerror) {
        this.haveerror=haveerror;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public User getUserwhotooksurvey() {
        return userwhotooksurvey;
    }

    public void setUserwhotooksurvey(User userwhotooksurvey) {
        this.userwhotooksurvey=userwhotooksurvey;
    }

    public boolean getIsuserwhotooksurveysameasloggedinuser() {
        return isuserwhotooksurveysameasloggedinuser;
    }

    public void setIsuserwhotooksurveysameasloggedinuser(boolean isuserwhotooksurveysameasloggedinuser) {
        this.isuserwhotooksurveysameasloggedinuser=isuserwhotooksurveysameasloggedinuser;
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

    public String getSocialbookmarklinks() {
        return socialbookmarklinks;
    }

    public void setSocialbookmarklinks(String socialbookmarklinks) {
        this.socialbookmarklinks=socialbookmarklinks;
    }

    public boolean getQualifiesforsurvey() {
        return qualifiesforsurvey;
    }

    public void setQualifiesforsurvey(boolean qualifiesforsurvey) {
        this.qualifiesforsurvey=qualifiesforsurvey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg=msg;
    }

    public boolean getBloggerhastakentoomanysurveysalreadytoday() {
        return bloggerhastakentoomanysurveysalreadytoday;
    }

    public void setBloggerhastakentoomanysurveysalreadytoday(boolean bloggerhastakentoomanysurveysalreadytoday) {
        this.bloggerhastakentoomanysurveysalreadytoday=bloggerhastakentoomanysurveysalreadytoday;
    }

    public List<PublicSurveyFacebookFriendListitem> getFacebookuserswhotooksurvey() {
        return facebookuserswhotooksurvey;
    }

    public void setFacebookuserswhotooksurvey(List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey) {
        this.facebookuserswhotooksurvey=facebookuserswhotooksurvey;
    }

    public TreeMap<String, String> getFacebookuserswhodidnottakesurvey() {
        return facebookuserswhodidnottakesurvey;
    }

    public void setFacebookuserswhodidnottakesurvey(TreeMap<String, String> facebookuserswhodidnottakesurvey) {
        this.facebookuserswhodidnottakesurvey=facebookuserswhodidnottakesurvey;
    }

    public String getDNEERO_REQUEST_PARAM_IDENTIFIER() {
        return DNEERO_REQUEST_PARAM_IDENTIFIER;
    }

    public void setDNEERO_REQUEST_PARAM_IDENTIFIER(String DNEERO_REQUEST_PARAM_IDENTIFIER) {
        this.DNEERO_REQUEST_PARAM_IDENTIFIER=DNEERO_REQUEST_PARAM_IDENTIFIER;
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

    public String getInvitefriendsurl() {
        return invitefriendsurl;
    }

    public void setInvitefriendsurl(String invitefriendsurl) {
        this.invitefriendsurl=invitefriendsurl;
    }


    public String getSurveyResponseHtml() {
        return surveyResponseHtml;
    }

    public void setSurveyResponseHtml(String surveyResponseHtml) {
        this.surveyResponseHtml=surveyResponseHtml;
    }

    public String getSurveyResponseFlashEmbed() {
        return surveyResponseFlashEmbed;
    }

    public void setSurveyResponseFlashEmbed(String surveyResponseFlashEmbed) {
        this.surveyResponseFlashEmbed=surveyResponseFlashEmbed;
    }

    
}
