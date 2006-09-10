package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerSurveyDetail {

    private Survey survey;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerSurveyDetail(){
        logger.debug("BloggerSurveyDetail instanciated.");
        survey = new Survey();
    }


    public String beginView(){
        logger.debug("beginView called: survey.surveyid="+survey.getSurveyid());
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            survey = Survey.get(Integer.parseInt(tmpSurveyid));
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

}
