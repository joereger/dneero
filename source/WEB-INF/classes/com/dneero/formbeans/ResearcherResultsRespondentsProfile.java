package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:11 AM
 */
public class ResearcherResultsRespondentsProfile {


    Logger logger = Logger.getLogger(this.getClass().getName());
    private Survey survey;
    private Response response;
    private Blogger blogger;
    private User user;


    public ResearcherResultsRespondentsProfile(){
        load();
    }

    public String beginView(){
        String tmpResponseid = Jsf.getRequestParam("responseid");
        if (com.dneero.util.Num.isinteger(tmpResponseid)){
            logger.debug("beginView called: found surveyid in request param="+tmpResponseid);
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            response = Response.get(Integer.parseInt(tmpResponseid));
            blogger = Blogger.get(response.getBloggerid());
            user = User.get(blogger.getUserid());
            blogger.refresh();
            load();
        }
        return "researcherresultsrespondentsprofile";
    }

    private void load(){    
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
