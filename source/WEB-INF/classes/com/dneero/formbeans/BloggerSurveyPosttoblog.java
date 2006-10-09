package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.util.Jsf;
import com.dneero.util.Str;

import java.util.HashMap;

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

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyPosttoblog(){
        logger.debug("BloggerSurveyPosttoblog instanciated.");
        survey = new Survey();
        if (Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());

            earnedalready = "$"+Str.formatForMoney(survey.getWillingtopayperrespondent());

            double maxearningNum = survey.getWillingtopayperrespondent()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );
            canearn = "$"+Str.formatForMoney(maxearningNum - survey.getWillingtopayperrespondent());
            
        }
    }


//    public String beginView(){
//        logger.debug("beginView called: survey.surveyid="+survey.getSurveyid());
//        String tmpSurveyid = Jsf.getRequestParam("surveyid");
//        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
//            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
//            survey = Survey.get(Integer.parseInt(tmpSurveyid));
//        } else {
//            logger.debug("beginView called: NOT found surveyid in param="+tmpSurveyid);
//        }
//        return "bloggersurveytake";
//    }

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
}
