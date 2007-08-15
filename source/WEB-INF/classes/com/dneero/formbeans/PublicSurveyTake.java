package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.util.GeneralException;
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

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyTake implements Serializable {


    private String html;
    private boolean haveerror = false;
    private Survey survey;
    private User userwhotooksurvey = null;
    private boolean isuserwhotooksurveyloaded = false;
    private boolean isuserwhotooksurveysameasloggedinuser;
    private SurveyEnhancer surveyEnhancer;
    private String surveyForTakers;
    private boolean bloggerhasalreadytakensurvey;
    private String surveyAnswersForThisBlogger;
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
    private String resultsHtmlForReferredByBlog = "";
    private boolean isreferredbyblog = false;
    private List<Impression> impressions;
    private List<PublicSurveyDiscussListitem> surveydiscusses;
    private List<PublicSurveyRespondentsListitem> respondents;
    private String discussSubject="";
    private String discussComment="";
    private String DNEERO_REQUEST_PARAM_IDENTIFIER = SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER;
    private boolean surveytakergavetocharity = false;
    private String charityname = "";
    private String[] facebookfriendsselected;

    public PublicSurveyTake(){
        load();
    }

//    public String beginView(){
////        Logger logger = Logger.getLogger(this.getClass().getName());
////        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
////            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
////            logger.debug("surveyid found: "+Jsf.getRequestParam("surveyid"));
////        }
////        if (Jsf.getUserSession().getCurrentSurveyid()>0){
////            try{Jsf.getHttpServletResponse().sendRedirect("/surveytake.jsf?surveyid="+Jsf.getUserSession().getCurrentSurveyid()); return null;}catch(Exception ex){logger.error(ex);}
////        }
//        return "publicsurveytake";
//    }
//


    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");

        //Make sure we have a surveyid and set the current usersession to this surveyid
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
            logger.debug("surveyid found: "+Jsf.getRequestParam("surveyid"));
        }
        if (Jsf.getUserSession().getCurrentSurveyid()<=0){
            try{Jsf.getHttpServletResponse().sendRedirect("/publicsurveylist.jsf"); return;}catch(Exception ex){logger.error(ex);}
        }
        if (Num.isinteger(Jsf.getRequestParam("userid"))){
            Jsf.getUserSession().setPendingSurveyReferredbyuserid(Integer.parseInt(Jsf.getRequestParam("userid")));
        }



        //Go about the fancy business of loading this bean
        survey = new Survey();
        if(Jsf.getUserSession().getCurrentSurveyid()>0){
            logger.debug("Jsf.getUserSession().getCurrentSurveyid()>0");
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());



            
            //Find the person who took the survey
            if (Num.isinteger(Jsf.getRequestParam("userid"))){
                logger.debug("userid found: "+Jsf.getRequestParam("userid"));
                userwhotooksurvey = User.get(Integer.parseInt(Jsf.getRequestParam("userid")));
                isuserwhotooksurveyloaded = true;
                Blogger bloggerwhotook = Blogger.get(userwhotooksurvey.getUserid());
                for (Iterator<Response> iterator = bloggerwhotook.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        if (response.getIsforcharity()){
                            surveytakergavetocharity = true;
                            charityname = response.getCharityname();
                        }
                    }
                }
            }

            //Determine whether the user who's seeing the page is the same person who took it
            if (userwhotooksurvey!=null && Jsf.getUserSession().getIsloggedin() && userwhotooksurvey.getUserid()==Jsf.getUserSession().getUser().getUserid()){
                isuserwhotooksurveysameasloggedinuser = true;
            } else {
                isuserwhotooksurveysameasloggedinuser = false;
            }





            //If the survey is draft or waiting
            if (survey.getStatus()<Survey.STATUS_OPEN){
                try{Jsf.getHttpServletResponse().sendRedirect("/surveynotopen.jsf"); return;}catch(Exception ex){logger.error(ex);}
            }
            socialbookmarklinks = SocialBookmarkLinks.getSocialBookmarkLinks(survey);
            surveyEnhancer = new SurveyEnhancer(survey);
            html = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
            SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
            surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
            if (Jsf.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
                bloggerhastakentoomanysurveysalreadytoday = true;
            } else {
                bloggerhastakentoomanysurveysalreadytoday = false;
            }


            //See if blogger is qualified to take
            bloggerhasalreadytakensurvey = false;
            int responseid = 0;
            if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
                Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
                for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        bloggerhasalreadytakensurvey = true;
                        responseid = response.getResponseid();
                        //Now override any url-line userid=X that we see
                        userwhotooksurvey = Jsf.getUserSession().getUser();
                        isuserwhotooksurveysameasloggedinuser = true;
                        isuserwhotooksurveyloaded = true;
                    }
                }
                if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
                    qualifiesforsurvey = false;
                }
            }

            //If blogger has taken the survey already
            if (bloggerhasalreadytakensurvey){
                surveyAnswersForThisBlogger = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, true, true, false);
                htmltoposttoblog = SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, false, false, true, false);
                htmltoposttoblogflash = SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, false, true, false);
                htmltoposttoblogflashwithembedandobjecttag = SurveyFlashServlet.getEmbedSyntaxWithObjectTag(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, false, true, false);
                htmltoposttoblogimagelink = SurveyImageServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, false);
                htmltoposttobloglink = SurveyLinkServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), responseid, false);
            } else {
                int useridTmp = 0;
                if (userwhotooksurvey!=null && userwhotooksurvey.getUserid()>0){
                    useridTmp = userwhotooksurvey.getUserid();
                }
                surveyOnBlogPreview = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), useridTmp, responseid, true, true, false);
            }
            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);




            //Turn on the correct tab
            if (survey.getStatus()!=Survey.STATUS_OPEN){
                tabselectedindex = 2;    
            }
            if (bloggerhasalreadytakensurvey){
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
            if (Jsf.getRequestParam("tabselectedindex")!=null && Num.isinteger(Jsf.getRequestParam("tabselectedindex"))){
                tabselectedindex = Integer.parseInt(Jsf.getRequestParam("tabselectedindex"));
            }


            //Establish pendingSurveyReferredbyuserid
            User user = null;
            if (Jsf.getRequestParam("userid")!=null && Num.isinteger(Jsf.getRequestParam("userid"))){
                user = User.get(Integer.parseInt(Jsf.getRequestParam("userid")));
                isreferredbyblog = true;
                Jsf.getUserSession().setPendingSurveyReferredbyuserid(user.getUserid());
            }
            logger.debug("isreferredbyblog="+isreferredbyblog);
            if (!survey.getIsresultshidden()){
                resultsHtml = SurveyResultsDisplay.getHtmlForResults(survey, null, 0);
            } else {
                resultsHtml = "<font class=\"smallfont\">This researcher has chosen to hide overall aggregate results.  However, dNeero does not allow researchers to hide aggregate results from individual blogs so those results are still available.  To see such results, find a blog that's posted this survey and click the See How Others Voted link... you'll see how others from that blog answered.</font>";
            }
            if (isreferredbyblog){
                resultsHtmlForReferredByBlog = SurveyResultsDisplay.getHtmlForResults(survey, null, Jsf.getUserSession().getPendingSurveyReferredbyuserid());
                resultstabselectedindex = 1;
            } else {
                resultsHtmlForReferredByBlog = "<font class='mediumfont'>Nobody who has clicked from the blog you were just at has answered... yet.  You could be the first!</font>";
                resultstabselectedindex = 0;
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
        } else {
            logger.debug("Jsf.getUserSession().getCurrentSurveyid()<=0 ---");
        }



        //Establish user referral in case of signup
        if (Num.isinteger(Jsf.getRequestParam("userid"))){
            Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(Jsf.getRequestParam("userid")));
        }
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
                //return "accountindex";
            }
        }
        return "publicsurveytakefinished";
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
        Logger logger = Logger.getLogger(this.getClass().getName());

        if (facebookfriendsselected!=null && facebookfriendsselected.length>0){
            int numberinvited = 0;
            ArrayList<Integer> uids = new ArrayList<Integer>();
            for (int i = 0; i < facebookfriendsselected.length; i++) {
                String uid = facebookfriendsselected[i];
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

    public TreeMap getFacebookallfriends(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        TreeMap tm = new TreeMap();
        FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
        ArrayList<FacebookUser> friends = faw.getFriends();
        for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
            FacebookUser facebookUser = iterator.next();
            tm.put(facebookUser.getFirst_name()+" "+facebookUser.getLast_name(), facebookUser.getUid());
        }
        return tm;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public String getSurveyForTakers() {
        return surveyForTakers;
    }

    public void setSurveyForTakers(String surveyForTakers) {
        this.surveyForTakers = surveyForTakers;
    }

    public boolean getBloggerhasalreadytakensurvey() {
        return bloggerhasalreadytakensurvey;
    }

    public void setBloggerhasalreadytakensurvey(boolean bloggerhasalreadytakensurvey) {
        this.bloggerhasalreadytakensurvey = bloggerhasalreadytakensurvey;
    }

    public String getSurveyAnswersForThisBlogger() {
        return surveyAnswersForThisBlogger;
    }

    public void setSurveyAnswersForThisBlogger(String surveyAnswersForThisBlogger) {
        this.surveyAnswersForThisBlogger = surveyAnswersForThisBlogger;
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

    public String getResultsHtmlForReferredByBlog() {
        return resultsHtmlForReferredByBlog;
    }

    public void setResultsHtmlForReferredByBlog(String resultsHtmlForReferredByBlog) {
        this.resultsHtmlForReferredByBlog = resultsHtmlForReferredByBlog;
    }


    public boolean getIsreferredbyblog() {
        return isreferredbyblog;
    }

    public void setIsreferredbyblog(boolean isreferredbyblog) {
        this.isreferredbyblog = isreferredbyblog;
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

    public boolean getIsuserwhotooksurveyloaded() {
        return isuserwhotooksurveyloaded;
    }

    public void setIsuserwhotooksurveyloaded(boolean isuserwhotooksurveyloaded) {
        this.isuserwhotooksurveyloaded = isuserwhotooksurveyloaded;
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
}
