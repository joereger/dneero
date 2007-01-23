package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.util.Str;
import com.dneero.display.SurveyInBlogWrapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

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
        if (request.getParameter("p")!=null && com.dneero.util.Num.isinteger(request.getParameter("p"))){
            if (request.getParameter("p").equals("1")){
                ispreview = true;
            }
        }

        if (survey!=null && !ispreview){
            RecordImpression.record(request);
        }

        String surveyashtml = SurveyAsHtml.getHtml(survey, user, false);
        StringBuffer scrollablediv = new StringBuffer();
        scrollablediv.append("<div style=\"border : solid 0px #cccccc; background : #e6e6e6; padding : 5px; width : 425px; height : 200px; overflow : auto; \">"+"\n");
        scrollablediv.append(surveyashtml);
        scrollablediv.append("</div>"+"\n");
        String surveyashtmlwrapped = SurveyInBlogWrapper.wrap(user, survey, scrollablediv.toString(), true, false);
        String output = Str.cleanForjavascriptAndReplaceDoubleQuoteWithSingle(surveyashtmlwrapped);
        output = output.replaceAll("\\n", "\"+\\\n\"");
        output = output.replaceAll("\\r", "\"+\\\n\"");
        out.print("document.write(\""+output+"\");"+"\n");

    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, boolean ispreview){
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String urlofsurvey = baseurl+"s?s="+surveyid+"&u="+userid+"&p="+ispreviewStr;
        return "<!-- Start dNeero Survey --><script src=\""+urlofsurvey+"\"></script><!-- End dNeero Survey -->";
    }


}
