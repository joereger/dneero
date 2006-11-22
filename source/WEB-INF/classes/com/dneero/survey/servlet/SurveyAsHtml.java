package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.util.Str;
import com.dneero.systemprops.SystemProperty;

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

            String baseurl = SystemProperty.getProp(SystemProperty.PROP_BASEURL);

            out.append("<!-- Start dNeero Survey -->\n" +
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" background=\""+baseurl+"images/surveyinblog/dneero-survey-bg.gif\" width=\"100%\" style=\"border: 2px solid #b4b4b4;\">\n" +
                    "\t<tr>\n" +
                    "\t\t<td valign=\"top\" colspan=\"7\" style=\"padding: 15px;\">\n" +
                    "\t\t\t<!-- Start Survey Questions -->\n" +
                    stp.getSurveyForDisplay()+
                    "\n" +
                    "\t\t\t<!-- End Survey Questions -->\n" +
                    "\t\t</td>\n" +
                    "\t</tr>\n" +
                    "\t<tr>\t\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\" width=\"50%\">\n" +
                    "&nbsp;\n"+
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveytake.jsf?surveyid="+survey.getSurveyid()+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-questionmark.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\" width=\"7\">\n" +
                    "\t\t\t<img src=\""+baseurl+"images/surveyinblog/dneero-survey-dots.gif\" border=\"0\">\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\">\n" +
                    "\t\t\t<a href='"+baseurl+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-people.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\" width=\"7\">\n" +
                    "\t\t\t<img src=\""+baseurl+"images/surveyinblog/dneero-survey-dots.gif\" border=\"0\">\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\">\n" +
                    "\t\t\t<a href='"+baseurl+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-logo.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#dadada\" width=\"50%\">\n" +
                    "&nbsp;\n"+
                    "\t\t</td>\n" +
                    "\t</tr>\n" +
                    "</table>\n" +
                    "<!-- End dNeero Survey -->");


        } else {
            out = new StringBuffer();
            out.append("This embedded survey link is not correctly formatted.");
        }
        return Str.cleanForjavascriptAndReplaceDoubleQuoteWithSingle(out.toString());
    }

}
