package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2007
 * Time: 1:15:50 PM
 */
public class PublicSurveyTakeRedirector {

    private String msg = "";

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            logger.debug("surveyid found: "+Pagez.getRequest().getParameter("surveyid"));
        }
        if (Pagez.getUserSession().getCurrentSurveyid()>0){
            try{Pagez.sendRedirect("/survey.jsf?surveyid="+Pagez.getUserSession().getCurrentSurveyid()); return;}catch(Exception ex){logger.error("",ex);}
        }
        if (!msg.equals("")){
            
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
