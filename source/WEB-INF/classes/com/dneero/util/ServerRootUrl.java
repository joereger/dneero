package com.dneero.util;

import javax.servlet.ServletConfig;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 12:54:56 PM
 */
public class ServerRootUrl {

    private static String serverRootUrl = "http://dneero.com/";



    public ServerRootUrl(javax.servlet.http.HttpServletRequest request){
         //serverRootUrl = request.getSession().getServletContext().getRealPath("/");
    }

    public ServerRootUrl(ServletConfig config){
        //serverRootUrl = config.getServletContext().getRealPath("/");
    }

    public static String getServerRootUrl() {
        return serverRootUrl;
    }




}
