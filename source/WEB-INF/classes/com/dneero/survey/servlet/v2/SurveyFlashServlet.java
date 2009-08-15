package com.dneero.survey.servlet.v2;

import org.apache.log4j.Logger;
import org.apache.catalina.connector.ClientAbortException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URLEncoder;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Dbcache;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.systemprops.BaseUrl;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.util.RandomString;
import com.dneero.util.Io;
import com.dneero.pageperformance.PagePerformanceUtil;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.flagstone.transform.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyFlashServlet extends HttpServlet {



    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.error("V2 SurveyFlashServlet doPost() called... shouldn't be... should be pulling file directly from filesystem");

//        long timestart = new java.util.Date().getTime();
//        logger.debug("Looking for flash survey via servlet");
//        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
//        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
//        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));
//        logger.debug("request.getParameter(\"c\")="+request.getParameter("c"));
//        logger.debug("request.getParameter(\"r\")="+request.getParameter("r"));
//
//        Survey survey = null;
//        int surveyid = 0;
//        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
//            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
//            if (survey!=null){
//                surveyid = survey.getSurveyid();
//            }
//        }
//
//        com.dneero.dao.Response resp = null;
//        int responseid = 0;
//        if (request.getParameter("r")!=null && com.dneero.util.Num.isinteger(request.getParameter("r"))){
//            resp = com.dneero.dao.Response.get(Integer.parseInt(request.getParameter("r")));
//            if (resp!=null){
//                responseid = resp.getResponseid();
//            }
//        }
//
//        int userid = 0;
//        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
//            userid = Integer.parseInt(request.getParameter("u"));
//        }
//
//        boolean ispreview = false;
//        if (request.getParameter("p")!=null && com.dneero.util.Num.isinteger(request.getParameter("p"))){
//            if (request.getParameter("p").equals("1")){
//                ispreview = true;
//            }
//        }
//
//        boolean cache = true;
//        if (request.getParameter("c")!=null && com.dneero.util.Num.isinteger(request.getParameter("c"))){
//            if (request.getParameter("c").equals("0")){
//                cache = false;
//            }
//        }
//        cache = false; //NO CACHE TURNED ON YET... CACHING IS AT XML LEVEL RIGHT NOW... NO CUSTOMIZATION OF SWF IN V2 YET
//        //IN FACT, THIS REALLY SHOULDN"T EVEN RUN... I"M JUST PULLING THE FILE OFF THE FILESYSTEM
//
//        if (survey!=null && survey.getSurveyid()>0 && !ispreview){
//            RecordImpression.record(request);
//        } else {
//            logger.debug("not recording impression.");
//            if (survey==null){
//                logger.debug("survey is null");
//            } else {
//                logger.debug("survey.getSurveyid()="+survey.getSurveyid()+" ispreview="+ispreview);
//            }
//        }
//
//        byte[] bytes = null;
//        String nameInCache = "surveyflashservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+ispreview;
//        String cacheGroup =  "embeddedsurveycache"+"/"+"surveyid-"+surveyid;
//        Object fromCache = null;
//        //Object fromCache = CacheFactory.getCacheProvider("DbcacheProvider").get(nameInCache, cacheGroup);
//        if (fromCache!=null && cache){
//            logger.debug("returning bytes from cache");
//            bytes = (byte[])fromCache;
//            logger.debug("bytes.length="+bytes.length);
//        } else {
//            logger.debug("rebuilding bytes and putting them into cache");
//            try{
//                //Get Bytes of SWF
//                File swf = new File(WebAppRootDir.getWebAppRootPath() + "flashviewer/v2/ConvoEmbed.swf");
//                bytes = Io.getBytesFromFile(swf);
//                //Put bytes into cache
//                //CacheFactory.getCacheProvider("DbcacheProvider").put(nameInCache, cacheGroup, bytes);
//                logger.debug("End TransformSWF");
//            } catch (Exception ex){
//                logger.error("Error with transform in bottom section",ex);
//            }
//        }
//
//        //try{logger.debug("bytes="+bytes.toString());}catch(Exception ex){logger.error("",ex);}
//
//        try{
//            //Get servlet outputstream, set content type and send swf to browser client
//            ServletOutputStream outStream = response.getOutputStream();
//            response.setContentType("application/x-shockwave-flash");
//            outStream.write(bytes);
//            outStream.close();
//        } catch (ClientAbortException cex){
//            logger.debug("Client aborted", cex);
//        } catch (java.net.SocketException sex){
//            logger.debug("Trouble writing survey to browser", sex);
//        } catch (Exception e){
//            logger.error("Error writing survey to browser", e);
//        }
//
//        //Performance recording
//        try {
//            long timeend = new java.util.Date().getTime();
//            long elapsedtime = timeend - timestart;
//            PagePerformanceUtil.add("/convo.swf", InstanceProperties.getInstancename(), elapsedtime);
//        } catch (Exception ex) {
//            logger.error("", ex);
//        }
    }


    private static String getUrlOfMovie(String baseurl, int surveyid, int userid, int responseid, int plid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        Logger logger = Logger.getLogger(SurveyFlashServlet.class);
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        String cacheStr = "0";
        if (cache){
            cacheStr = "1";
        }
        String hdlStr = "0";
        if (plid!=1){
            hdlStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String randomStr = "";
        if (appendrandomstringtoforcebrowserrefresh){
            randomStr = "&rnd="+ RandomString.randomAlphanumeric(5);
        }

        String baseurlencoded = BaseUrl.get(false, plid);
        try{baseurlencoded = URLEncoder.encode(baseurlencoded, "UTF-8");}catch(Exception ex){logger.error("",ex); baseurlencoded = BaseUrl.get(false, plid);}

        //String urlofmovie = baseurl+"flashviewer/dneerosurvey.swf?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&c="+cacheStr+"&r="+responseid+"&hdl="+hdlStr+"&baseurl="+baseurlencoded+randomStr;
        String urlofmovie = baseurl+"fv2/convo.swf?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&c="+cacheStr+"&r="+responseid+"&hdl="+hdlStr+"&baseurl="+baseurlencoded+randomStr;

        return urlofmovie;
    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, int plid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        String urlofmovie = getUrlOfMovie(baseurl, surveyid, userid, responseid, plid, ispreview, cache, appendrandomstringtoforcebrowserrefresh);
        String out = ""+
              "<embed src=\""+urlofmovie+"\" wmode=\"transparent\" allowFullScreen=\"true\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\"></embed>" +
              "";
        return out;
    }

    public static String getEmbedSyntaxWithObjectTag(String baseurl, int surveyid, int userid, int responseid, int plid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        String urlofmovie = getUrlOfMovie(baseurl, surveyid, userid, responseid, plid, ispreview, cache, appendrandomstringtoforcebrowserrefresh);
        String out = "<object type=\"application/x-shockwave-flash\" allowScriptAccess=\"never\" allowNetworking=\"internal\" width=\"425\" height=\"250\" align=\"middle\" data=\""+urlofmovie+"\">" +
                     "<param name=\"allowScriptAccess\" value=\"never\" />" +
                     "<param name=\"allowNetworking\" value=\"internal\" />" +
                     "<param name=\"wmode\" value=\"transparent\">" +
                     "<param name=\"movie\" value=\""+urlofmovie+"\" />" +
                     "<param name=\"allowFullScreen\" value=\"true\" />" +
                     "<embed src=\""+urlofmovie+"\" wmode=\"transparent\" allowFullScreen=\"true\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\">"+
                     "</embed>"+
                     "</object>";
        return out;
    }


}