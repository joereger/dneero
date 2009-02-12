package com.dneero.survey.servlet.v2;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.systemprops.WebAppRootDir;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyImageServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Calling V1 SurveyImageServlet");
        com.dneero.survey.servlet.v1.SurveyImageServlet sis = new com.dneero.survey.servlet.v1.SurveyImageServlet();
        sis.doPost(request, response);
    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyImageServlet.class);
        logger.debug("Calling V1 SurveyImageServlet");
        return com.dneero.survey.servlet.v1.SurveyImageServlet.getEmbedSyntax(baseurl, surveyid, userid, responseid, ispreview);
    }


}