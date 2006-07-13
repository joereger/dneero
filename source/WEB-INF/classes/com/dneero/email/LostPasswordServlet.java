package com.dneero.email;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.dneero.dao.User;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;

/**
 * Serves a captcha image
 */
public class LostPasswordServlet extends HttpServlet {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doPost() -> request.getParameter(\"u\")="+request.getParameter("u")+" request.getParameter(\"k\")="+request.getParameter("k"));

        User user=null;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            user = User.get(Integer.parseInt(request.getParameter("u")));
        }

        String emailactivationkey="";
        if (request.getParameter("k")!=null){
            emailactivationkey = request.getParameter("k");
        }

        logger.debug("user.getUserid()="+user.getUserid()+" emailactivationkey="+emailactivationkey);

        if (user!=null && user.getEmailactivationkey().equals(emailactivationkey)){
            user.setIsactivatedbyemail(true);
            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            //Set the flag in the session that'll allow this user to reset their password
            UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");

            userSession.setAllowedToResetPasswordBecauseHasValidatedByEmail(true);
            userSession.setUser(user);

            response.sendRedirect("/lostpasswordchoose.jsf");
            return;
        } else {
            response.sendRedirect("/lostpassword.jsf");
            return;
        }


    }
}
