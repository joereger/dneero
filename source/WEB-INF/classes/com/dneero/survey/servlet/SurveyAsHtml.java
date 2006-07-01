package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:42:43 AM
 */
public class SurveyAsHtml {

    public static String getHtml(Survey survey, User user){
        StringBuffer out = new StringBuffer();

        out.append("<b>dNeero Survey</b>");
        out.append("<br>");
        out.append(survey.getSurveybody());
        out.append("<br>");
        out.append("<b>dNeero - blog for pay</b>");

        return out.toString();
    }

}
