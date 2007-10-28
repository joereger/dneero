package com.dneero.htmlui;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Calendar;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.session.UrlSplitter;
import com.dneero.session.PersistentLogin;
import com.dneero.session.SurveysTakenToday;
import com.dneero.dao.User;
import com.dneero.util.Time;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.facebook.FacebookAuthorizationJsp;
import com.dneero.eula.EulaHelper;
import com.dneero.xmpp.SendXMPPMessage;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 4:24:22 PM
 */
public class FilterMain implements Filter {

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
        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("Start FilterMain");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("------");
//                logger.debug("-------------");
//                logger.debug("---------------------------START REQUEST: "+httpServletRequest.getRequestURL());
//                logger.debug("httpServletRequest.getSession().getId()="+httpServletRequest.getSession().getId());

                //Set up Pagez
                Pagez.setRequest(httpServletRequest);
                Pagez.setResponse(httpServletResponse);
                Pagez.setBeanMgr(new BeanMgr());
                Object obj = CacheFactory.getCacheProvider().get(httpServletRequest.getSession().getId(), "userSessionNew");
                if (obj!=null && (obj instanceof UserSession)){
                    logger.debug("found a userSession in the cache");
                    Pagez.setUserSession((UserSession)obj);
                } else {
                    logger.debug("no userSession in cache");
                    UserSession userSession = new UserSession();
                    Pagez.setUserSessionAndUpdateCache(userSession);
                }

                //Production redirect to www.dneero.com for https
                //@todo make this configurable... i.e. no hard-coded urls
                UrlSplitter urlSplitter = new UrlSplitter(httpServletRequest);
                if (urlSplitter.getRawIncomingServername().equals("dneero.com")){
                    if (urlSplitter.getMethod().equals("GET")){
                        httpServletResponse.sendRedirect(urlSplitter.getScheme()+"://"+"www.dneero.com"+urlSplitter.getServletPath()+urlSplitter.getParametersAsQueryStringQuestionMarkIfRequired());
                        return;
                    } else {
                        httpServletResponse.sendRedirect(urlSplitter.getScheme()+"://"+"www.dneero.com/");
                        return;
                    }
                }

                //Redirect login page to https
                if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1") && urlSplitter.getScheme().equals("http") && urlSplitter.getServletPath().equals("login.jsf")){
                    try{
                        httpServletResponse.sendRedirect(BaseUrl.get(true)+"jsp/login.jsp");
                        return;
                    } catch (Exception ex){
                        logger.error("",ex);
                        return;
                    }
                }

                //Facebook start
                FacebookAuthorizationJsp.doAuth();
                //Facebook end

                logger.debug("before persistent login and isfacebookui="+Pagez.getUserSession().getIsfacebookui());

                //Persistent login start
                boolean wasAutoLoggedIn = false;
                if (!Pagez.getUserSession().getIsfacebookui()){
                    if (!Pagez.getUserSession().getIsloggedin()){
                        //See if the incoming request has a persistent login cookie
                        Cookie[] cookies = httpServletRequest.getCookies();
                        logger.debug("looking for cookies");
                        if (cookies!=null && cookies.length>0){
                            logger.debug("cookies found.");
                            for (int i = 0; i < cookies.length; i++) {
                                if (cookies[i].getName().equals(PersistentLogin.cookieName)){
                                    logger.debug("persistent cookie found.");
                                    int useridFromCookie = PersistentLogin.checkPersistentLogin(cookies[i]);
                                    if (useridFromCookie>-1){
                                        logger.debug("setting userid="+useridFromCookie);
                                        User user = User.get(useridFromCookie);
                                        if (user!=null && user.getUserid()>0 && user.getIsenabled()){
                                            UserSession newUserSession = new UserSession();
                                            newUserSession.setUser(user);
                                            newUserSession.setIsloggedin(true);
                                            newUserSession.setIsLoggedInToBeta(true);
                                            newUserSession.setPendingSurveyReferredbyuserid(Pagez.getUserSession().getPendingSurveyReferredbyuserid());
                                            newUserSession.setPendingSurveyResponseAsString(Pagez.getUserSession().getPendingSurveyResponseAsString());
                                            newUserSession.setPendingSurveyResponseSurveyid(Pagez.getUserSession().getPendingSurveyResponseSurveyid());
                                            newUserSession.setCurrentSurveyid(Pagez.getUserSession().getCurrentSurveyid());
                                            newUserSession.setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                                            //Check the eula
                                            if (!EulaHelper.isUserUsingMostRecentEula(user)){
                                                newUserSession.setIseulaok(false);
                                            } else {
                                                newUserSession.setIseulaok(true);
                                            }
                                            Pagez.setUserSessionAndUpdateCache(newUserSession);
                                            wasAutoLoggedIn = true;
                                            //Notify via XMPP
                                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Auto-Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                                            xmpp.send();
                                            //Now dispatch request to the same page so that header is changed to reflect logged-in status
//                                            if (wasAutoLoggedIn){
//                                                try{
//                                                    if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
//                                                        try{
//                                                            httpServletResponse.sendRedirect(BaseUrl.get(true)+"jsp/account/index.jsp?msg=autologin");
//                                                            return;
//                                                        } catch (Exception ex){
//                                                            logger.error("",ex);
//                                                            httpServletResponse.sendRedirect("/jsp/account/index.jsp?msg=autologin");
//                                                            return;
//                                                        }
//                                                    } else {
//                                                        httpServletResponse.sendRedirect("/jsp/account/index.jsp?msg=autologin");
//                                                        return;
//                                                    }
//                                                } catch (Exception ex){
//                                                    logger.error("",ex);
//                                                    ex.printStackTrace();
//                                                    httpServletResponse.sendRedirect("/jsp/index.jsp");
//                                                    return;
//                                                }
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //Persistent login end

                logger.debug("after persistent login and isfacebookui="+Pagez.getUserSession().getIsfacebookui());


                //Account activation
                if (Pagez.getUserSession().getUser()!=null && !Pagez.getUserSession().getUser().getIsactivatedbyemail()){
                    //User isn't activated but they get a grace period
                    int daysInGracePeriod = 3;
                    Calendar startOfGracePeriod = Time.xDaysAgoStart(Calendar.getInstance(), daysInGracePeriod);
                    if (Pagez.getUserSession().getUser().getCreatedate().before(startOfGracePeriod.getTime())){
                        if (urlSplitter.getRequestUrl().indexOf("emailactivationwaiting.jsp")==-1){
                            httpServletResponse.sendRedirect("/jsp/emailactivationwaiting.jsp");
                            return;
                        }
                    }
                }

                //Now check the eula
                if (Pagez.getUserSession().getIsloggedin() && !Pagez.getUserSession().getIseulaok()){
                    System.out.println("redirecting to force eula accept");
                    if (urlSplitter.getRequestUrl().indexOf("loginagreeneweula.jsp")==-1){
                        httpServletResponse.sendRedirect("/jsp/loginagreeneweula.jsp");
                        return;
                    }
                }
        

            }
        }catch(Exception ex){logger.error("", ex);}

        //Call the rest of the filters
        chain.doFilter(request, response);

        try{
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
//                logger.debug("---------------------------END REQUEST: "+httpServletRequest.getRequestURL());
//                logger.debug("-------------");
//                logger.debug("------");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
//                logger.debug("");
                logger.debug("End FilterMain");
            }
        }catch(Exception ex){logger.error("", ex);}
    }

}
