package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.survey.servlet.EmbedInHtmlSyntax;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyDetail {

    private Survey survey;
    private SurveyEnhancer surveyEnhancer;
    private String surveyForTakers;
    private boolean bloggerhasalreadytakensurvey;
    private String surveyAnswersForThisBlogger;
    private String surveyOnBlogPreview;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public PublicSurveyDetail(){
        logger.debug("PublicSurveyDetail instanciated.");
        survey = new Survey();
    }


    public String beginView(){
        logger.debug("beginView called");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            survey = Survey.get(Integer.parseInt(tmpSurveyid));
            bloggerhasalreadytakensurvey = false;
            int userid = 0;
            if (Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
                userid = Jsf.getUserSession().getUser().getUserid();
                Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
                for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        bloggerhasalreadytakensurvey = true;
                    }
                }
            }
            if (bloggerhasalreadytakensurvey){
                surveyAnswersForThisBlogger = EmbedInHtmlSyntax.getFlash("/", survey.getSurveyid(), userid, true);
            } else {
                surveyOnBlogPreview = EmbedInHtmlSyntax.getFlash("/", survey.getSurveyid(), userid, true);
            }
            surveyEnhancer = new SurveyEnhancer(survey);
            surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
        } else {
            logger.debug("beginView called: NOT found surveyid in param="+tmpSurveyid);
        }
        return "publicsurveydetail";
    }

    public String beginTakeSurvey(){
        try{
            Jsf.redirectResponse("surveytake.jsf?surveyid="+Jsf.getUserSession().getCurrentSurveyid());
        } catch (Exception ex){
            logger.error(ex);
        }
        return null;
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

    public String getSurveyOnBlogPreview() {
        return surveyOnBlogPreview;
    }

    public void setSurveyOnBlogPreview(String surveyOnBlogPreview) {
        this.surveyOnBlogPreview = surveyOnBlogPreview;
    }



}
