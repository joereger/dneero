package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URLEncoder;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.systemprops.BaseUrl;
import com.flagstone.transform.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyFlashServlet extends HttpServlet {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Looking for flash survey.");

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
            RecordImpression.record(request);
        }

        String surveyashtml = SurveyAsHtml.getHtml(survey, user, false);

        StringBuffer surveyasxhtml = new StringBuffer();
        surveyasxhtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        surveyasxhtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        surveyasxhtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        surveyasxhtml.append("<head>");
        surveyasxhtml.append("<title>dNeero Survey</title>");
        surveyasxhtml.append("</head>");
        surveyasxhtml.append("<body>");
        surveyasxhtml.append(surveyashtml);
        surveyasxhtml.append("</body>");
        surveyasxhtml.append("</html>");

        String surveyashtmlencoded = surveyasxhtml.toString();
        try{surveyashtmlencoded = URLEncoder.encode(surveyasxhtml.toString(), "UTF-8");}catch(Exception ex){logger.error(ex); surveyashtmlencoded = surveyasxhtml.toString();}


        if (1==1){
            try{
                logger.debug("Start TransformSWF");
                //Get the movie from the file system
                FSMovie movie = new FSMovie(WebAppRootDir.getWebAppRootPath() + "flashviewer/dneeroflashviewer.swf");
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
//                    actions.add(new FSPush("baseurl"));
//                    actions.add(new FSPush(BaseUrl.get(false)));
//                    actions.add(FSAction.InitVariable());
                    obj.setActions(actions);
                }
                //Encode the swf and put its bytes into memory
                byte[] bytes = movie.encode();
                //Get servlet outputstream, set content type and send swf to browser client
                ServletOutputStream outStream = response.getOutputStream();
                response.setContentType("application/x-shockwave-flash");
                outStream.write(bytes);
                outStream.close();
                logger.debug("End TransformSWF");
            } catch (Exception ex){
                logger.error(ex);
            }
        }


//        if (1==2){
//            //Output the SWF file
//            int bytesOutput = 0;
//            try{
//                //Get the SWF file from the hard disk
//                File swffile = new File(WebAppRootDir.getWebAppRootPath() + "flashviewer/dneeroflashviewer.swf");
//                if (!swffile.canRead()) {
//                    logger.error("Can't find dneeroflashviewer.swf at "+swffile.getAbsolutePath());
//                }
//                // Use getOutputStream instead of getWriter
//                ServletOutputStream outStream = response.getOutputStream();
//                response.setContentType("application/x-shockwave-flash");
//                try{
//                    // Creating a new FileInputStream object
//                    FileInputStream fis = new FileInputStream(swffile);
//                    if (fis != null){
//                        byte[] data = new byte[4096];
//                        try{
//                            int bytesRead;
//                            while ((bytesRead = fis.read(data)) != -1){
//                                bytesOutput = bytesOutput + bytesRead;
//                                outStream.write(data, 0, bytesRead);
//                            }
//                            fis.close();
//                        }catch (java.net.SocketException e){
//                            //Do nothing... typically a ClientAbortException
//                        }catch (IOException e){
//                            logger.debug("IO Exception attempting to read file: '" + swffile.getAbsolutePath() + "'<br>" + e.toString());
//                            logger.debug(e);
//                        }catch (Throwable e){
//                            logger.error(e);
//                        }
//                    }
//                } catch (IOException e){
//                    //This is a specific error that happens when users abort their connection.
//                    logger.debug(e);
//                } catch (java.lang.IllegalStateException e){
//                    //This is a specific error that happens when users abort their connection.
//                    logger.debug(e);
//                } catch (Exception e) {
//                    logger.error(e);
//                }
//                //Close the output stream
//                logger.debug("flash swf sent to client: bytesOutput="+bytesOutput);
//                outStream.close();
//            } catch (java.net.SocketException e){
//                logger.debug(e);
//            }
//        }
    }





    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyFlashServlet.class);
        String out = "";
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }

//        StringBuffer surveyashtml = new StringBuffer();
//        surveyashtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//        surveyashtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
//        surveyashtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
//        surveyashtml.append("<head>");
//        surveyashtml.append("<title>dNeero Survey</title>");
//        surveyashtml.append("</head>");
//        surveyashtml.append("<body>");
//        surveyashtml.append(SurveyAsHtml.getHtml(Survey.get(surveyid), User.get(userid), false));
//        surveyashtml.append("</body>");
//        surveyashtml.append("</html>");
//
//        //String surveyashtmlquotes = surveyashtml.toString().replaceAll("\\\"","\\\\\"");
//        String surveyashtmlquotes = surveyashtml.toString();
//        logger.debug("surveyashtmlquotes = "+surveyashtmlquotes);
//
//
//        String surveyashtmlencoded = surveyashtmlquotes;
//        try{surveyashtmlencoded = URLEncoder.encode(surveyashtmlquotes, "UTF-8");}catch(Exception ex){logger.error(ex); surveyashtmlencoded = surveyashtmlquotes;}

        String baseurlencoded = BaseUrl.get(false);
        try{baseurlencoded = URLEncoder.encode(baseurlencoded, "UTF-8");}catch(Exception ex){logger.error(ex); baseurlencoded = BaseUrl.get(false);}


        String urlofmovie = baseurl+"flashviewer/dneerosurvey.swf?s="+surveyid+"&u="+userid+"&ispreview="+ispreviewStr+"&baseurl="+baseurlencoded;

//        out = "<!-- Start dNeero Survey --><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"425\" height=\"250\" id=\"dneeroflashviewer\" align=\"middle\">" +
//              "<param name=\"allowScriptAccess\" value=\"never\" />" +
//              "<param name=\"movie\" value=\""+urlofmovie+"\"/>"+
//              "<param name=\"FlashVars\" value=\"surveyashtml="+surveyashtmlencoded+"\"/>"+
//              "<param name=\"quality\" value=\"high\" /><param name=\"bgcolor\" value=\"#ffffff\" />"+
//              "<embed src=\""+urlofmovie+"\" FlashVars=\"surveyashtml="+surveyashtmlencoded+"\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />" +
//              "</object><!-- End dNeero Survey -->";

        out = "<!-- Start dNeero Survey -->"+
              "<embed src=\""+urlofmovie+"\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"250\" name=\"dneeroflashviewer\" align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />" +
              "<!-- End dNeero Survey -->";

        return out;
    }


}
