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
        if (request.getParameter("surveyid")!=null && com.dneero.util.Num.isinteger(request.getParameter("surveyid"))){
            survey = Survey.get(Integer.parseInt(request.getParameter("surveyid")));
        }

        User user = null;
        if (request.getParameter("userid")!=null && com.dneero.util.Num.isinteger(request.getParameter("userid"))){
            user = User.get(Integer.parseInt(request.getParameter("userid")));
        }

        if (survey!=null && user!=null){
            String referer = request.getHeader("referer");
            logger.debug("referer=" + referer);

            //Find blogid
            Blog blog=null;
            if (user.getBlogger()!=null && referer!=null){
                for (Iterator it = user.getBlogger().getBlogs().iterator(); it.hasNext(); ) {
                    Blog blogTmp = (Blog)it.next();
                    if (referer.indexOf(blogTmp.getUrl())>0){
                        blog = blogTmp;
                        break;
                    }
                }
            }

            //Record
            RecordImpression.record(survey, user, blog, request);
        }

        String output = SurveyAsHtml.getHtml(survey, user);
        out.print("document.write(\""+output+"\");"+"\n");

    }


}
