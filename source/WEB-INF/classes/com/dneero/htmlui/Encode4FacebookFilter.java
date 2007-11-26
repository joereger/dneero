package com.dneero.htmlui;

import org.apache.log4j.Logger;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2007
 * Time: 2:14:46 AM
 */
public class Encode4FacebookFilter implements Filter {
    ServletContext sc = null;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("+++ doFilter() begin");
        // check that it is a HTTP request
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            // nonce encode the normal output
            Encode4FacebookResponseWrapper wrappedResponse = new Encode4FacebookResponseWrapper(response, sc);

            // make sure a session exists
            HttpSession session = request.getSession(true);


            chain.doFilter(req, wrappedResponse);

            // finish the response
            wrappedResponse.finishResponse();
        }
        logger.debug("+++ doFilter() end");
    }

    public void init(FilterConfig filterConfig) {
        // reference the context
        sc = filterConfig.getServletContext();
    }

    public void destroy() {
        // noop
    }
}
