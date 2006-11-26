package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.dao.Blog;
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyDisclosure {

    private Survey survey;
    private String html;
    private SurveyEnhancer surveyEnhancer;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public PublicSurveyDisclosure(){
        logger.debug("PublicSurveyDisclosure instanciated.");
        survey = new Survey();
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
        }
        if(Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            surveyEnhancer = new SurveyEnhancer(survey);
            html = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger());
        }
        //Establish pendingSurveyReferredbyblogid by looking at referer, store that in the session and use it later
        Blog referredByBlog = ImpressionActivityObjectStorage.findBlogFromReferer(Jsf.getHttpServletRequest().getHeader("referer"));
        if (referredByBlog!=null){
            Jsf.getUserSession().setPendingSurveyReferredbyblogid(referredByBlog.getBlogid());
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
}
