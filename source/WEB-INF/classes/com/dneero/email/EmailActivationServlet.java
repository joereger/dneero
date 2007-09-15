package com.dneero.email;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.awt.image.BufferedImage;

import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.systemprops.SystemProperty;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * Serves a captcha image
 */
public class EmailActivationServlet extends HttpServlet {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        User user=null;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            user = User.get(Integer.parseInt(request.getParameter("u")));
        }

        String emailactivationkey="";
        if (request.getParameter("k")!=null){
            emailactivationkey = request.getParameter("k");
        }

        if (user!=null && user.getEmailactivationkey().trim().equals(emailactivationkey.trim())){
            user.setIsactivatedbyemail(true);
            try{user.save();} catch (Exception ex){logger.error(ex);}

            //@todo send a welcome email message after successful email activation
            if(user.getFacebookuserid()<=0){
                response.sendRedirect("/emailactivationsuceed.jsf");
                return;
            } else {
                response.sendRedirect("http://apps.facebook.com/"+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/");
                return;
            }
        } else {
            response.sendRedirect("/emailactivationfail.jsf");
            return;
        }


    }
}
