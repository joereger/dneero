package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.util.Jsf;
import com.dneero.xmpp.SendXMPPMessage;
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
        if (Jsf.getUserSession().getUser()!=null){
            //Notify debug group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "dNeero Survey Taken: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+") by "+Jsf.getUserSession().getUser().getFirstname()+" "+Jsf.getUserSession().getUser().getLastname()+" ("+Jsf.getUserSession().getUser().getEmail()+")");
            xmpp.send();
        }
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
