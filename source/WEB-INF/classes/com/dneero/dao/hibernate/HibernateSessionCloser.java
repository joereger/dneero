package com.dneero.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

import com.dneero.htmlui.Pagez;
import com.dneero.util.GeneralException;

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
                Pagez.setStartTime(new Date().getTime());
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("------");
                logger.debug("-------------");
                logger.debug("---------------------------START REQUEST: "+httpServletRequest.getRequestURL());
                logger.debug("httpServletRequest.getSession().getId()="+httpServletRequest.getSession().getId());
                //Start Hibernate Session
                //HibernateUtil.startSession();
            }
        }catch(Exception ex){logger.error("", ex);}

        //Call the rest of the filters
        chain.doFilter(request, response);

        //Close the Hibernate session
        try{
            HibernateUtil.closeSession();
            HibernateUtilDbcache.closeSession();
            HibernateUtilImpressions.closeSession();
        } catch (Exception ex){
            logger.error("Error closing hibernate session at end of request.",ex);
        }

        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                //End Hibernate Session
                //HibernateUtil.endSession();
                logger.debug("---------------------------END REQUEST: "+httpServletRequest.getRequestURL());
                logger.debug("-------------: "+Pagez.getElapsedTime()+" millis");
                logger.debug("------");
                logger.debug("");
                logger.debug("");
                logger.debug("");
                logger.debug("");
            }
        }catch(Exception ex){logger.error("", ex);}
    }

}
