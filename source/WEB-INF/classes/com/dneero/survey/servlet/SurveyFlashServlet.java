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

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.systemprops.WebAppRootDir;

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

//        PrintWriter out = response.getWriter();

        Survey survey = null;
        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
            survey = Survey.get(Integer.parseInt(request.getParameter("s")));
        }
//
//        User user = null;
//        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
//            user = User.get(Integer.parseInt(request.getParameter("u")));
//        }
//
        boolean ispreview = false;
        if (request.getParameter("ispreview")!=null && com.dneero.util.Num.isinteger(request.getParameter("ispreview"))){
            if (request.getParameter("ispreview").equals("1")){
                ispreview = true;
            }
        }

        if (survey!=null && !ispreview){
            RecordImpression.record(request);
        }

//        StringBuffer output = new StringBuffer();
//        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
//                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
//                "\t<head>\n" +
//                "\t\t<title>dNeero Survey</title>\n" +
//                "\t</head>\n" +
//                "\t<body>");
//        output.append(SurveyAsHtml.getHtml(survey, user, true));
//        output.append("</body></html>");

        //Output the SWF file
        int bytesOutput = 0;
        try{
            //Get the SWF file from the hard disk
            File swffile = new File(WebAppRootDir.getWebAppRootPath() + "flashviewer/dneeroflashviewer.swf");
            if (!swffile.canRead()) {
                logger.error("Can't find dneeroflashviewer.swf at "+swffile.getAbsolutePath());
            }
            // Use getOutputStream instead of getWriter
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("application/x-shockwave-flash");
            try{
                // Creating a new FileInputStream object
                FileInputStream fis = new FileInputStream(swffile);
                if (fis != null){
                    byte[] data = new byte[4096];
                    try{
                        int bytesRead;
                        while ((bytesRead = fis.read(data)) != -1){
                            bytesOutput = bytesOutput + bytesRead;
                            outStream.write(data, 0, bytesRead);
                        }
                        fis.close();
                    }catch (java.net.SocketException e){
                        //Do nothing... typically a ClientAbortException
                    }catch (IOException e){
                        logger.debug("IO Exception attempting to read file: '" + swffile.getAbsolutePath() + "'<br>" + e.toString());
                        logger.debug(e);
                    }catch (Throwable e){
                        logger.error(e);
                    }
                }
            } catch (IOException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (java.lang.IllegalStateException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (Exception e) {
                logger.error(e);
            }
            //Close the output stream
            logger.debug("flash swf sent to client: bytesOutput="+bytesOutput);
            outStream.close();
        } catch (java.net.SocketException e){
            logger.debug(e);
        }
    }


}
