package com.dneero.survey.servlet.v1;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.pageperformance.PagePerformanceUtil;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.systemprops.BaseUrl;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.util.RandomString;
import com.flagstone.transform.*;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

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

        long timestart = new java.util.Date().getTime();
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

        int userid = 0;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            userid = Integer.parseInt(request.getParameter("u"));
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
        String nameInCache = "surveyflashservlet-s"+surveyid+"-u"+userid+"-ispreview"+ispreview;
        String cacheGroup =  "embeddedsurveycache"+"/"+"surveyid-"+surveyid;
        Object fromCache = CacheFactory.getCacheProvider("DbcacheProvider").get(nameInCache, cacheGroup);
        if (fromCache!=null && cache){
            logger.debug("returning bytes from cache");

            //String value = (String)fromCache;
            //logger.debug("value="+value);
            //bytes = value.getBytes("utf-8");

            bytes = (byte[])fromCache;
            logger.debug("bytes.length="+bytes.length);
        } else {
            logger.debug("rebuilding bytes and putting them into cache");
            try{
                String surveyashtml = "Sorry.  Not found. Surveyid="+request.getParameter("s");
                if (survey!=null && survey.getSurveyid()>0){
                    User user = User.get(userid);
                    surveyashtml = SurveyAsHtml.getHtml(survey, user, false);
                }
                StringBuffer surveyasxhtml = new StringBuffer();
                surveyasxhtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                surveyasxhtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
                surveyasxhtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
                surveyasxhtml.append("<head>");
                surveyasxhtml.append("<title></title>");
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
                        FSMovie movie = new FSMovie(WebAppRootDir.getWebAppRootPath() + "flashviewer/dneeroflashviewer.swf");
                        //List the allobjects in the movie that are of type DoAction
                        ArrayList objects = movie.getObjectsOfType(FSMovieObject.DoAction);
                        for (Iterator it = objects.iterator(); it.hasNext(); ) {
                            FSDoAction obj = (FSDoAction)it.next();
                            logger.debug("object found: obj.name()="+obj.name());
                            logger.debug("object found: obj.toString()="+obj.toString());

                            //Add the var to all DoAction blocks
                            ArrayList actions = obj.getActions();
                            actions.add(new FSPush("SURVEY_AS_HTML"));
                            actions.add(new FSPush(surveyashtmlencoded));
                            actions.add(FSAction.InitVariable());
                            obj.setActions(actions);
                        }
                        //Get all allobjects
                        logger.debug("Start Listing All Objects");
                        ArrayList allobjects = movie.getObjects();
                        for (Iterator it = allobjects.iterator(); it.hasNext(); ) {
                            FSMovieObject obj = (FSMovieObject)it.next();
                            logger.debug("allobject found: obj.name()="+obj.name());
                            if (obj.getType()==FSMovieObject.DefineTextField){
                                FSDefineTextField textfield = (FSDefineTextField)obj;
                                logger.debug("found text field: textfield.getVariableName()="+textfield.getVariableName());
                                if (textfield.getVariableName().equals("searchenginetext_var")){
                                    logger.debug("Setting the text of the search engine textbox");
                                    textfield.setHTML(true);
                                    //Set the search engine text... note that this is using surveyashtml to avoid all the wrapper xml/doc stuff
                                    textfield.setInitialText("This is a <h1><a href=\"http://www.dneero.com/survey.jsp?surveyid="+surveyid+"\">survey</a></h1>.<br/><br/>"+surveyashtml);
                                }
                                if (textfield.getVariableName().equals("flashhtmlvar")){
                                    logger.debug("Setting the text of the html textbox");
                                    textfield.setHTML(true);
                                    //Set the search engine text... note that this is using surveyashtml to avoid all the wrapper xml/doc stuff
                                    textfield.setInitialText(surveyashtmlencoded);
                                }
                            }
                        }
                        logger.debug("End Listing All Objects");
                        //Encode the swf and put its bytes into memory
                        bytes = movie.encode();
                        //Put bytes into cache
                        CacheFactory.getCacheProvider("DbcacheProvider").put(nameInCache, cacheGroup, bytes);
                        logger.debug("End TransformSWF");
                    } catch (Exception ex){
                        logger.error("Error with transform in bottom section",ex);
                    }
                }
            } catch (Exception ex){
                logger.error("Error getting survey from cache: ex.getMessage()="+ex.getMessage(), ex);
            }
        }

        //try{logger.debug("bytes="+bytes.toString());}catch(Exception ex){logger.error("",ex);}

        try{
            //Get servlet outputstream, set content type and send swf to browser client
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("application/x-shockwave-flash");
            outStream.write(bytes);
            outStream.close();
        } catch (ClientAbortException cex){
            logger.debug("Client aborted", cex);
        } catch (java.net.SocketException sex){
            logger.debug("Trouble writing survey to browser", sex);
        } catch (Exception e){
            logger.error("Error writing survey to browser", e);
        }

        //Performance recording
        try {
            long timeend = new java.util.Date().getTime();
            long elapsedtime = timeend - timestart;
            PagePerformanceUtil.add("/dneerosurvey.swf", InstanceProperties.getInstancename(), elapsedtime);
        } catch (Exception ex) {
            logger.error("", ex);
        }
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

        String urlofmovie = baseurl+"flashviewer/dneerosurvey.swf?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&c="+cacheStr+"&r="+responseid+"&hdl="+hdlStr+"&baseurl="+baseurlencoded+randomStr;
        return urlofmovie;
    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, int plid, boolean ispreview, boolean cache, boolean appendrandomstringtoforcebrowserrefresh){
        String urlofmovie = getUrlOfMovie(baseurl, surveyid, userid, responseid, plid, ispreview, cache, appendrandomstringtoforcebrowserrefresh);
        String out = ""+
              "<embed src=\""+urlofmovie+"\" wmode=\"transparent\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\"></embed>" +
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
                     "<embed src=\""+urlofmovie+"\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\">"+
                     "</embed>"+
                     "</object>";
        return out;
    }


}
