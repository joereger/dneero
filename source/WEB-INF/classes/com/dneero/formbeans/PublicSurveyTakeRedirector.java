package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Num;
import com.dneero.util.Jsf;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2007
 * Time: 1:15:50 PM
 */
public class PublicSurveyTakeRedirector {

    private String msg = "";

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Jsf.getRequestParam("surveyid"))){
            Jsf.getUserSession().setCurrentSurveyid(Integer.parseInt(Jsf.getRequestParam("surveyid")));
            logger.debug("surveyid found: "+Jsf.getRequestParam("surveyid"));
        }
        if (Jsf.getUserSession().getCurrentSurveyid()>0){
            try{Jsf.redirectResponse("/survey.jsf?surveyid="+Jsf.getUserSession().getCurrentSurveyid()); return null;}catch(Exception ex){logger.error(ex);}
        }
        if (!msg.equals("")){
            
        }
        return "publicsurvey";
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
