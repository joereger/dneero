package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.util.Str;
import com.dneero.util.RandomString;
import com.dneero.display.SurveyInBlogWrapper;
import com.dneero.cache.providers.CacheFactory;

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
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Looking for javascript survey via servlet");
        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));
        logger.debug("request.getParameter(\"h\")="+request.getParameter("h"));

        PrintWriter out = response.getWriter();

        Survey survey = null;
        int surveyid = 0;
        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
            logger.debug("Start Loading Survey");
            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
            if (survey!=null){
                surveyid = survey.getSurveyid();
            }
            logger.debug("End Loading Survey");
        }

        User user = null;
        int userid = 0;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            user = User.get(Integer.parseInt(request.getParameter("u")));
            if (user!=null){
                userid = user.getUserid();
            }
        }

        boolean ispreview = false;
        if (request.getParameter("p")!=null && com.dneero.util.Num.isinteger(request.getParameter("p"))){
            if (request.getParameter("p").equals("1")){
                ispreview = true;
            }
        }

        boolean cache = true;
        if (request.getParameter("c")!=null && com.dneero.util.Num.isinteger(request.getParameter("c"))){
            if (request.getParameter("c").equals("0")){
                cache = false;
            }
        }

        boolean makeHttpsIfSSLIsOn = false;
        if (request.getParameter("h")!=null && com.dneero.util.Num.isinteger(request.getParameter("h"))){
            if (request.getParameter("h").equals("1")){
                makeHttpsIfSSLIsOn = true;
            }
        }

        if (survey!=null && survey.getSurveyid()>0 && !ispreview){
            RecordImpression.record(request);
        }

        String output = "";
        if (survey!=null && survey.getSurveyid()>0){
            String nameInCache = "surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+ispreview+"-makeHttpsIfSSLIsOn"+makeHttpsIfSSLIsOn;
            String cacheGroup = "embeddedsurveycache"+"/"+"surveyid-"+surveyid;
            Object fromCache = CacheFactory.getCacheProvider().get(nameInCache, cacheGroup);
            if (fromCache!=null && cache){
                logger.debug("returning string from cache");
                output = (String)fromCache;
            } else {
                logger.debug("rebuilding string and putting it into cache");
                try{
                    String surveyashtml = SurveyAsHtml.getHtml(survey, user, makeHttpsIfSSLIsOn);
                    StringBuffer scrollablediv = new StringBuffer();
                    scrollablediv.append("<div style=\"background : #ffffff; padding: 5px; width: 405px; height: 215px; overflow : auto; text-align: left;\">"+"\n");
                    scrollablediv.append(surveyashtml);
                    scrollablediv.append("</div>"+"\n");
                    String surveyashtmlwrapped = SurveyInBlogWrapper.wrap(user, survey, scrollablediv.toString(), true, makeHttpsIfSSLIsOn);
                    output = Str.cleanForjavascriptAndReplaceDoubleQuoteWithSingle(surveyashtmlwrapped);
                    output = output.replaceAll("\\n", "\"+\\\n\"");
                    output = output.replaceAll("\\r", "\"+\\\n\"");
                    output = "document.write(\""+output+"\");"+"\n";
                    //Put bytes into cache
                    CacheFactory.getCacheProvider().put(nameInCache, cacheGroup, output);
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        } else {
            output = "Sorry.  Survey not found.  Surveyid="+request.getParameter("s");
            output = "document.write(\""+output+"\");"+"\n";
        }
        //Output to client
        out.print(output);
    }

    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, boolean ispreview, boolean makeHttpsIfSSLIsOn, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        String cacheStr = "0";
        if (cache){
            cacheStr = "1";
        }
        String makeHttpsIfSSLIsOnStr = "0";
        if (makeHttpsIfSSLIsOn){
            makeHttpsIfSSLIsOnStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String randomStr = "";
        if (appendrandomstringtoforcebrowserrefresh){
            randomStr = "&rnd="+ RandomString.randomAlphanumeric(5);
        }
        String urlofsurvey = baseurl+"s?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&h="+makeHttpsIfSSLIsOnStr+"&c="+cacheStr+randomStr;
        return "<script src=\""+urlofsurvey+"\"></script>";
    }


}
