package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.finders.FindSurveysForBlogger;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyTake {

    private Survey survey;
    private String html;
    private boolean haveerror = false;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyTake(){
        logger.debug("BloggerSurveyTake instanciated.");
        survey = new Survey();
        if (Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            html = SurveyTakerDisplay.getHtmlForSurveyTaking(Survey.get(Jsf.getUserSession().getCurrentSurveyid()), Blogger.get(Jsf.getUserSession().getUser().getBloggerid()));
        }
    }


    public String takeSurvey(){
        logger.debug("takeSurvey() called");
        try{
            SurveyResponseParser srp = new SurveyResponseParser((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
            createResponse(survey, srp, Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), 0);
            haveerror = false;
        } catch (ComponentException cex){
            haveerror = true;
            Jsf.setFacesMessage(cex.getErrorsAsSingleString());
            return "bloggersurveytake";
        }
        return "bloggersurveyposttoblog";
    }

    //This method can be called by this class or by BloggerIndex to store a pending survey
    public static void createResponse(Survey survey, SurveyResponseParser srp, Blogger blogger, int referredbyblogid) throws ComponentException{
        Logger logger = Logger.getLogger(BloggerSurveyTake.class);
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
                //Create Response
                try{
                    //Create the response
                    Response response = new Response();
                    response.setBloggerid(blogger.getBloggerid());
                    response.setResponsedate(new Date());
                    response.setSurveyid(survey.getSurveyid());
                    response.setReferredbyblogid(referredbyblogid);
                    survey.getResponses().add(response);
                    try{
                        survey.save();
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
                } catch (Exception ex){
                    logger.error(ex);
                    allCex.addValidationError(ex.getMessage());
                }
                //Move money, etc
                if (allCex.getErrors().length<=0){
                    //Affect balance for blogger
                    MoveMoneyInAccountBalance.pay(Jsf.getUserSession().getUser(), survey.getWillingtopayperrespondent(), "Pay for taking survey: '"+survey.getTitle()+"'", true);
                    //Affect balance for researcher
                    MoveMoneyInAccountBalance.charge(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (survey.getWillingtopayperrespondent()+(survey.getWillingtopayperrespondent()*(SurveyMoneyStatus.DNEEROMARKUPPERCENT/100))), "User "+Jsf.getUserSession().getUser().getFirstname()+" "+Jsf.getUserSession().getUser().getLastname()+" responds to survey '"+survey.getTitle()+"'");
                    //Notify debug group
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "dNeero Survey Taken: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+") by "+Jsf.getUserSession().getUser().getFirstname()+" "+Jsf.getUserSession().getUser().getLastname()+" ("+Jsf.getUserSession().getUser().getEmail()+")");
                    xmpp.send();
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
}
