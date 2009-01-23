package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.components.Dropdown;
import com.dneero.display.components.Essay;
import com.dneero.display.components.Textbox;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.facebook.FacebookPendingReferrals;
import com.dneero.facebook.FacebookUser;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.helpers.StoreResponse;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.PaymentMethod;
import com.dneero.scheduledjobs.SurveydisplayActivityObjectQueue;
import com.dneero.session.SurveysTakenToday;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.survey.servlet.SurveyFlashServlet;
import com.dneero.survey.servlet.SurveydisplayActivityObject;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.RandomString;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.sir.SocialInfluenceRating;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.*;


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
    private String surveyResponseHtml;
    private String surveyResponseFlashEmbed;
    private ArrayList<Question> userquestionsthatmustbeanswered = new ArrayList<Question>();
    private ArrayList<PublicSurveyUserquestionListitem> userquestionlistitems = new ArrayList<PublicSurveyUserquestionListitem>();
    private ArrayList<Question> optionaluserquestions = new ArrayList<Question>();
    private ArrayList<PublicSurveyUserquestionListitem> optionaluserquestionlistitems = new ArrayList<PublicSurveyUserquestionListitem>();
    private ArrayList<Integer> useridswhohavebeencheckedforuserquestions = new ArrayList<Integer>();
    private Response response = null;
    private String yourquestion = "";
    private boolean yourquestionismultiplechoice = true;
    private boolean yourquestionisshorttext= false;
    private boolean yourquestionislongtext = false;
    private ArrayList<String> yourquestionmultiplechoiceoptions = new ArrayList<String>();

    public PublicSurvey(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

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

        //Responseid
        int responseid = 0;

        //Responseid from userwhotooksurvey
        if(userwhotooksurvey!=null && loggedinuserhasalreadytakensurvey){
            responseid = responseidOfLoggedinUser;
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


        //Turn on the correct tab
        if (survey.getStatus()!=Survey.STATUS_OPEN){
            //redirect to results
            //try{Pagez.sendRedirect("/surveyresults.jsp?surveyid="+surveyid); return;}catch(Exception ex){logger.error("",ex);}
        }
        if (loggedinuserhasalreadytakensurvey && isuserwhotooksurveysameasloggedinuser){
            //redirect to postit
            //try{Pagez.sendRedirect("/surveypostit.jsp?surveyid="+surveyid); return;}catch(Exception ex){logger.error("",ex);}
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
        logger.debug("calling new SurveyEnhancer(survey) from PublicSurvey");
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

        //The main survey flash embed
        if (userwhotooksurvey!=null){
            surveyResponseFlashEmbed = SurveyFlashServlet.getEmbedSyntax("/", survey.getSurveyid(), userwhotooksurvey.getUserid(), responseid, survey.getPlid(), true, true, false);
        }

        //To display to those looking to take survey
        if (!loggedinuserhasalreadytakensurvey){
            takesurveyhtml = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true, userwhotooksurvey);
        } else if (Pagez.getUserSession().getUser()!=null) {
            takesurveyhtml = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, Blogger.get(Pagez.getUserSession().getUser().getBloggerid()), true, userwhotooksurvey);
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

        }

        //Get userquestions
        userquestionlistitems = new ArrayList<PublicSurveyUserquestionListitem>();
        if (userwhotooksurvey!=null){
            userquestionsthatmustbeanswered = findUserQuestionsFor(userwhotooksurvey);
            for (Iterator<Question> uqIter=userquestionsthatmustbeanswered.iterator(); uqIter.hasNext();) {
                Question question=uqIter.next();
                if (question.getIsuserquestion() && question.getUserid()>0){
                    PublicSurveyUserquestionListitem psli = new PublicSurveyUserquestionListitem();
                    psli.setQuestion(question);
                    psli.setUser(User.get(question.getUserid()));
                    Blogger blogger = null;
                    if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                        blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                    }
                    psli.setComponent(ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger));
                    userquestionlistitems.add(psli);
                }
            }
        }

        //Get optional userquestions
        optionaluserquestionlistitems = new ArrayList<PublicSurveyUserquestionListitem>();
        optionaluserquestions = findOptionalUserQuestionsFor(userquestionsthatmustbeanswered);
        for (Iterator<Question> uqIter=optionaluserquestions.iterator(); uqIter.hasNext();) {
            Question question=uqIter.next();
            if (question.getIsuserquestion() && question.getUserid()>0){
                PublicSurveyUserquestionListitem psli = new PublicSurveyUserquestionListitem();
                psli.setQuestion(question);
                psli.setUser(User.get(question.getUserid()));
                Blogger blogger = null;
                if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                    blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                }
                psli.setComponent(ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger));
                optionaluserquestionlistitems.add(psli);
            }
        }

        //Prepop your user question
        prePopulateYourQuestion();

        //Record the survey display using the display cache
        SurveydisplayActivityObject sdao = new SurveydisplayActivityObject();
        sdao.setSurveyid(survey.getSurveyid());
        SurveydisplayActivityObjectQueue.addSdao(sdao);


    }

    private void prePopulateYourQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("start prePopulateYourQuestion()");
        if (loggedinuserhasalreadytakensurvey){
            for (Iterator<Question> iterator=survey.getQuestions().iterator(); iterator.hasNext();) {
                Question question=iterator.next();
                if (question.getIsuserquestion()){
                    if (Pagez.getUserSession().getUser().getUserid()==question.getUserid()){
                        //Get the main question
                        yourquestion = question.getQuestion();
                        logger.debug("question.getComponenttype()="+question.getComponenttype());
                        //If it's multiple choice
                        if (question.getComponenttype()==Dropdown.ID){
                            yourquestionismultiplechoice =  true;
                            yourquestionmultiplechoiceoptions = new ArrayList<String>();
                            String options = "";
                            for (Iterator<Questionconfig> it = question.getQuestionconfigs().iterator(); it.hasNext();) {
                                Questionconfig questionconfig = it.next();
                                if (questionconfig.getName().equals("options")){
                                    options = questionconfig.getValue();
                                }
                            }
                            String[] optionsSplit = options.split("\\n");
                            for (int i = 0; i < optionsSplit.length; i++) {
                                String s = optionsSplit[i];
                                yourquestionmultiplechoiceoptions.add(s.trim());
                            }
                        } else {
                            yourquestionismultiplechoice =  false;
                            yourquestionmultiplechoiceoptions = new ArrayList<String>();
                        }
                        //If it's a short text
                        if (question.getComponenttype()==Textbox.ID){
                            yourquestionisshorttext =  true;
                        } else {
                            yourquestionisshorttext =  false;
                        }
                        //If it's a long text
                        if (question.getComponenttype()==Essay.ID){
                            yourquestionislongtext =  true;
                        } else {
                            yourquestionislongtext =  false;
                        }
                    }
                }
            }
        }
        logger.debug("end prePopulateYourQuestion()");
    }


    public void takeSurvey() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("takeSurvey() called");
        survey = Survey.get(survey.getSurveyid());

        //If the user's logged in and is a blogger make sure they're qualified for this survey
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(Blogger.get(Pagez.getUserSession().getUser().getBloggerid()), survey)){
                vex.addValidationError("Sorry, you're not qualified to join this conversation.  Your qualification is determined by your Profile.  Conversation igniters determine their intended audience when they create a conversation.");
                throw vex;
            }
        }

        //If the user's already taken too many surveys
        if (!loggedinuserhasalreadytakensurvey && Pagez.getUserSession().getSurveystakentoday()>SurveysTakenToday.MAXSURVEYSPERDAY){
            vex.addValidationError("Sorry, you've already joined the maximum number of conversations today ("+SurveysTakenToday.MAXSURVEYSPERDAY+").  Wait until tomorrow (defined in U.S. Eastern Standard Time) and try again.");
            throw vex;
        }

        //If the survey requires an accesscode
        if (survey.getIsaccesscodeonly()){
            //If this is the first take
            if (!loggedinuserhasalreadytakensurvey){
                //If the access code isn't there
                if (Pagez.getUserSession().getAccesscode()==null || !Pagez.getUserSession().getAccesscode().equalsIgnoreCase(survey.getAccesscode())){
                    vex.addValidationError("Sorry, this conversation requires an Access Code.");
                    throw vex;
                }
            }
        }


        //Validate the response and put into memory
        SurveyResponseParser srp;
        try{
            srp = new SurveyResponseParser(Pagez.getRequest());
            createResponse(survey, srp, null);
            haveerror = false;
        } catch (ComponentException cex){
            haveerror = true;
            vex.addValidationError(cex.getErrorsAsSingleString());
            throw vex;
        }

        //Do Facebook stuff
        createFacebookUserIfNecessary();

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
            Pagez.getUserSession().setPendingSurveyResponseAsString("");
        }

        //If the user's logged in and is a blogger
        if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            try{
                StoreResponse.storeResponseInDb(survey, srp, blogger, Pagez.getUserSession().getPendingSurveyReferredbyuserid());
                Pagez.getUserSession().setPendingSurveyResponseSurveyid(0);
                Pagez.getUserSession().setPendingSurveyResponseAsString("");
            }catch (ComponentException cex){
                haveerror = true;
                vex.addValidationError(cex.getErrorsAsSingleString());
                throw vex;
            }catch(Exception ex){
                logger.error("",ex);
            }
        }
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
                logger.debug("Storing response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){
            throw allCex;
        }
    }

    private void createFacebookUserIfNecessary(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Start Facebook shenanigans
        if (Pagez.getUserSession().getIsfacebookui()){
            int facebookuserid = 0;
            if (Pagez.getUserSession().getFacebookUser()!=null && Num.isinteger(Pagez.getUserSession().getFacebookUser().getUid())){
                facebookuserid = Integer.parseInt(Pagez.getUserSession().getFacebookUser().getUid());
            }
            if (facebookuserid>0){
                User user = new User();
                //Check to see if we already have this facebookuserid in the database
                List<User> usersWithSameFacebookid = HibernateUtil.getSession().createCriteria(User.class)
                                                   .add(Restrictions.eq("facebookuserid", facebookuserid))
                                                   .setCacheable(true)
                                                   .list();
                //Just a little runtime error logging
                if (usersWithSameFacebookid!=null && usersWithSameFacebookid.size()>1){
                    logger.error("More than one user with facebookuserid="+facebookuserid);
                }
                //Find the user or create them
                if (usersWithSameFacebookid!=null && usersWithSameFacebookid.size()>0){
                    //Grab the first user in the list
                    user = usersWithSameFacebookid.get(0);
                } else {
                    //No user exists so I need to auto-create one
                    user.setEmail("");
                    user.setPlid(Pagez.getUserSession().getPl().getPlid());
                    user.setPassword("");
                    user.setFirstname(Pagez.getUserSession().getFacebookUser().getFirst_name());
                    user.setLastname(Pagez.getUserSession().getFacebookUser().getLast_name());
                    user.setIsactivatedbyemail(true);  //Auto-activated by email... done because user will have to enter email in account settings
                    user.setIsqualifiedforrevshare(true);
                    user.setReferredbyuserid(FacebookPendingReferrals.getReferredbyUserid(facebookuserid));
                    user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
                    user.setEmailactivationlastsent(new Date());
                    user.setCreatedate(new Date());
                    user.setPaymethodpaypaladdress("");
                    user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
                    user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
                    user.setPaymethodcreditcardid(0);
                    user.setChargemethodcreditcardid(0);
                    user.setBloggerid(0);
                    user.setResearcherid(0);
                    user.setNotifyofnewsurveysbyemaileveryexdays(1);
                    user.setNotifyofnewsurveyslastsent(new Date());
                    user.setAllownoncriticalemails(true);
                    user.setInstantnotifybyemailison(false);
                    user.setInstantnotifybytwitterison(false);
                    user.setInstantnotifytwitterusername("");
                    user.setInstantnotifyxmppison(false);
                    user.setInstantnotifyxmppusername("");
                    user.setIsenabled(true);
                    user.setFacebookuserid(facebookuserid);
                    user.setFacebookappremoveddate(new Date());
                    user.setIsfacebookappremoved(false);
                    user.setResellercode(RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7));
                    user.setResellerpercent(0.0);
                    user.setCurrentbalance(0.0);
                    user.setCurrentbalanceblogger(0.0);
                    user.setCurrentbalanceresearcher(0.0);
                    user.setLastlogindate(new java.util.Date());
                    user.setNickname("");
                    user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
                    user.setSirdate(new Date());
                    user.setSirdebug("");
                    user.setSirpoints(0.0);
                    user.setSirrank(0);
                    try{
                        user.save();
                    } catch (GeneralException gex){
                        logger.debug("Facebook auto-register failed: " + gex.getErrorsAsSingleString());
                        return;
                    }

                    //Notify customer care group
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New dNeero User via Facebook: "+ user.getFirstname() + " " + user.getLastname());
                    xmpp.send();
                }

                //Setup the userSession to be logged in now
                Pagez.getUserSession().setUser(user);
                Pagez.getUserSession().setIsloggedin(true);
                Pagez.getUserSession().setIsLoggedInToBeta(true);
                Pagez.getUserSession().setIseulaok(true);
                Pagez.setUserSessionAndUpdateCache(Pagez.getUserSession());
            }
        }
        //End Facebook shenanigans
    }



    

    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
        facebookuserswhodidnottakesurvey = new TreeMap<String, String>();
        if (survey!=null){
            //Go to facebook and get a list of the logged-in user's friends
            //FacebookApiWrapperHtmlui faw = new FacebookApiWrapperHtmlui(Pagez.getUserSession());
            ArrayList<FacebookUser> friends = Pagez.getUserSession().getFacebookFriends();
            if (friends!=null && friends.size()>0){
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

    private ArrayList<Question> findUserQuestionsFor(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("findUserQuestionsFor(userid="+user.getUserid()+" name="+user.getFirstname()+" "+user.getLastname()+")");
        ArrayList<Question> userquestionsthatmustbeanswered = new ArrayList<Question>();
        for (Iterator<Question> iterator=survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question=iterator.next();
            if (question.getIsuserquestion()){
                if (user.getUserid()==question.getUserid()){
                    logger.debug("adding questionid="+question.getQuestionid()+" from userid="+user.getUserid()+" name="+user.getFirstname()+" "+user.getLastname()+"");
                    userquestionsthatmustbeanswered.add(question);
                    //Go up the chain
                    List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                                       .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                                                       .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                                       .add(Restrictions.gt("referredbyuserid", 0))
                                                       .add(Restrictions.ne("referredbyuserid", user.getUserid()))
                                                       .setCacheable(true)
                                                       .list();
                    for (Iterator<Response> responseIterator=responses.iterator(); responseIterator.hasNext();) {
                        Response response=responseIterator.next();
                        if (!hasUseridBeenChecked(response.getReferredbyuserid()) &&  response.getReferredbyuserid()>0 && response.getReferredbyuserid()!=user.getUserid()){
                            User userwhoreferred = User.get(response.getReferredbyuserid());
                            //Update the list of ones that've been checked already to prevent infinite loops
                            if(useridswhohavebeencheckedforuserquestions==null){
                                useridswhohavebeencheckedforuserquestions = new ArrayList<Integer>();
                            }
                            useridswhohavebeencheckedforuserquestions.add(user.getUserid());
                            //Find and add questions, making this function recursive
                            userquestionsthatmustbeanswered.addAll(findUserQuestionsFor(userwhoreferred));
                        }
                    }
                }
            }
        }
        return userquestionsthatmustbeanswered;
    }

    private boolean hasUseridBeenChecked(int userid){
        if (useridswhohavebeencheckedforuserquestions!=null){
            for (Iterator it = useridswhohavebeencheckedforuserquestions.iterator(); it.hasNext(); ) {
                int alreadyCheckedUserid = (Integer)it.next();
                if (alreadyCheckedUserid==userid){
                    return true;
                }
            }
        }
        return false;
    }

    

    private ArrayList<Question> findOptionalUserQuestionsFor(ArrayList<Question> excludethesequestions){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Question> optionaluserquestions = new ArrayList<Question>();
        for (Iterator<Question> iterator=survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question=iterator.next();
            if (question.getIsuserquestion()){
                boolean includequestion = true;
                if (excludethesequestions!=null){
                    for (Iterator<Question> iterator1=excludethesequestions.iterator(); iterator1.hasNext();) {
                        Question exclQuest=iterator1.next();
                        if (exclQuest.getQuestionid()==question.getQuestionid()){
                            includequestion = false;
                        }
                    }
                }
                //incl
                if (includequestion){
                    optionaluserquestions.add(question);
                    logger.debug("including questionid="+question.getQuestionid());
                } else {
                    logger.debug("not including questionid="+question.getQuestionid());
                }
            }
        }
        return optionaluserquestions;
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

    public ArrayList<Question> getUserquestionsthatmustbeanswered() {
        return userquestionsthatmustbeanswered;
    }

    public void setUserquestionsthatmustbeanswered(ArrayList<Question> userquestionsthatmustbeanswered) {
        this.userquestionsthatmustbeanswered=userquestionsthatmustbeanswered;
    }

    public ArrayList<PublicSurveyUserquestionListitem> getUserquestionlistitems() {
        return userquestionlistitems;
    }

    public void setUserquestionlistitems(ArrayList<PublicSurveyUserquestionListitem> userquestionlistitems) {
        this.userquestionlistitems=userquestionlistitems;
    }

    public ArrayList<PublicSurveyUserquestionListitem> getOptionaluserquestionlistitems() {
        return optionaluserquestionlistitems;
    }

    public void setOptionaluserquestionlistitems(ArrayList<PublicSurveyUserquestionListitem> optionaluserquestionlistitems) {
        this.optionaluserquestionlistitems=optionaluserquestionlistitems;
    }


    public ArrayList<Question> getOptionaluserquestions() {
        return optionaluserquestions;
    }

    public void setOptionaluserquestions(ArrayList<Question> optionaluserquestions) {
        this.optionaluserquestions=optionaluserquestions;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response=response;
    }

    public String getYourquestion() {
        return yourquestion;
    }

    public void setYourquestion(String yourquestion) {
        this.yourquestion=yourquestion;
    }

    public boolean getYourquestionismultiplechoice() {
        return yourquestionismultiplechoice;
    }

    public void setYourquestionismultiplechoice(boolean yourquestionismultiplechoice) {
        this.yourquestionismultiplechoice=yourquestionismultiplechoice;
    }

    public boolean getYourquestionisshorttext() {
        return yourquestionisshorttext;
    }

    public void setYourquestionisshorttext(boolean yourquestionisshorttext) {
        this.yourquestionisshorttext=yourquestionisshorttext;
    }

    public boolean getYourquestionislongtext() {
        return yourquestionislongtext;
    }

    public void setYourquestionislongtext(boolean yourquestionislongtext) {
        this.yourquestionislongtext=yourquestionislongtext;
    }

    public ArrayList<String> getYourquestionmultiplechoiceoptions() {
        return yourquestionmultiplechoiceoptions;
    }

    public void setYourquestionmultiplechoiceoptions(ArrayList<String> yourquestionmultiplechoiceoptions) {
        this.yourquestionmultiplechoiceoptions=yourquestionmultiplechoiceoptions;
    }
}
