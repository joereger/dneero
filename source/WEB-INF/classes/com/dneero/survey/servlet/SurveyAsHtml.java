package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.util.Str;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:42:43 AM
 */
public class SurveyAsHtml {


    public static String getHtml(Survey survey, User user){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyAsHtml.class);
        if (survey!=null && user!=null){
            SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, user.getBlogger());
            out.append("<b>dNeero Survey</b>");
            out.append("<br/>");
            out.append(Str.cleanForjavascriptAndReplaceDoubleQuoteWithSingle(stp.getSurveyForDisplay()));
            out.append("<br/>");
            out.append("<b>dNeero - blog for pay</b>");
        } else {
            out = new StringBuffer();
            out.append("This embedded survey link is not correctly formatted.");
        }
        return out.toString();
    }

}
