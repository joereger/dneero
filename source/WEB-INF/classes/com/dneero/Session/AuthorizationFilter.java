package com.dneero.session;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Iterator;

import com.dneero.dao.Userrole;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 4:59:32 PM
 */
public class AuthorizationFilter implements Filter{

    FilterConfig config = null;
    ServletContext servletContext = null;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public AuthorizationFilter(){

    }

    public void init(FilterConfig filterConfig) throws ServletException{
        config = filterConfig;
        servletContext = config.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpSession session = httpRequest.getSession();
        String requestPath = httpRequest.getPathInfo();

        logger.debug("Auth filter called for requestPath="+requestPath+" httpRequest.getServletPath()="+httpRequest.getServletPath()+" sessionid="+session.getId());
        UserSession userSession = (UserSession)session.getAttribute(UserSession.SESSIONLOOKUPKEY);
        if (userSession==null){
            userSession = new UserSession();
            session.setAttribute(UserSession.SESSIONLOOKUPKEY, userSession);
        }

        boolean isAuthorized = false;
        boolean isPageprotected = false;

        if (requestPath!=null && requestPath.indexOf("blogger")>0){
            isPageprotected = true;
            for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()==Roles.BLOGGER){
                    logger.debug("Blogger authorized.");
                    isAuthorized = true;
                }
            }
        }
        if (requestPath!=null && requestPath.indexOf("researcher")>0){
            isPageprotected = true;
            for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()==Roles.RESEARCHER){
                    logger.debug("Researcher authorized.");
                    isAuthorized = true;
                }
            }
        }

        if (requestPath!=null && requestPath.indexOf("account")>0){
            isPageprotected = true;
            for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()!=Roles.ANONYMOUS){
                    logger.debug("Non-anonymous authorized for common account section.");
                    isAuthorized = true;
                }
            }
        }

        if (!isAuthorized && isPageprotected){
            logger.debug("Redirecting user because they fail authorization.");
            httpResponse.sendRedirect("/login.jsp");
        }

        logger.debug("User passes auth.  requestPath="+requestPath + " sessionid="+session.getId());
        chain.doFilter(request, response);

    }

    public void destroy(){

    }

}
