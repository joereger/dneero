package com.dneero.display;

import com.dneero.systemprops.BaseUrl;
import com.dneero.dao.Survey;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jan 18, 2007
 * Time: 12:05:45 PM
 */
public class SurveyInBlogWrapper {

    public static String wrap(User user, Survey survey, String in, boolean includeFooter, boolean makeHttpsIfSSLIsOn){
        StringBuffer out = new StringBuffer();
        if (survey==null){
            survey = new Survey();
        }
        int userid = 0;
        if (user!=null){
            userid = user.getUserid();
        }
        String baseurl = BaseUrl.get(makeHttpsIfSSLIsOn);
        out.append("<!-- Start dNeero Survey -->\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"#e6e6e6\" width=\"425\">\n" +
                "\t<tr>\n" +
                "\t\t<td valign=\"top\" colspan=\"7\" height=\"215\">\n" +
                "\t\t\t<!-- Start Survey Questions -->\n" +
                in +
                "\n" +
                "\t\t\t<!-- End Survey Questions -->\n" +
                "\t\t</td>\n" +
                "\t</tr>\n");
         if(includeFooter){
            out.append("\t<tr>\t\n" +
                    "\t\t<td valign=\"bottom\" align=\"left\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveytake.jsf?surveyid="+survey.getSurveyid()+"&userid="+userid+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-questionmark.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"center\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveyanswers.jsf?surveyid="+survey.getSurveyid()+"&userid="+userid+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-people.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveydisclosure.jsf?surveyid="+survey.getSurveyid()+"&userid="+userid+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-logo.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t</tr>\n");    
        }
        out.append("</table>\n" +
                "<!-- End dNeero Survey -->");

         return out.toString();
    }


}
