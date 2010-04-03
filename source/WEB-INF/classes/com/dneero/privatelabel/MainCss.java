package com.dneero.privatelabel;

import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

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
public class MainCss extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());

        //Get the css from /default or db
        String out = PlTemplate.getMaincss(Pagez.getUserSession().getPl());

        //Send it on along now y'hear
        ServletOutputStream outStream = response.getOutputStream();
        response.setContentType("text/css");
        outStream.write(out.getBytes());
        outStream.close();
    }






}