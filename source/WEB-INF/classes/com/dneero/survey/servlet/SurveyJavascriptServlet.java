package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyJavascriptServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        Survey survey = null;
        if (request.getParameter("surveyid")!=null && com.dneero.util.Num.isinteger(request.getParameter("surveyid"))){
            survey = Survey.get(Integer.parseInt(request.getParameter("surveyid")));
        }

        User user = null;
        if (request.getParameter("userid")!=null && com.dneero.util.Num.isinteger(request.getParameter("userid"))){
            user = User.get(Integer.parseInt(request.getParameter("userid")));
        }

        String output = SurveyAsHtml.getHtml(survey, user, request);
        out.print("document.write(\""+output+"\");"+"\n");

    }


}
