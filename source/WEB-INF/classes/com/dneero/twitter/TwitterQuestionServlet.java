package com.dneero.twitter;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;

import com.dneero.util.Num;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Feb 3, 2009
 * Time: 4:11:27 PM
 */
public class TwitterQuestionServlet extends HttpServlet {


    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("TwitterQuestionServlet called to service.");
        int twitaskid = 0;
        logger.debug("request.getRequestURI()="+request.getRequestURI());
        String[] split = request.getRequestURI().split("\\/");
        logger.debug("split.length="+split.length);
        if (split.length>=3){
            if (Num.isinteger(split[2])){
                twitaskid=Integer.parseInt(split[2]);
            }
        }
        if (twitaskid>0){
            logger.debug("redirecting to twitaskid="+twitaskid);
            Pagez.sendRedirect("/twitask.jsp?twitaskid="+twitaskid);
            return;
        } else {
            Pagez.getUserSession().setMessage("Sorry, that Twitter Question not found right now.");
            Pagez.sendRedirect("/index.jsp");
            return;
        }
        //request.getRequestDispatcher("/twitask.jsp?twitaskid="+twitaskid).forward(request, response);
        //return;
    }

}
