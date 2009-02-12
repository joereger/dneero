package com.dneero.survey.servlet.v2;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.dneero.session.UrlSplitter;
import com.dneero.survey.servlet.v1.*;
import com.dneero.survey.servlet.v1.SurveyImageServlet;

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
        logger.debug("Calling V1 SurveyHtmlpageServlet");
        com.dneero.survey.servlet.v1.SurveyHtmlpageServlet sis = new com.dneero.survey.servlet.v1.SurveyHtmlpageServlet();
        sis.doPost(request, response);
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        return getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview, "Show Conversation");
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview, String linktext){
        Logger logger = Logger.getLogger(SurveyHtmlpageServlet.class);
        logger.debug("Calling V1 SurveyHtmlpageServlet");
        return com.dneero.survey.servlet.v1.SurveyHtmlpageServlet.getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview);
    }

//    public static String getUrlOfPageWithSurveyOnIt(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
//        Logger logger = Logger.getLogger(SurveyImageServlet.class);
//        String ispreviewStr = "0";
//        if (ispreview){
//            ispreviewStr = "1";
//        }
//        if (baseurl.equals("")){
//            baseurl = "/";
//        }
//
//        //String urlofsurvey = baseurl+"shtml?s="+surveyid+"&u="+userid+"&p="+ispreviewStr;
//        String urlofsurvey = baseurl+"survey.jsp?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&r="+responseid;
//        return urlofsurvey;
//    }


}