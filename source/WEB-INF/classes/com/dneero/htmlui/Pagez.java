package com.dneero.htmlui;


import com.dneero.cache.providers.CacheFactory;
import com.dneero.systemprops.SystemProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

import java.net.URLEncoder;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 11:05:30 AM
 */
public class Pagez {

    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();
    private static ThreadLocal<UserSession> userSessionLocal = new ThreadLocal<UserSession>();
    private static ThreadLocal<BeanMgr> beanMgrLocal = new ThreadLocal<BeanMgr>();



    public static void setRequest(HttpServletRequest request){
        requestLocal.set(request);
    }

    public static void setResponse(HttpServletResponse response){
        responseLocal.set(response);
    }

    public static void sendRedirect(String url){
        sendRedirect(url, true);
    }

    public static void sendRedirect(String url, boolean doFancyDpageStuff){
        Logger logger = Logger.getLogger(Pagez.class);
        if (!Pagez.getUserSession().getIsfacebookui()){
            //Web ui
            url = responseLocal.get().encodeRedirectURL(url);
            if (!responseLocal.get().isCommitted()){
                responseLocal.get().reset();
                try{responseLocal.get().sendRedirect(url);}catch(Exception ex){logger.error("", ex);}
            }
        } else {
            //Facebookui
            try{
                ServletOutputStream out = responseLocal.get().getOutputStream();
                if (doFancyDpageStuff){
                    out.print("<fb:redirect url=\"http://apps.facebook.com/"+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/?dpage="+ URLEncoder.encode(url, "UTF-8")+"\"/>");
                } else {
                    out.print("<fb:redirect url=\""+url+"\"/>");
                }
            }catch(Exception ex){logger.error("", ex);}
        }
    }


    






    public static void setUserSessionAndUpdateCache(UserSession userSession){
        CacheFactory.getCacheProvider().put(getRequest().getSession().getId(), "userSession", userSession);
        userSessionLocal.set(userSession);
    }

    public static void setUserSession(UserSession userSession){
        userSessionLocal.set(userSession);
    }

    public static void setBeanMgr(BeanMgr beanMgr){
        beanMgrLocal.set(beanMgr);
    }

    public static HttpServletRequest getRequest(){
        return requestLocal.get();
    }

    public static HttpServletResponse getResponse(){
        return responseLocal.get();
    }

    public static UserSession getUserSession(){
        return userSessionLocal.get();
    }

    public static BeanMgr getBeanMgr(){
        return beanMgrLocal.get();
    }


}
