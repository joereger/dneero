package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.display.SurveyTakerDisplay;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyPosttoblog {

    private Survey survey;
    private HashMap valueMap;
    private String earnedalready="$0.00";
    private String canearn = "$0.00";
    private String surveyAnswersForThisBlogger;
    private SurveyEnhancer surveyEnhancer;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyPosttoblog(){
        logger.debug("BloggerSurveyPosttoblog instanciated.");
        survey = new Survey();
        beginView();
    }


    public String beginView(){
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

    public void load(){
        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
            boolean bloggerhasalreadytakensurvey = false;
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getSurveyid()==survey.getSurveyid()){
                    bloggerhasalreadytakensurvey = true;
                }
            }
            if (!bloggerhasalreadytakensurvey){
                try{Jsf.redirectResponse("bloggersurveydetail.jsf?surveyid="+survey.getSurveyid());}catch (Exception ex){logger.error(ex);}
            }
            surveyAnswersForThisBlogger = "<script src=\"/s?s="+survey.getSurveyid()+"&u="+Jsf.getUserSession().getUser().getUserid()+"&ispreview=1\"></script>";
            surveyEnhancer = new SurveyEnhancer(survey);
            earnedalready = surveyEnhancer.getWillingtopayforresponse();
            double maxearningNum = survey.getWillingtopayperrespondent()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );
            canearn = "$"+Str.formatForMoney(maxearningNum - survey.getWillingtopayperrespondent());
        }
    }

    public String takeSurvey(){
        logger.debug("takeSurvey() called");
        return "bloggersurveyposttoblog";
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public HashMap getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap valueMap) {
        this.valueMap = valueMap;
    }

    public String getEarnedalready() {
        return earnedalready;
    }

    public void setEarnedalready(String earnedalready) {
        this.earnedalready = earnedalready;
    }

    public String getCanearn() {
        return canearn;
    }

    public void setCanearn(String canearn) {
        this.canearn = canearn;
    }


    public String getSurveyAnswersForThisBlogger() {
        return surveyAnswersForThisBlogger;
    }

    public void setSurveyAnswersForThisBlogger(String surveyAnswersForThisBlogger) {
        this.surveyAnswersForThisBlogger = surveyAnswersForThisBlogger;
    }

    public SurveyEnhancer getSurveyEnhancer() {
        return surveyEnhancer;
    }

    public void setSurveyEnhancer(SurveyEnhancer surveyEnhancer) {
        this.surveyEnhancer = surveyEnhancer;
    }
}
