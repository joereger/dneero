package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;

import java.util.Iterator;
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

    private Survey survey;
    private String html;
    private SurveyEnhancer surveyEnhancer;
    private boolean haveerror = false;
    private String socialbookmarklinks = "";

    public PublicSurveyTake(){
        load();
    }

    public String beginView(){
        //load();
        return "publicsurveytake";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyTake instanciated.");
        survey = new Survey();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
            logger.debug("surveyid found: "+Jsf.getRequestParam("surveyid"));
        }
        if(Jsf.getUserSession().getCurrentSurveyid()>0){
            logger.debug("Jsf.getUserSession().getCurrentSurveyid()>0");
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            socialbookmarklinks = SocialBookmarkLinks.getSocialBookmarkLinks(survey);
            surveyEnhancer = new SurveyEnhancer(survey);
            html = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
        } else {
            logger.debug("Jsf.getUserSession().getCurrentSurveyid()<=0 ---");
        }
        //Establish pendingSurveyReferredbyblogid by looking at referer, store that in the session and use it later
        Blog referredByBlog = ImpressionActivityObjectStorage.findBlogFromReferer(Jsf.getHttpServletRequest().getHeader("referer"));
        if (referredByBlog!=null){
            Jsf.getUserSession().setPendingSurveyReferredbyblogid(referredByBlog.getBlogid());
        }
        //Establish user referral in case of signup
        if (Num.isinteger(Jsf.getRequestParam("userid"))){
            Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(Jsf.getRequestParam("userid")));
        }
    }


    public String takeSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("takeSurvey() called");
        try{
            SurveyResponseParser srp = new SurveyResponseParser((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
            createResponse(survey, srp, null);
            haveerror = false;
        } catch (ComponentException cex){
            haveerror = true;
            Jsf.setFacesMessage(cex.getErrorsAsSingleString());
            return "publicsurveytake";
        }
        if (Jsf.getUserSession().getIsloggedin()){
            if (Jsf.getUserSession().getUser().getBloggerid()>0){
                BloggerIndex bean = (BloggerIndex)Jsf.getManagedBean("bloggerIndex");
                return bean.beginView();
                //return "bloggerhome";
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
                //If the person is logged-in, save the survey for them
                if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null){
                    //Pending survey save
                    //Note: this code also on Login and Registration
                    Responsepending responsepending = new Responsepending();
                    responsepending.setUserid(Jsf.getUserSession().getUser().getUserid());
                    responsepending.setResponseasstring(Jsf.getUserSession().getPendingSurveyResponseAsString());
                    responsepending.setReferredbyblogid(Jsf.getUserSession().getPendingSurveyReferredbyblogid());
                    responsepending.setSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
                    try{responsepending.save();}catch (Exception ex){logger.error(ex);}
                    Jsf.getUserSession().setPendingSurveyResponseSurveyid(0);
                    Jsf.getUserSession().setPendingSurveyReferredbyblogid(0);
                    Jsf.getUserSession().setPendingSurveyResponseAsString("");
                }
            }
        }
        //Throw if necessary
        if (allCex.getErrors().length>0){
            throw allCex;
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
}
