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
import com.dneero.systemprops.WebAppRootDir;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class DengSwf extends HttpServlet {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Looking for deng swf");


        //Output the SWF file
        int bytesOutput = 0;
        try{
            //Get the SWF file from the hard disk
            File swffile = new File(WebAppRootDir.getWebAppRootPath() + "flashviewer/deng.swf");
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
                        logger.error("", e);
                    }
                }
            } catch (IOException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (IllegalStateException e){
                //This is a specific error that happens when users abort their connection.
                logger.debug(e);
            } catch (Exception e) {
                logger.error("", e);
            }
            //Close the output stream
            logger.debug("flash swf sent to client: bytesOutput="+bytesOutput);
            outStream.close();
        } catch (java.net.SocketException e){
            logger.debug(e);
        }
    }


}
