package com.dneero.survey.servlet.v2;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.Blogger;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.display.SurveyTemplateProcessorV2;
import com.dneero.pageperformance.PagePerformanceUtil;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.util.Util;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyFlashServletXMLResponse extends HttpServlet {


    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());

        long timestart = new java.util.Date().getTime();
        logger.debug("Looking for flash V2 survey via servlet");
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
        //cache=false; //Remove before production, yo!

        //Recordeth Thy Impressions that Bringeth Charitable Donations Henceforth Thusly and Stuff
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


        //To Cache or Not to Cache, that is the Question
        String responseAsXML = null;
        String nameInCache = "surveyflashservletxmlresponse-v2-s"+surveyid+"-u"+userid+"-r"+responseid+"-ispreview"+ispreview;
        String cacheGroup =  "embeddedsurveycache"+"/"+"surveyid-"+surveyid;
        Object fromCache = CacheFactory.getCacheProvider("DbcacheProvider").get(nameInCache, cacheGroup);
        if (!cache){logger.debug("cache off");} else {logger.debug("cache on");}
        logger.debug("fromCache="+String.valueOf((String)fromCache));
        if (fromCache!=null && cache){
            logger.debug("returning responseAsXML from cache");
            responseAsXML = (String)fromCache;
            logger.debug("responseAsXML="+responseAsXML);
        } else {
            logger.debug("rebuilding responseAsXML and putting into cache");
            try{
                if (survey!=null && survey.getSurveyid()>0){
                    User user = User.get(userid);
                    Blogger blogger = Blogger.get(user.getBloggerid());
                    Document responseAsXMLDoc = SurveyTemplateProcessorV2.getXmlForDisplay(survey, blogger, resp, false, false);
                    responseAsXML = Util.jdomXmlDocAsString(responseAsXMLDoc);
                } else {
                    Element el = new Element("error");
                    el.setContent(new Text("This is not currently available."));
                    Document doc = new Document(el);
                    responseAsXML = Util.jdomXmlDocAsString(doc);
                }
                logger.debug("responseAsXML="+responseAsXML);
                //Consider: responseAsXML = URLEncoder.encode(responseAsXML.toString(), "UTF-8");
                //Cache put
                if (1==1){
                    try{
                        //Put bytes into cache
                        logger.debug("putting to cache responseAsXML="+responseAsXML);
                        CacheFactory.getCacheProvider("DbcacheProvider").put(nameInCache, cacheGroup, responseAsXML);
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
            response.setContentType("text/xml");
            outStream.write(responseAsXML.getBytes());
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
            PagePerformanceUtil.add("/SurveyFlashServletXMLResponse", InstanceProperties.getInstancename(), elapsedtime);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }





}