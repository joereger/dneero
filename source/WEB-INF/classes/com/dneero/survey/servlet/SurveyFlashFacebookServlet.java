package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.systemprops.BaseUrl;
import com.dneero.util.RandomString;
import com.flagstone.transform.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyFlashFacebookServlet extends HttpServlet {



    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Looking for flash survey via servlet");
        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));
        logger.debug("request.getParameter(\"c\")="+request.getParameter("c"));
        logger.debug("request.getParameter(\"r\")="+request.getParameter("r"));

        Survey survey = null;
        int surveyid = 0;
        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
            if (survey!=null){
                surveyid = survey.getSurveyid();
            }
        }

        com.dneero.dao.Response resp = null;
        int responseid = 0;
        if (request.getParameter("r")!=null && com.dneero.util.Num.isinteger(request.getParameter("r"))){
            resp = com.dneero.dao.Response.get(Integer.parseInt(request.getParameter("r")));
            if (resp!=null){
                responseid = resp.getResponseid();
            }
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

        if (survey!=null && survey.getSurveyid()>0 && !ispreview){
            RecordImpression.record(request);
        } else {
            logger.debug("not recording impression.");
            if (survey==null){
                logger.debug("survey is null");
            } else {
                logger.debug("survey.getSurveyid()="+survey.getSurveyid()+" ispreview="+ispreview);
            }
        }

        byte[] bytes = null;
        String nameInCache = "surveyflashfacebookservlet-s"+surveyid+"-u"+userid+"-ispreview"+ispreview;
        String cacheGroup =  "embeddedsurveycache"+"/"+"surveyid-"+surveyid;
        Object fromCache = CacheFactory.getCacheProvider().get(nameInCache, cacheGroup);
        if (fromCache!=null && cache){
            logger.debug("returning bytes from cache");
            bytes = (byte[])fromCache;
        } else {
            logger.debug("rebuilding bytes and putting them into cache");
            try{
                String surveyashtml = "Sorry.  Survey not found. Surveyid="+request.getParameter("s");
                if (survey!=null && survey.getSurveyid()>0){
                    surveyashtml = SurveyAsHtml.getHtml(survey, user, false);
                }
                StringBuffer surveyasxhtml = new StringBuffer();
                surveyasxhtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                surveyasxhtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
                surveyasxhtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
                surveyasxhtml.append("<head>");
                surveyasxhtml.append("<title>dNeero Survey</title>");
                surveyasxhtml.append("<style>");
                surveyasxhtml.append(".questiontitle{");
                surveyasxhtml.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 13px; font-weight: bold; margin: 0px; border: 0px solid #8d8d8d; padding: 0px; text-align: left; background: #e6e6e6;");
                surveyasxhtml.append("}");
                surveyasxhtml.append(".answer{");
                surveyasxhtml.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; margin: 0px;  padding: 0px; text-align: left;");
                surveyasxhtml.append("}");
                surveyasxhtml.append(".answer_highlight{");
                surveyasxhtml.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; font-weight: bold; border: 0px solid #c1c1c1; margin: 0px;  padding: 0px; text-align: left; background: #ffffff;");
                surveyasxhtml.append("}");
                surveyasxhtml.append("</style>");
                surveyasxhtml.append("</head>");
                surveyasxhtml.append("<body>");
                surveyasxhtml.append(surveyashtml);
                surveyasxhtml.append("</body>");
                surveyasxhtml.append("</html>");
                String surveyashtmlencoded = surveyasxhtml.toString();
                logger.debug("surveyashtmlencoded="+surveyashtmlencoded);
                try{surveyashtmlencoded = URLEncoder.encode(surveyasxhtml.toString(), "UTF-8");}catch(Exception ex){logger.error("",ex); surveyashtmlencoded = surveyasxhtml.toString();}
                if (1==1){
                    try{
                        logger.debug("Start TransformSWF");
                        //Get the movie from the file system
                        FSMovie movie = new FSMovie(WebAppRootDir.getWebAppRootPath() + "flashviewer/dneeroflashfacebookviewer.swf");
                        //List the objects in the movie that are of type DoAction
                        ArrayList objects = movie.getObjectsOfType(FSMovieObject.DoAction);
                        for (Iterator it = objects.iterator(); it.hasNext(); ) {
                            FSDoAction obj = (FSDoAction)it.next();
                            logger.debug("object found: obj.name()="+obj.name());
                            //Add the var to all DoAction blocks
                            ArrayList actions = obj.getActions();
                            actions.add(new FSPush("SURVEY_AS_HTML"));
                            actions.add(new FSPush(surveyashtmlencoded));
                            actions.add(FSAction.InitVariable());
                            obj.setActions(actions);
                        }
                        //Encode the swf and put its bytes into memory
                        bytes = movie.encode();
                        //Put bytes into cache
                        CacheFactory.getCacheProvider().put(nameInCache, cacheGroup, bytes);
                        logger.debug("End TransformSWF");
                    } catch (Exception ex){
                        logger.error("",ex);
                    }
                }
            } catch (Exception ex){
                logger.error("Error getting survey from cache", ex);
            }
        }

        try{logger.debug("bytes="+bytes.toString());}catch(Exception ex){logger.error("",ex);}

        try{
            //Get servlet outputstream, set content type and send swf to browser client
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("application/x-shockwave-flash");
            outStream.write(bytes);
            outStream.close();
        } catch (Exception e){
            logger.error("Error getting survey from cache");
        }
    }


    private static String getUrlOfMovie(String baseurl, int surveyid, int userid, int responseid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        Logger logger = Logger.getLogger(SurveyFlashFacebookServlet.class);
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        String cacheStr = "0";
        if (cache){
            cacheStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String randomStr = "";
        if (appendrandomstringtoforcebrowserrefresh){
            randomStr = "&rnd="+ RandomString.randomAlphanumeric(5);
        }

        String baseurlencoded = BaseUrl.get(false);
        try{baseurlencoded = URLEncoder.encode(baseurlencoded, "UTF-8");}catch(Exception ex){logger.error("",ex); baseurlencoded = BaseUrl.get(false);}

        String urlofmovie = baseurl+"flashviewer/dneerosurveyfacebook.swf?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&c="+cacheStr+"&r="+responseid+"&baseurl="+baseurlencoded+randomStr;
        return urlofmovie;
    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        String urlofmovie = getUrlOfMovie(baseurl, surveyid, userid, responseid, ispreview, cache, appendrandomstringtoforcebrowserrefresh);
        String out = ""+
              "<embed src=\""+urlofmovie+"\" wmode=\"transparent\" quality=\"high\" bgcolor=\"#ffffff\" width=\"375\" height=\"150\" name=\"dneeroflashviewer\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\"></embed>" +
              "";
        return out;
    }

    public static String getFBMLSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        Logger logger = Logger.getLogger(SurveyFlashFacebookServlet.class);
        String urlofmovie = getUrlOfMovie(baseurl, surveyid, userid, responseid, ispreview, cache, appendrandomstringtoforcebrowserrefresh);
        String out = "";
        try{
            String flashvars = URLEncoder.encode("baseurl="+baseurl, "UTF-8");
            out = "<fb:swf swfbgcolor='ffffff' swfsrc='"+urlofmovie+"' imgsrc='"+baseurl+"/images/profile-placeholder3.gif"+"' width='375' height='150'/>";
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out;
    }




}
