package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.util.Str;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:42:43 AM
 */
public class SurveyAsHtml {

    public static String getHtml(Survey survey, User user, boolean makeHttpsIfSSLIsOn){
        return getHtml(survey, user, makeHttpsIfSSLIsOn, false);
    }

    public static String getHtml(Survey survey, User user, boolean makeHttpsIfSSLIsOn, boolean displayEvenIfSysadminRejected){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyAsHtml.class);
        if (survey!=null && user!=null){
            SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, Blogger.get(user.getBloggerid()));
            out.append(stp.getSurveyForDisplay(makeHttpsIfSSLIsOn, displayEvenIfSysadminRejected));
        } else {
            out = new StringBuffer();
            out.append("This embedded conversation link is not correctly formatted.");
        }
        return out.toString();
    }

}
