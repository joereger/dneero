package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

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
        logger.error("SurveyHtmlpageServlet called.  It shouldn't be.");
        response.sendRedirect("/survey.jsf?s="+request.getParameter("s")+"&u="+request.getParameter("u")+"&p="+request.getParameter("p")+"&permitbeforefacebookappadd=1");
        return;
//        logger.debug("Looking for html page survey via servlet");
//        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
//        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
//        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));
//
//        PrintWriter out = response.getWriter();
//
//        Survey survey = null;
//        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
//            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
//        }
//
//        User user = null;
//        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
//            user = User.get(Integer.parseInt(request.getParameter("u")));
//        }
//
//        boolean ispreview = false;
//        if (request.getParameter("p")!=null && com.dneero.util.Num.isinteger(request.getParameter("p"))){
//            if (request.getParameter("p").equals("1")){
//                ispreview = true;
//            }
//        }
//
//        if (survey!=null && !ispreview){
//            RecordImpression.record(request);
//        }
//
//        response.setContentType("text/html");
//
//        String surveyflashembed = SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), survey.getSurveyid(), user.getUserid(), ispreview, true, false);
//
//        out.print("<html><body bgcolor=\"#ffffff\">");
//        out.print("<center>");
//        out.print(surveyflashembed);
//        out.print("</center>");
//        out.print("<script src=\"https://ssl.google-analytics.com/urchin.js\" type=\"text/javascript\"></script>\n" +
//                "        <script type=\"text/javascript\">_uacct = \"UA-208946-2\";urchinTracker();</script>");
//        out.print("</body></html>");

    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        return getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview, "Show Survey");
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview, String linktext){
        String out = ""+
              "<a href=\""+getUrlOfPageWithSurveyOnIt(baseurl, surveyid, userid, responseid, ispreview)+"\">" +
              linktext +
              "</a>"+
              "";

        return out;
    }

    public static String getUrlOfPageWithSurveyOnIt(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyImageServlet.class);
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }

        //String urlofsurvey = baseurl+"shtml?s="+surveyid+"&u="+userid+"&p="+ispreviewStr;
        String urlofsurvey = baseurl+"survey.jsf?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&r="+responseid+"+&permitbeforefacebookappadd=1";
        return urlofsurvey;
    }


}
