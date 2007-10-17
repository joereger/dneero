package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.session.SurveysTakenToday;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 22, 2006
 * Time: 9:48:52 AM
 */
public class BloggerIndex implements Serializable {

    private boolean userhasresponsependings = false;
    private String responsependingmsg = "";
    private boolean showmarketingmaterial = false;
    private String msg = "";

    public BloggerIndex(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()==0){
            try{Jsf.redirectResponse("/blogger/bloggerdetails.jsf");}catch(Exception ex){logger.error(ex);}
        }
        load();
    }

    public String beginView(){
        load();
        //If the user hasn't yet set up their blogger profile
        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()==0){
            BloggerDetails bean = (BloggerDetails)Jsf.getManagedBean("bloggerDetails");
            return bean.beginView();
        }
        return "bloggerindex";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Jsf.getRequestParam("showmarketingmaterial")!=null && Jsf.getRequestParam("showmarketingmaterial").equals("1")){
            showmarketingmaterial = true;
        } else {
            showmarketingmaterial = false;
        }
        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null){
            int surveyidtoredirectto = 0;
            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Jsf.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            for (Iterator<Responsepending> iterator = responsependings.iterator(); iterator.hasNext();) {
                Responsepending responsepending = iterator.next();
                userhasresponsependings = true;
                //If we have a blogger at this point, commit the pending response
                if (Jsf.getUserSession().getUser().getBloggerid()>0){
                    Survey survey = Survey.get(responsepending.getSurveyid());
                    try{
                        SurveyResponseParser srp = new SurveyResponseParser(responsepending.getResponseasstring());
                        createResponse(survey,  srp, Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), responsepending.getReferredbyuserid());
                        responsependingmsg = responsependingmsg + "You just earned $"+ Str.formatForMoney(survey.getWillingtopayperrespondent())+"! We have successfully committed your response to '"+survey.getTitle()+"'!  But don't forget to post this survey to your blog to earn even more money... click <a href='/survey.jsf?surveyid="+survey.getSurveyid()+"'>here</a>." + "<br/><br/>";
                        surveyidtoredirectto = survey.getSurveyid();
                    } catch (ComponentException cex){
                        responsependingmsg = responsependingmsg + "There was an error committing your response to the survey '"+survey.getTitle()+"': " + cex.getErrorsAsSingleString() + "  But don't worry... we're always adding new survey opportunities!<br/><br/>";
                    }
                    //Delete the responsepending, now that it's been handled
                    responsepending.delete();
                }
            }
            if(surveyidtoredirectto>0){
                logger.debug("redirecting, will add justcompletedsurvey=1");
                try{Jsf.redirectResponse("/survey.jsf?surveyid="+surveyidtoredirectto+"&justcompletedsurvey=1"); return;}catch(Exception ex){logger.error(ex);}
            }
        }

        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getIsfacebookui()){
            try{Jsf.redirectResponse("/publicsurveylist.jsf");}catch(Exception ex){logger.error(ex);}
        }

//        BloggerSurveyList bean = (BloggerSurveyList)Jsf.getManagedBean("bloggerSurveyList");
//        bean.beginView();
        BloggerCompletedsurveys bean2 = (BloggerCompletedsurveys)Jsf.getManagedBean("bloggerCompletedsurveys");
        bean2.beginView();
    }

    public static void createResponse(Survey survey, SurveyResponseParser srp, Blogger blogger, int referredbyuserid) throws ComponentException{
        Logger logger = Logger.getLogger(PublicSurveyTake.class);
        ComponentException allCex = new ComponentException();
        //Make sure blogger hasn't taken already
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                allCex.addValidationError("You have already taken this survey before.  Each survey can only be answered once.");
            }
        }
        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey.");
        }
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
            if (Jsf.getUserSession()!=null && !Jsf.getUserSession().getIsloggedin()){
                //Not logged-in... store this response in memory for now
                Jsf.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Jsf.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing survey response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            } else {
                storeResponseInDb(survey, srp, blogger, referredbyuserid);
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){

            throw allCex;
        }
    }

    public static void storeResponseInDb(Survey survey, SurveyResponseParser srp, Blogger blogger, int referredbyuserid)  throws ComponentException{
        Logger logger = Logger.getLogger(BloggerIndex.class);
        ComponentException allCex = new ComponentException();
        User user = User.get(blogger.getUserid());
        //Make sure blogger hasn't taken already
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .setCacheable(false)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                allCex.addValidationError("You have already taken this survey before.  Each survey can only be answered once.");
            }
        }
        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey.");
        }
        //Create Response
        int responseid = 0;
        boolean isforcharity = false;
        String charityname = "";
        try{
            //Charity
            if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity")!=null){
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")!=null");
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")="+srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity"));
                String[] isforcharityArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity");
                if (isforcharityArr[0].equals("1")){
                    isforcharity = true;
                    if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-charityname")!=null){
                        String[] charitynameArr = (String[])srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-charityname");
                        charityname = charitynameArr[0];
                    } else {
                        charityname = "Default Charity";
                    }
                }
            } else {
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")==null");
            }
            //This is just a backup that forces a charity give in case somebody hacks the form and disables the hidden field isforcharity
            if (survey.getIscharityonly()){
                isforcharity = true;
                if (charityname.equals("")){
                    charityname = "Default Charity";
                }
            }
            logger.debug("isforcharity = "+isforcharity);
            logger.debug("charityname = "+charityname);

            //Create the response
            if (allCex.getErrors().length<=0){
                Response response = new Response();
                response.setBloggerid(blogger.getBloggerid());
                response.setResponsedate(new Date());
                response.setSurveyid(survey.getSurveyid());
                response.setReferredbyuserid(referredbyuserid);
                response.setIsforcharity(isforcharity);
                response.setCharityname(charityname);
                response.setPoststatus(Response.POSTATUS_NOTPOSTED);
                response.setIspaid(false);
                response.setResponsestatushtml("");
                //survey.getResponses().add(response);
                try{
                    response.save();
                    responseid = response.getResponseid();
                    survey.refresh();
                } catch (Exception ex){
                    logger.error(ex);
                    allCex.addValidationError(ex.getMessage());
                }
                //Process each question
                if (allCex.getErrors().length<=0){
                    for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
                        try{component.processAnswer(srp, response);} catch (ComponentException cex){allCex.addErrorsFromAnotherGeneralException(cex);}
                    }
                }
                //Refresh blogger
                try{blogger.save();} catch (Exception ex){logger.error(ex);};

                //Process the statusHtml for the response
                try{UpdateResponsePoststatus.processSingleResponse(response);} catch (Exception ex){logger.error(ex);};

                //Update Facebook
                FacebookApiWrapper facebookApiWrapper = new FacebookApiWrapper(Jsf.getUserSession());
                facebookApiWrapper.postSurveyToFacebookMiniFeed(survey, response);
                facebookApiWrapper.updateFacebookProfile(user);
            }
        } catch (Exception ex){
            logger.error(ex);
            allCex.addValidationError(ex.getMessage());
        }
        //Notify
        if (allCex.getErrors().length<=0){
            //Notify debug group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "dNeero Survey Taken: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+") by "+Jsf.getUserSession().getUser().getFirstname()+" "+Jsf.getUserSession().getUser().getLastname()+" ("+Jsf.getUserSession().getUser().getEmail()+")");
            xmpp.send();
        } else {
            throw allCex;
        }
        //Update the session data on number of surveys taken today
        try{
            Jsf.getUserSession().setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(Jsf.getUserSession().getUser()));
        } catch (Exception ex){
            logger.error(ex);
        }


    }


    public boolean isUserhasresponsependings() {
        return userhasresponsependings;
    }

    public void setUserhasresponsependings(boolean userhasresponsependings) {
        this.userhasresponsependings = userhasresponsependings;
    }

    public String getResponsependingmsg() {
        return responsependingmsg;
    }

    public void setResponsependingmsg(String responsependingmsg) {
        this.responsependingmsg = responsependingmsg;
    }

    public boolean getShowmarketingmaterial() {
        return showmarketingmaterial;
    }

    public void setShowmarketingmaterial(boolean showmarketingmaterial) {
        this.showmarketingmaterial = showmarketingmaterial;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
