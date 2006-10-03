package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.util.*;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.survey.servlet.SurveyAsHtml;

import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyDetail {

    private Survey survey;
    private SurveyEnhancer surveyEnhancer;
    private String surveyForTakers;
    private boolean bloggerhasalreadytakensurvey;
    private String surveyAnswersForThisBlogger;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyDetail(){
        logger.debug("BloggerSurveyDetail instanciated.");
        survey = new Survey();
    }


    public String beginView(){
        logger.debug("beginView called");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            survey = Survey.get(Integer.parseInt(tmpSurveyid));

            Blogger blogger = Jsf.getUserSession().getUser().getBlogger();
            bloggerhasalreadytakensurvey = false;
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    bloggerhasalreadytakensurvey = true;
                    surveyAnswersForThisBlogger = SurveyAsHtml.getHtml(survey, User.get(blogger.getUserid()));
                }
            }

            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger());
        } else {
            logger.debug("beginView called: NOT found surveyid in param="+tmpSurveyid);
        }
        return "bloggersurveydetail";
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

}
