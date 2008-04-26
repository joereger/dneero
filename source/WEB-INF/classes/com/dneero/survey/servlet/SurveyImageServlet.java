package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.systemprops.WebAppRootDir;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyImageServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Looking for image survey via servlet");
        logger.debug("request.getParameter(\"s\")="+request.getParameter("s"));
        logger.debug("request.getParameter(\"u\")="+request.getParameter("u"));
        logger.debug("request.getParameter(\"p\")="+request.getParameter("p"));

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
            //Don't record just for a link
            //RecordImpression.record(request);
        }


        //Output the image file
        int bytesOutput = 0;
        try{
            //Get the image file from the hard disk
            File imageFile = new File(WebAppRootDir.getWebAppRootPath() + "images/image-embed.gif");
            if (!imageFile.canRead()) {
                logger.error("Can't find dneero-logo.gif at "+imageFile.getAbsolutePath());
            }
            // Use getOutputStream instead of getWriter
            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("image/gif");
            try{
                // Creating a new FileInputStream object
                FileInputStream fis = new FileInputStream(imageFile);
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
                        logger.debug("IO Exception attempting to read file: '" + imageFile.getAbsolutePath() + "'<br>" + e.toString());
                        logger.debug(e);
                    }catch (Throwable e){
                        logger.error("", e);
                    }
                }
            } catch (IOException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (java.lang.IllegalStateException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (Exception e) {
                logger.error("", e);
            }
            //Close the output stream
            logger.debug("sent to client: bytesOutput="+bytesOutput);
            outStream.close();
        } catch (java.net.SocketException e){
            logger.debug(e);
        }

    }



    public static String getEmbedSyntax(String baseurl, int surveyid, int userid, int responseid, boolean ispreview){
        Logger logger = Logger.getLogger(SurveyImageServlet.class);
        String out = "";
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }

        String urlofimage = baseurl+"i?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&r="+responseid;
        String urlofsurvey = baseurl+"shtml?s="+surveyid+"&u="+userid+"&p="+ispreviewStr+"&r="+responseid;

        out = "<!-- Start dNeero Conversation -->"+
              "<a href=\""+urlofsurvey+"\">" +
              "<img src=\""+urlofimage+"\" border=\"0\"/>" +
              "</a>"+
              "<!-- End dNeero Conversation -->";

        return out;
    }


}
