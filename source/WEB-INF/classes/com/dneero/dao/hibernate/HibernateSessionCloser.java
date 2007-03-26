package com.dneero.dao.hibernate;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        try{
            logger.debug("");
            logger.debug("");
            logger.debug("");
            logger.debug("");
            logger.debug("------");
            logger.debug("-------------");
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            logger.debug("---------------------------START REQUEST: "+httpServletRequest.getRequestURL());
        }catch(Exception ex){logger.debug(ex);}

        chain.doFilter(request, response);
        try{
            HibernateUtil.closeSession();
        } catch (Exception ex){
            logger.debug("Error closing hibernate session at end of request.");
            logger.error(ex);
        }

        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            logger.debug("---------------------------END REQUEST: "+httpServletRequest.getRequestURL());
            logger.debug("-------------");
            logger.debug("------");
            logger.debug("");
            logger.debug("");
            logger.debug("");
            logger.debug("");
        }catch(Exception ex){logger.debug(ex);}
    }

}
