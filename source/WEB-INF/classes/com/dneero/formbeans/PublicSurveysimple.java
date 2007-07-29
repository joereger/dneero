package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.io.Serializable;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.survey.servlet.SurveyFlashServlet;
import com.dneero.systemprops.BaseUrl;
import com.dneero.util.Jsf;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Jul 17, 2007
 * Time: 1:28:10 PM
 */
public class PublicSurveysimple implements Serializable {

    private String embedhtml="";
    private Survey survey;
    private User user;

    public PublicSurveysimple(){
        load();
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        logger.debug("Looking for html page survey via servlet");
        logger.debug("request.getParameter(\"s\")="+ Jsf.getRequestParam("s"));
        logger.debug("request.getParameter(\"u\")="+ Jsf.getRequestParam("u"));
        logger.debug("request.getParameter(\"p\")="+ Jsf.getRequestParam("p"));

        if (Jsf.getRequestParam("s")!=null && com.dneero.util.Num.isinteger(Jsf.getRequestParam("s"))){
            survey = Survey.get(Integer.parseInt(Jsf.getRequestParam("s")));
        }

        if (Jsf.getRequestParam("u")!=null && com.dneero.util.Num.isinteger(Jsf.getRequestParam("u"))){
            user = User.get(Integer.parseInt(Jsf.getRequestParam("u")));
        }

        boolean ispreview = false;
        if (Jsf.getRequestParam("p")!=null && com.dneero.util.Num.isinteger(Jsf.getRequestParam("p"))){
            if (Jsf.getRequestParam("p").equals("1")){
                ispreview = true;
            }
        }

        if (survey!=null && !ispreview){
            RecordImpression.record(Jsf.getHttpServletRequest());
        }

        String url = BaseUrl.get(false);
        url = FacesContext.getCurrentInstance().getExternalContext().encodeResourceURL(url);
        embedhtml = SurveyFlashServlet.getEmbedSyntax(url, survey.getSurveyid(), user.getUserid(), ispreview, true, false);


    }


    public String getEmbedhtml() {
        return embedhtml;
    }

    public void setEmbedhtml(String embedhtml) {
        this.embedhtml = embedhtml;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }                               
}
