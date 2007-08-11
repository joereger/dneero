package com.dneero.survey.servlet;

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
        logger.debug("Looking for link survey via servlet");
        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));
        logger.debug("request.getParameter(\"r\")="+request.getParameter("r"));

        try{
            String urlofsurvey = BaseUrl.get(false)+"shtml?s="+request.getParameter("s")+"r="+request.getParameter("r")+"&u="+request.getParameter("u")+"&p="+request.getParameter("p");
            response.sendRedirect(urlofsurvey);

        } catch (java.net.SocketException e){
            logger.debug(e);
        }

    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyLinkServlet.class);
        String out = "";
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String urlofsurvey = baseurl+"shtml?s="+surveyid+"&u="+userid+"&p="+ispreviewStr;

        out = ""+
              "<a href=\""+urlofsurvey+"\">" +
              "Click Here for Survey Answers" +
              "</a>"+
              "";

        return out;
    }


}
