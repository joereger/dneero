package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.survey.servlet.*;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.scheduledjobs.SurveydisplayActivityObjectQueue;
import com.dneero.systemprops.BaseUrl;
import com.dneero.session.SurveysTakenToday;
import com.dneero.helpers.UserInputSafe;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.facebook.FacebookUser;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyTake implements Serializable {


    private String takesurveyhtml;
    private boolean haveerror = false;
    private Survey survey;
    private User userwhotooksurvey = null;
    private boolean isuserwhotooksurveysameasloggedinuser;
    private SurveyEnhancer surveyEnhancer;
    private boolean loggedinuserhasalreadytakensurvey;
    private String surveyResponseFlashEmbed;
    private boolean showSurveyResponseFlashEmbed = false;
    private String surveyOnBlogPreview;
    private boolean qualifiesforsurvey = true;
    private String socialbookmarklinks = "";
    private String htmltoposttoblog = "";
    private String htmltoposttoblogflash = "";
    private String htmltoposttoblogflashwithembedandobjecttag = "";
    private String htmltoposttoblogimagelink = "";
    private String htmltoposttobloglink = "";
    private String surveyCriteriaAsHtml;
    private String msg = "";
    private int tabselectedindex = 0;
    private int resultstabselectedindex = 0;
    private boolean bloggerhastakentoomanysurveysalreadytoday = false;
    private String resultsHtml = "";
    private String resultsHtmlForUserWhoTookSurvey = "";
    private String resultsYourFriends = "";
    private String resultsfriendstabtext = "People from site you were at";
    private boolean resultsshowyourfriendstab = false;
    private List<Impression> impressions;
    private List<PublicSurveyDiscussListitem> surveydiscusses;
    private List<PublicSurveyRespondentsListitem> respondents;
    private String discussSubject="";
    private String discussComment="";
    private String DNEERO_REQUEST_PARAM_IDENTIFIER = SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER;
    private boolean surveytakergavetocharity = false;
    private String charityname = "";
    private String[] facebookfriendsselected;
    private String[] facebookfriendsselected2;
    private List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
    private TreeMap<String, String> facebookuserswhodidnottakesurvey = new TreeMap<String, String>();

    public PublicSurveyTake(){
        load();
    }

    private void load(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");

        //Surveyid from session or url
        int surveyid = Jsf.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            surveyid = Integer.parseInt(Jsf.getRequestParam("surveyid"));
        } else if (Num.isinteger(Jsf.getRequestParam("s"))) {
            surveyid = Integer.parseInt(Jsf.getRequestParam("s"));
        }
        Jsf.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            try{Jsf.getHttpServletResponse().sendRedirect("/publicsurveylist.jsf"); return;}catch(Exception ex){logger.error(ex);}
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            try{Jsf.getHttpServletResponse().sendRedirect("/surveynotopen.jsf"); return;}catch(Exception ex){logger.error(ex);}
        }

        //Userid from url
        int userid = 0;
        if (Num.isinteger(Jsf.getRequestParam("userid"))){
            userid = Integer.parseInt(Jsf.getRequestParam("userid"));
        } else if (Num.isinteger(Jsf.getRequestParam("u"))){
            userid = Integer.parseInt(Jsf.getRequestParam("u"));
        }
        Jsf.getUserSession().setPendingSurveyReferredbyuserid(userid);
        Jsf.getUserSession().setReferredbyOnlyUsedForSignup(userid);

        //Set userwhotooksurvey, first verifying that they've actually taken the survey
        userwhotooksurvey = null;
        if (userid>0){
            User userTmp = User.get(userid);
            Blogger blogger = Blogger.get(userTmp.getBloggerid());
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    userwhotooksurvey = userTmp;
                    break;
                }
            }
        }

        //See if logged in user has taken survey yet
        loggedinuserhasalreadytakensurvey = false;
        int responseidOfLoggedinUser = 0;
        if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
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
            userwhotooksurvey = Jsf.getUserSession().getUser();
        }

        //Determine whether the user who's seeing the page is the same person who took it
        if (userwhotooksurvey!=null && Jsf.getUserSession().getIsloggedin() && userwhotooksurvey.getUserid()==Jsf.getUserSession().getUser().getUserid()){
            isuserwhotooksurveysameasloggedinuser = true;
        } else {
            isuserwhotooksurveysameasloggedinuser = false;
        }

        //Responseid
        int responseid = 0;
        if (Num.isinteger(Jsf.getRequestParam("responseid"))){
            responseid=Integer.parseInt(Jsf.getRequestParam("responseid"));
        } else if (Num.isinteger(Jsf.getRequestParam("r"))){
            responseid=Integer.parseInt(Jsf.getRequestParam("r"));
        }

        //Record the impression if we have enough info for it
        if (userid>0 && surveyid>0 && (Jsf.getRequestParam("p")==null || Jsf.getRequestParam("p").equals("1"))){
            RecordImpression.record(Jsf.getHttpServletRequest());
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

        //Criteria for survey
        SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
        surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();

        //Max surveys per day
        if (Jsf.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
            bloggerhastakentoomanysurveysalreadytoday = true;
        } else {
            bloggerhastakentoomanysurveysalreadytoday = false;
        }

        //See if blogger is qualified to take survey
        qualifiesforsurvey = true;
        if (!loggedinuserhasalreadytakensurvey && Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null){
            Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
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

        //Whether to hide the takesurveyhtml.  This is used when clicking on a 

        //If blogger has taken the survey already
        if (loggedinuserhasalreadytakensurvey){
            htmltoposttoblog = SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, false, true, false);
            htmltoposttoblogflash = SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, true, false);
            htmltoposttoblogflashwithembedandobjecttag = SurveyFlashServlet.getEmbedSyntaxWithObjectTag(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false, true, false);
            htmltoposttoblogimagelink = SurveyImageServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
            htmltoposttobloglink = SurveyLinkServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseidOfLoggedinUser, false);
        } else {
            htmltoposttoblog = "";
            htmltoposttoblogflash = "";
            htmltoposttoblogflashwithembedandobjecttag = "";
            htmltoposttoblogimagelink = "";
            htmltoposttobloglink = "";
        }

        //Survey on blog preview
        if (loggedinuserhasalreadytakensurvey){
            surveyOnBlogPreview = "";
        } else {
            int useridTmp = 0;
            if (userwhotooksurvey!=null && userwhotooksurvey.getUserid()>0){
                useridTmp = userwhotooksurvey.getUserid();
            }
            surveyOnBlogPreview = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), useridTmp, responseid, true, true, false);
        }

        //The main survey flash embed
        if (userwhotooksurvey!=null){
            surveyResponseFlashEmbed = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), userwhotooksurvey.getUserid(), responseid, true, true, false);
        }

        //Whether to show the answers for the userwho took the survey
        if (Jsf.getRequestParam("show")!=null && Jsf.getRequestParam("show").equals("showSurveyResponseFlashEmbed")){
            showSurveyResponseFlashEmbed = true;    
        }

        //Turn on the correct tab
        if (survey.getStatus()!=Survey.STATUS_OPEN){
            tabselectedindex = 2;
        }
        if (loggedinuserhasalreadytakensurvey && isuserwhotooksurveysameasloggedinuser){
            tabselectedindex = 1;
        } else {
            tabselectedindex = 0;
        }
        if (Jsf.getRequestParam("show")!=null && Jsf.getRequestParam("show").equals("results")){
            tabselectedindex = 2;
        }
        if (Jsf.getRequestParam("show")!=null && Jsf.getRequestParam("show").equals("disclosure")){
            tabselectedindex = 6;
        }
        if (Jsf.getRequestParam("show")!=null && Jsf.getRequestParam("show").equals("showSurveyResponseFlashEmbed")){
            tabselectedindex = 0;
        }
        if (Jsf.getRequestParam("tabselectedindex")!=null && Num.isinteger(Jsf.getRequestParam("tabselectedindex"))){
            tabselectedindex = Integer.parseInt(Jsf.getRequestParam("tabselectedindex"));
        }

        //Determine which of the results tabs is on
        if (userwhotooksurvey!=null){
            resultstabselectedindex = 1;
        } else {
            resultstabselectedindex = 0;
        }

        //Results main tab
        if (!survey.getIsresultshidden()){
            resultsHtml = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>());
        } else {
            resultsHtml = "<font class=\"smallfont\">The researcher who created this survey has chosen to hide overall aggregate results.  However, dNeero does not allow researchers to hide aggregate results from individual blogs so those results are still available.  To see such results, find a blog that's posted this survey and click the See How Others Voted link... you'll see how others from that blog answered.</font>";
        }

        //Set the results for userwhotooksurvey
        if (userwhotooksurvey!=null){
            resultsHtmlForUserWhoTookSurvey = SurveyResultsDisplay.getHtmlForResults(survey, null, userwhotooksurvey.getUserid(), new ArrayList<Integer>());
        } else {
            //resultsHtmlForUserWhoTookSurvey = "<font class='mediumfont'>Nobody who learned of this survey from "+userwhotooksurvey.getFirstname()+" "+userwhotooksurvey.getLastname()+" has answered... yet.  You could be the first!</font>";
            resultsHtmlForUserWhoTookSurvey = "";
        }

        //Set results friends tab text
        if (userwhotooksurvey!=null){
            resultsfriendstabtext = Str.truncateString(userwhotooksurvey.getFirstname(), 15)+"'s Friends";
        }

        //Special Facebook activities
        if (Jsf.getUserSession().getIsfacebookui()){
            //Load facebook users
            loadFacebookUsers();
            //Generate results
            resultsshowyourfriendstab = true;
            FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
            ArrayList<FacebookUser> friends = faw.getFriends();
            if (friends!=null && friends.size()>0){
                StringBuffer facebookquery = new StringBuffer();
                facebookquery.append(" ( ");
                for (Iterator it = friends.iterator(); it.hasNext(); ) {
                    FacebookUser facebookUser = (FacebookUser)it.next();
                    facebookquery.append("facebookuserid="+facebookUser.getUid());
                    if (it.hasNext()){
                        facebookquery.append(" OR ");
                    }
                }
                facebookquery.append(" ) ");
                ArrayList<Integer> onlyincluderesponsesfromtheseuserids = new ArrayList<Integer>();
                List fbusers = HibernateUtil.getSession().createQuery("from User WHERE "+facebookquery.toString()).list();
                for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
                    User fbuser = (User) iterator.next();
                    onlyincluderesponsesfromtheseuserids.add(fbuser.getUserid());
                }
                resultsYourFriends = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, onlyincluderesponsesfromtheseuserids);
            } else {
                resultsYourFriends = "<font class='mediumfont'>None of your friends have taken this survey... yet.</font>";
            }
        }

        //Load impressions
        impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+survey.getSurveyid()+"' and referer<>'' order by impressionstotal desc").setCacheable(true).list();

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
        if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
            if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), survey)){
                Jsf.setFacesMessage("Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey.");
                return "publicsurvey";
            }
        }

        //If the user's already taken too many surveys
        if (Jsf.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
            Jsf.setFacesMessage("Sorry, you've already taken the maximum number of surveys today ("+SurveysTakenToday.MAXSURVEYSPERDAY+").  Wait until tomorrow (defined in U.S. Eastern Standard Time) and try again.");
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
        if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()<=0){
            //Pending survey save
            //Note: this code also on Login and Registration
            Responsepending responsepending = new Responsepending();
            responsepending.setUserid(Jsf.getUserSession().getUser().getUserid());
            responsepending.setResponseasstring(Jsf.getUserSession().getPendingSurveyResponseAsString());
            responsepending.setReferredbyuserid(Jsf.getUserSession().getPendingSurveyReferredbyuserid());
            responsepending.setSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
            try{responsepending.save();}catch(Exception ex){logger.error(ex);}
            Jsf.getUserSession().setPendingSurveyResponseSurveyid(0);
            Jsf.getUserSession().setPendingSurveyReferredbyuserid(0);
            Jsf.getUserSession().setPendingSurveyResponseAsString("");

        }

        //If the user's logged in and is a blogger
        if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
            try{
                BloggerIndex.storeResponseInDb(survey, srp, blogger, Jsf.getUserSession().getPendingSurveyReferredbyuserid());
            }catch (ComponentException cex){
                haveerror = true;
                Jsf.setFacesMessage(cex.getErrorsAsSingleString());
                return "publicsurvey";
            }catch(Exception ex){
                logger.error(ex);
            }
        }

        //Where to send afterwards?
        if (Jsf.getUserSession().getIsloggedin()){
            if (Jsf.getUserSession().getUser().getBloggerid()>0){
                load();
                try{Jsf.redirectResponse("/survey.jsf?surveyid="+survey.getSurveyid());}catch(Exception ex){logger.error(ex);}
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
        Logger logger = Logger.getLogger(PublicSurveyTake.class);
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
            if (Jsf.getUserSession()!=null){
                //Store this response in memory for now
                Jsf.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Jsf.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing survey response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){
            throw allCex;
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
            tabselectedindex = 4;
            return null;
        }
        if (Jsf.getUserSession().getIsloggedin()){
            Surveydiscuss surveydiscuss = new Surveydiscuss();
            surveydiscuss.setSurveyid(survey.getSurveyid());
            surveydiscuss.setIsapproved(true);
            surveydiscuss.setSubject(UserInputSafe.clean(discussSubject));
            surveydiscuss.setComment(UserInputSafe.clean(discussComment));
            surveydiscuss.setDate(new Date());
            surveydiscuss.setUserid(Jsf.getUserSession().getUser().getUserid());
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
        tabselectedindex = 4;
        //Return from survey new comment in a way that retains the survey url
        try{Jsf.getHttpServletResponse().sendRedirect("/survey.jsf?surveyid="+Jsf.getUserSession().getCurrentSurveyid()+"&tabselectedindex=4"); return null;}catch(Exception ex){logger.error(ex);}
        return "publicsurvey";
    }

    public String tellFriends(){
        return tellFriendsOperation(facebookfriendsselected);
    }

    public String tellFriends2(){
        return tellFriendsOperation(facebookfriendsselected2);
    }

    private String tellFriendsOperation(String[] friendstotell){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (friendstotell!=null && friendstotell.length>0){
            int numberinvited = 0;
            ArrayList<Integer> uids = new ArrayList<Integer>();
            for (int i = 0; i < friendstotell.length; i++) {
                String uid = friendstotell[i];
                if (Num.isinteger(uid)){
                    numberinvited = numberinvited + 1;
                    logger.debug("Facebookfriend to invite, uid="+uid);
                    if (numberinvited<=10){
                        uids.add(Integer.parseInt(uid));
                    }
                }
            }
            FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
            faw.inviteFriendsToSurvey(uids, survey);
        }
        try{Jsf.getHttpServletResponse().sendRedirect("/survey.jsf?surveyid="+survey.getSurveyid()); return null;}catch(Exception ex){logger.error(ex);}
        return "publicsurvey";
    }

    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
        facebookuserswhodidnottakesurvey = new TreeMap<String, String>();
        if (survey!=null){
            //Go to facebook and get a list of the logged-in user's friends
            FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
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

    public String getTakesurveyhtml() {
        return takesurveyhtml;
    }

    public void setTakesurveyhtml(String takesurveyhtml) {
        this.takesurveyhtml = takesurveyhtml;
    }

    public boolean getHaveerror() {
        return haveerror;
    }

    public void setHaveerror(boolean haveerror) {
        this.haveerror = haveerror;
    }

    public String getSocialbookmarklinks() {
        return socialbookmarklinks;
    }

    public void setSocialbookmarklinks(String socialbookmarklinks) {
        this.socialbookmarklinks = socialbookmarklinks;
    }

    public String getSurveyCriteriaAsHtml() {
        return surveyCriteriaAsHtml;
    }

    public void setSurveyCriteriaAsHtml(String surveyCriteriaAsHtml) {
        this.surveyCriteriaAsHtml = surveyCriteriaAsHtml;
    }

    public boolean getLoggedinuserhasalreadytakensurvey() {
        return loggedinuserhasalreadytakensurvey;
    }

    public void setLoggedinuserhasalreadytakensurvey(boolean loggedinuserhasalreadytakensurvey) {
        this.loggedinuserhasalreadytakensurvey = loggedinuserhasalreadytakensurvey;
    }

    public String getSurveyResponseFlashEmbed() {
        return surveyResponseFlashEmbed;
    }

    public void setSurveyResponseFlashEmbed(String surveyResponseFlashEmbed) {
        this.surveyResponseFlashEmbed = surveyResponseFlashEmbed;
    }

    public String getSurveyOnBlogPreview() {
        return surveyOnBlogPreview;
    }

    public void setSurveyOnBlogPreview(String surveyOnBlogPreview) {
        this.surveyOnBlogPreview = surveyOnBlogPreview;
    }

    public boolean getQualifiesforsurvey() {
        return qualifiesforsurvey;
    }

    public void setQualifiesforsurvey(boolean qualifiesforsurvey) {
        this.qualifiesforsurvey = qualifiesforsurvey;
    }

    public String getHtmltoposttoblog() {
        return htmltoposttoblog;
    }

    public void setHtmltoposttoblog(String htmltoposttoblog) {
        this.htmltoposttoblog = htmltoposttoblog;
    }

    public String getHtmltoposttoblogflash() {
        return htmltoposttoblogflash;
    }

    public void setHtmltoposttoblogflash(String htmltoposttoblogflash) {
        this.htmltoposttoblogflash = htmltoposttoblogflash;
    }

    public String getHtmltoposttoblogimagelink() {
        return htmltoposttoblogimagelink;
    }

    public void setHtmltoposttoblogimagelink(String htmltoposttoblogimagelink) {
        this.htmltoposttoblogimagelink = htmltoposttoblogimagelink;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getTabselectedindex() {
        return tabselectedindex;
    }

    public void setTabselectedindex(int tabselectedindex) {
        this.tabselectedindex = tabselectedindex;
    }

    public boolean getBloggerhastakentoomanysurveysalreadytoday() {
        return bloggerhastakentoomanysurveysalreadytoday;
    }

    public void setBloggerhastakentoomanysurveysalreadytoday(boolean bloggerhastakentoomanysurveysalreadytoday) {
        this.bloggerhastakentoomanysurveysalreadytoday = bloggerhastakentoomanysurveysalreadytoday;
    }


    public String getResultsHtml() {
        return resultsHtml;
    }

    public void setResultsHtml(String resultsHtml) {
        this.resultsHtml = resultsHtml;
    }

    public String getResultsHtmlForUserWhoTookSurvey() {
        return resultsHtmlForUserWhoTookSurvey;
    }

    public void setResultsHtmlForUserWhoTookSurvey(String resultsHtmlForUserWhoTookSurvey) {
        this.resultsHtmlForUserWhoTookSurvey = resultsHtmlForUserWhoTookSurvey;
    }

    public int getResultstabselectedindex() {
        return resultstabselectedindex;
    }

    public void setResultstabselectedindex(int resultstabselectedindex) {
        this.resultstabselectedindex = resultstabselectedindex;
    }

    public List<Impression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<Impression> impressions) {
        this.impressions = impressions;
    }

    public String getDiscussSubject() {
        return discussSubject;
    }

    public void setDiscussSubject(String discussSubject) {
        this.discussSubject = discussSubject;
    }

    public String getDiscussComment() {
        return discussComment;
    }

    public void setDiscussComment(String discussComment) {
        this.discussComment = discussComment;
    }


    public List<PublicSurveyDiscussListitem> getSurveydiscusses() {
        return surveydiscusses;
    }

    public void setSurveydiscusses(List<PublicSurveyDiscussListitem> surveydiscusses) {
        this.surveydiscusses = surveydiscusses;
    }

    public String getHtmltoposttobloglink() {
        return htmltoposttobloglink;
    }

    public void setHtmltoposttobloglink(String htmltoposttobloglink) {
        this.htmltoposttobloglink = htmltoposttobloglink;
    }

    public String getHtmltoposttoblogflashwithembedandobjecttag() {
        return htmltoposttoblogflashwithembedandobjecttag;
    }

    public void setHtmltoposttoblogflashwithembedandobjecttag(String htmltoposttoblogflashwithembedandobjecttag) {
        this.htmltoposttoblogflashwithembedandobjecttag = htmltoposttoblogflashwithembedandobjecttag;
    }

    public String getDNEERO_REQUEST_PARAM_IDENTIFIER() {
        return DNEERO_REQUEST_PARAM_IDENTIFIER;
    }

    public void setDNEERO_REQUEST_PARAM_IDENTIFIER(String DNEERO_REQUEST_PARAM_IDENTIFIER) {
        this.DNEERO_REQUEST_PARAM_IDENTIFIER = DNEERO_REQUEST_PARAM_IDENTIFIER;
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

    public List<PublicSurveyRespondentsListitem> getRespondents() {
        return respondents;
    }

    public void setRespondents(List<PublicSurveyRespondentsListitem> respondents) {
        this.respondents = respondents;
    }


    public String[] getFacebookfriendsselected() {
        return facebookfriendsselected;
    }

    public void setFacebookfriendsselected(String[] facebookfriendsselected) {
        this.facebookfriendsselected = facebookfriendsselected;
    }

    public String getResultsfriendstabtext() {
        return resultsfriendstabtext;
    }

    public void setResultsfriendstabtext(String resultsfriendstabtext) {
        this.resultsfriendstabtext = resultsfriendstabtext;
    }

    public boolean getResultsshowyourfriendstab() {
        return resultsshowyourfriendstab;
    }

    public void setResultsshowyourfriendstab(boolean resultsshowyourfriendstab) {
        this.resultsshowyourfriendstab = resultsshowyourfriendstab;
    }

    public String getResultsYourFriends() {
        return resultsYourFriends;
    }

    public void setResultsYourFriends(String resultsYourFriends) {
        this.resultsYourFriends = resultsYourFriends;
    }


    public boolean getShowSurveyResponseFlashEmbed() {
        return showSurveyResponseFlashEmbed;
    }

    public void setShowSurveyResponseFlashEmbed(boolean showSurveyResponseFlashEmbed) {
        this.showSurveyResponseFlashEmbed = showSurveyResponseFlashEmbed;
    }

    public String[] getFacebookfriendsselected2() {
        return facebookfriendsselected2;
    }

    public void setFacebookfriendsselected2(String[] facebookfriendsselected2) {
        this.facebookfriendsselected2 = facebookfriendsselected2;
    }

    public List<PublicSurveyFacebookFriendListitem> getFacebookuserswhotooksurvey() {
        return facebookuserswhotooksurvey;
    }

    public void setFacebookuserswhotooksurvey(List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey) {
        this.facebookuserswhotooksurvey = facebookuserswhotooksurvey;
    }

    public TreeMap<String, String> getFacebookuserswhodidnottakesurvey() {
        return facebookuserswhodidnottakesurvey;
    }

    public void setFacebookuserswhodidnottakesurvey(TreeMap<String, String> facebookuserswhodidnottakesurvey) {
        this.facebookuserswhodidnottakesurvey = facebookuserswhodidnottakesurvey;
    }
}
