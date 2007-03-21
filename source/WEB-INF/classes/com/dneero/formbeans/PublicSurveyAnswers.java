package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.dao.Blog;
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyAnswers implements Serializable {

    private Survey survey;
    private String html;
    private String htmlreferredbyblogid;
    private SurveyEnhancer surveyEnhancer;

    public PublicSurveyAnswers(){

    }

    public String beginView(){
        load();
        return "publicsurveyanswers";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("PublicSurveyAnswers instanciated.");
        survey = new Survey();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
        }
        //Establish pendingSurveyReferredbyblogid by looking at referer, store that in the session and use it later
        Blog referredByBlog = ImpressionActivityObjectStorage.findBlogFromReferer(Jsf.getHttpServletRequest().getHeader("referer"));
        if (referredByBlog!=null){
            Jsf.getUserSession().setPendingSurveyReferredbyblogid(referredByBlog.getBlogid());
        }
        if(Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            surveyEnhancer = new SurveyEnhancer(survey);
            html = SurveyResultsDisplay.getHtmlForResults(survey, null, 0);
            if (Jsf.getUserSession().getPendingSurveyReferredbyblogid()>0){
                htmlreferredbyblogid = SurveyResultsDisplay.getHtmlForResults(survey, null, Jsf.getUserSession().getPendingSurveyReferredbyblogid());
            } else {
                htmlreferredbyblogid = "<font class='mediumfont'>Nobody who has clicked from the blog you were just at has answered... yet.  You could be the first!</font>";
            }
        }
        //Establish user referral in case of signup
        if (Num.isinteger(Jsf.getRequestParam("userid"))){
            Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(Jsf.getRequestParam("userid")));
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


    public String getHtmlreferredbyblogid() {
        return htmlreferredbyblogid;
    }

    public void setHtmlreferredbyblogid(String htmlreferredbyblogid) {
        this.htmlreferredbyblogid = htmlreferredbyblogid;
    }
}
