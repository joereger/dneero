package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.display.SurveyInBlogWrapper;
import com.dneero.systemprops.BaseUrl;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyHtmlpageServlet extends HttpServlet {


    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Looking for html page survey via servlet");
        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));

        PrintWriter out = response.getWriter();

        Survey survey = null;
        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
        }

        User user = null;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            user = User.get(Integer.parseInt(request.getParameter("u")));
        }

        boolean ispreview = false;
        if (request.getParameter("p")!=null && com.dneero.util.Num.isinteger(request.getParameter("p"))){
            if (request.getParameter("p").equals("1")){
                ispreview = true;
            }
        }

        if (survey!=null && !ispreview){
            RecordImpression.record(request);
        }

        response.setContentType("text/html");
        
//        String surveyashtml = SurveyAsHtml.getHtml(survey, user, false);
//        StringBuffer scrollablediv = new StringBuffer();
//        scrollablediv.append("<div style=\"background : #ffffff; padding: 5px; width: 405px; height: 215px; overflow : auto; text-align: left;\">"+"\n");
//        scrollablediv.append(surveyashtml);
//        scrollablediv.append("</div>"+"\n");
//        String surveyashtmlwrapped = SurveyInBlogWrapper.wrap(user, survey, scrollablediv.toString(), true, false);


        String surveyflashembed = SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), user.getUserid(), ispreview, true, false);

        out.print("<html><body bgcolor=\"#ffffff\">");
        out.print("<center>");
        out.print(surveyflashembed);
        out.print("</center>");
        out.print("<script src=\"https://ssl.google-analytics.com/urchin.js\" type=\"text/javascript\"></script>\n" +
                "        <script type=\"text/javascript\">_uacct = \"UA-208946-2\";urchinTracker();</script>");
        out.print("</body></html>");
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, boolean ispreview){
        return getEmbedSyntax(baseurl, surveyid, userid, ispreview, "Show Survey");
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, boolean ispreview, String linktext){
        String out = ""+
              "<a href=\""+getUrlOfPageWithSurveyOnIt(baseurl, surveyid, userid, ispreview)+"\">" +
              linktext +
              "</a>"+
              "";

        return out;
    }

    public static String getUrlOfPageWithSurveyOnIt(String baseurl, int surveyid, int userid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyImageServlet.class);
        String out = "";
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }

        String urlofsurvey = baseurl+"shtml?s="+surveyid+"&u="+userid+"&p="+ispreviewStr;
        return urlofsurvey;
    }


}
