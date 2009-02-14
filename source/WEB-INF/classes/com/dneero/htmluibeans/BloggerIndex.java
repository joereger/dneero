package com.dneero.htmluibeans;


import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.helpers.StoreResponse;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

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


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()==0){
            try{Pagez.sendRedirect("/blogger/bloggerdetails.jsp");return;} catch (Exception ex){logger.error("",ex);}
        }

        if (Pagez.getRequest().getParameter("showmarketingmaterial")!=null && Pagez.getRequest().getParameter("showmarketingmaterial").equals("1")){
            showmarketingmaterial = true;
        } else {
            showmarketingmaterial = false;
        }
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null){
            int surveyidtoredirectto = 0;
            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            for (Iterator<Responsepending> iterator = responsependings.iterator(); iterator.hasNext();) {
                Responsepending responsepending = iterator.next();
                userhasresponsependings = true;
                //If we have a blogger at this point, commit the pending response
                if (Pagez.getUserSession().getUser().getBloggerid()>0){
                    Survey survey = Survey.get(responsepending.getSurveyid());
                    try{
                        SurveyResponseParser srp = new SurveyResponseParser(responsepending.getResponseasstring());
                        createResponse(survey,  srp, Blogger.get(Pagez.getUserSession().getUser().getBloggerid()), responsepending.getReferredbyuserid());
                        responsependingmsg = responsependingmsg + "We've successfully committed your response to '"+survey.getTitle()+"'!" + "<br/><br/>";
                        surveyidtoredirectto = survey.getSurveyid();
                    } catch (ComponentException cex){
                        responsependingmsg = responsependingmsg + "There was an error committing your response to '"+survey.getTitle()+"': " + cex.getErrorsAsSingleString() + "  But don't worry... we're always adding new conversations opportunities!<br/><br/>";
                    }
                    //Delete the responsepending, now that it's been handled
                    responsepending.delete();
                }
            }
            if(surveyidtoredirectto>0){
                Pagez.getUserSession().setMessage(responsependingmsg);
                Pagez.sendRedirect("/surveypostit.jsp?surveyid="+surveyidtoredirectto+"&justcompletedsurvey=1");
                return;
            }
        }


//        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){
//            completedsurveys = new ArrayList();
//            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
//                                       .add(Restrictions.eq("bloggerid", Pagez.getUserSession().getUser().getBloggerid()))
//                                       .add(Restrictions.eq("ispaid", false))
//                                       .add(Restrictions.ne("poststatus", Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED))
//                                       .addOrder(Order.desc("responsedate"))
//                                       .setCacheable(true)
//                                       .setMaxResults(60)
//                                       .list();
//            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
//                Response response = iterator.next();
//                Survey survey = Survey.get(response.getSurveyid());
//                BloggerCompletedsurveysListitem listitem = new BloggerCompletedsurveysListitem();
//                listitem.setTotalimpressions(0);
//                listitem.setPaidandtobepaidimpressions(0);
//                listitem.setResponsedate(response.getResponsedate());
//                listitem.setResponseid(response.getResponseid());
//                listitem.setSurveyid(survey.getSurveyid());
//                listitem.setSurveytitle(survey.getTitle());
//                listitem.setResponse(response);
//                completedsurveys.add(listitem);
//            }
//        }


        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getIsfacebookui()){
            Pagez.sendRedirect("/publicsurveylist.jsp");
            return;
        }

    }

    public static void createResponse(Survey survey, SurveyResponseParser srp, Blogger blogger, int referredbyuserid) throws ComponentException{
        Logger logger = Logger.getLogger(BloggerIndex.class);
        ComponentException allCex = new ComponentException();
        //Make sure blogger hasn't taken already
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                allCex.addValidationError("You have already joined this conversation.");
            }
        }
        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to join this conversation.  Your qualification is determined by your Profile.  Conversation igniters determine their intended audience when they create a conversation.");
        }
        //Make sure each component is validated
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByType(question.getComponenttype(), question, blogger);
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
            if (Pagez.getUserSession()!=null && !Pagez.getUserSession().getIsloggedin()){
                //Not logged-in... store this response in memory for now
                Pagez.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Pagez.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
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
        StoreResponse.storeResponseInDb(survey, srp, blogger, referredbyuserid);
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

//    public ArrayList<BloggerCompletedsurveysListitem> getCompletedsurveys() {
//        return completedsurveys;
//    }
//
//    public void setCompletedsurveys(ArrayList<BloggerCompletedsurveysListitem> completedsurveys) {
//        this.completedsurveys=completedsurveys;
//    }
}
