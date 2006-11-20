package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.survey.servlet.SurveyAsHtml;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:01 AM
 */
public class ResearcherResultsRespondentsAnswers {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private Survey survey;
    private String resultsashtml;
    private Blogger blogger;
    private User user;



    public ResearcherResultsRespondentsAnswers(){

    }

    public String beginView(){
        Survey survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            this.survey = survey;
            String tmpBloggerid = Jsf.getRequestParam("bloggerid");
            if (com.dneero.util.Num.isinteger(tmpBloggerid)){
                blogger = Blogger.get(Integer.parseInt(tmpBloggerid));
                user = User.get(blogger.getBloggerid());
                resultsashtml = SurveyAsHtml.getHtml(survey, user);
            }
        }
        return "researcherresultsrespondentsanswers";
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
