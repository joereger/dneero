package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;

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
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            return com.dneero.survey.servlet.v1.SurveyAsHtml.getHtml(survey, user, makeHttpsIfSSLIsOn, displayEvenIfSysadminRejected);
        } else if (survey.getEmbedversion()==Survey.EMBEDVERSION_02){
            return com.dneero.survey.servlet.v1.SurveyAsHtml.getHtml(survey, user, makeHttpsIfSSLIsOn, displayEvenIfSysadminRejected);
        }
        return "";
    }

}
