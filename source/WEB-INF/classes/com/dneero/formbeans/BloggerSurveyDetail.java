package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.util.*;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.survey.servlet.SurveyJavascriptServlet;

import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyDetail implements Serializable {

    private Survey survey;
    private SurveyEnhancer surveyEnhancer;
    private String surveyForTakers;
    private String surveyOnBlogPreview;


    public BloggerSurveyDetail(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("BloggerSurveyDetail instanciated.");
        survey = new Survey();
        beginView();
    }


    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            load();
        } else {
            logger.debug("beginView called: NOT found surveyid in param="+tmpSurveyid);
        }
        load();
        return "bloggersurveydetail";
    }

    private void load(){
            Logger logger = Logger.getLogger(this.getClass().getName());
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
            boolean bloggerhasalreadytakensurvey = false;
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    bloggerhasalreadytakensurvey = true;
                }
            }
            if (bloggerhasalreadytakensurvey){
                try{Jsf.redirectResponse("bloggersurveyposttoblog.jsf?surveyid="+survey.getSurveyid());}catch (Exception ex){logger.error(ex);}
            }
            surveyOnBlogPreview = SurveyJavascriptServlet.getEmbedSyntax("/", survey.getSurveyid(), Jsf.getUserSession().getUser().getUserid(), true);
            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
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



    public String getSurveyOnBlogPreview() {
        return surveyOnBlogPreview;
    }

    public void setSurveyOnBlogPreview(String surveyOnBlogPreview) {
        this.surveyOnBlogPreview = surveyOnBlogPreview;
    }


}
