package com.dneero.survey.servlet.v2;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.dneero.systemprops.BaseUrl;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyLinkServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Calling V1 SurveyLinkServlet");
        com.dneero.survey.servlet.v1.SurveyLinkServlet sis = new com.dneero.survey.servlet.v1.SurveyLinkServlet();
        sis.doPost(request, response);

    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyLinkServlet.class);
        logger.debug("Calling V1 SurveyLinkServlet");
        return com.dneero.survey.servlet.v1.SurveyLinkServlet.getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview);
    }


}