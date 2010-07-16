package com.dneero.htmlui;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 4:24:22 PM
 */
public class JspOverrideFilter implements Filter {

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
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        //Set up Pagez
        Pagez.setRequest(httpServletRequest);
        Pagez.setResponse(httpServletResponse);
        Pagez.setBeanMgr(new BeanMgr());
        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("Start JspOverrideFilter");
            }
        }catch(Exception ex){logger.error("", ex);}


        //logger.debug("httpServletRequest.getRequestURI()="+httpServletRequest.getRequestURI());
        //logger.debug("httpServletRequest.getServletPath()="+httpServletRequest.getServletPath());
        //logger.debug("httpServletRequest.getPathTranslated()="+httpServletRequest.getLocalName());

        //Call the rest of the filters
        chain.doFilter(request, response);

        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("End JspOverrideFilter");
            }
        }catch(Exception ex){logger.error("", ex);}
    }



   



}