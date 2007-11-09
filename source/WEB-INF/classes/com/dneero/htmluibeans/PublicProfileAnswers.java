package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.Response;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:01 AM
 */
public class PublicProfileAnswers implements Serializable {

    private Survey survey;
    private String resultsashtml;
    private Blogger blogger;
    private User user;



    public PublicProfileAnswers(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        String tmpSurveyid = Pagez.getRequest().getParameter("surveyid");
        logger.debug("beginView called" + " tmpSurveyid="+tmpSurveyid +" Pagez.getRequest().getParameter(\"bloggerid\")="+ Pagez.getRequest().getParameter("bloggerid"));
        if (Num.isinteger(tmpSurveyid)){
            survey = Survey.get(Integer.parseInt(tmpSurveyid));
        }
        if (survey!=null && survey.getSurveyid()>0){
            String tmpBloggerid = Pagez.getRequest().getParameter("bloggerid");
            if (Num.isinteger(tmpBloggerid)){
                logger.debug("loaded bloggerid="+tmpBloggerid);
                blogger = Blogger.get(Integer.parseInt(tmpBloggerid));
                user = User.get(blogger.getUserid());
                resultsashtml = SurveyAsHtml.getHtml(survey, user, true);
            }
        }
        return "publicprofileanswers";
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public String getResultsashtml() {
        return resultsashtml;
    }

    public void setResultsashtml(String resultsashtml) {
        this.resultsashtml = resultsashtml;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
