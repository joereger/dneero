package com.dneero.survey.servlet.v1;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.dneero.session.UrlSplitter;

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
        UrlSplitter urlSplitter = new UrlSplitter(request);
        logger.debug("SurveyHtmlpageServlet called.  It shouldn't be. url="+urlSplitter.getRequestUrl()+"?"+urlSplitter.getQuerystring()+" referer="+request.getHeader("referer"));
        response.sendRedirect("/survey.jsp?s="+request.getParameter("s")+"&u="+request.getParameter("u")+"&p="+request.getParameter("p"));
        return;
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        return getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview, "Show Conversation");
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
        String urlofsurvey = baseurl+"survey.jsp?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&r="+responseid;
        return urlofsurvey;
    }


}
