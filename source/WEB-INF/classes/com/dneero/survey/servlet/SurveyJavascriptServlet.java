package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyJavascriptServlet extends HttpServlet {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        if (request.getParameter("ispreview")!=null && com.dneero.util.Num.isinteger(request.getParameter("ispreview"))){
            if (request.getParameter("ispreview").equals("1")){
                ispreview = true;
            }
        }

        if (survey!=null && !ispreview){
            RecordImpression.record(survey, request);
        }

        String output = SurveyAsHtml.getHtml(survey, user);
        output = output.replaceAll("\\n", "\"+\\\n\"");
        output = output.replaceAll("\\r", "\"+\\\n\"");
        out.print("document.write(\""+output+"\");"+"\n");

    }


}
