package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.util.Jsf;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import java.util.HashMap;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyTake {

    private Survey survey;
    private HashMap valueMap;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyTake(){
        logger.debug("BloggerSurveyTake instanciated.");
        survey = new Survey();
        if (Jsf.getUserSession().getCurrentSurveyid()>0){
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        }
    }


//    public String beginView(){
//        logger.debug("beginView called: survey.surveyid="+survey.getSurveyid());
//        String tmpSurveyid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
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
}