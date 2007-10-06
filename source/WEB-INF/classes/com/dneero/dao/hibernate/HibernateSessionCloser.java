package com.dneero.dao.hibernate;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

import com.dneero.session.UrlSplitter;
import com.dneero.session.PersistentLogin;
import com.dneero.session.UserSession;
import com.dneero.session.SurveysTakenToday;
import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.facebook.FacebookAuthorization;
import com.dneero.dao.User;
import com.dneero.eula.EulaHelper;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.formbeans.LoginAgreeNewEula;

/**
 * User: Joe Reger Jr
 * Date: Jul 18, 2006
 * Time: 9:50:38 AM
 */
public class HibernateSessionCloser implements Filter {

    private FilterConfig filterConfig = null;
    Logger logger = Logger.getLogger(this.getClass().getName());
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("------");
                logger.debug("-------------");
                logger.debug("---------------------------START REQUEST: "+httpServletRequest.getRequestURL());
            }
        }catch(Exception ex){logger.debug(ex);}

        //Call the rest of the filters
        chain.doFilter(request, response);

        //Close the Hibernate session
        try{
            HibernateUtil.closeSession();
        } catch (Exception ex){
            logger.debug("Error closing hibernate session at end of request.");
            logger.error(ex);
        }

        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("---------------------------END REQUEST: "+httpServletRequest.getRequestURL());
                logger.debug("-------------");
                logger.debug("------");
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("");
            }
        }catch(Exception ex){logger.debug(ex);}
    }

}
