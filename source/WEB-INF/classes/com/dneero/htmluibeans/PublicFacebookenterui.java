package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;

import com.dneero.util.Jsf;
import com.dneero.systemprops.BaseUrl;
import com.dneero.systemprops.SystemProperty;

/**
 * User: Joe Reger Jr
 * Date: Aug 28, 2007
 * Time: 10:54:28 AM
 */
public class PublicFacebookenterui implements Serializable {

    private String dummy="";
    private String url = "";

    public PublicFacebookenterui(){

    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Clear the isfacebookui flag
        String action = "";
        if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            action = "?action="+Pagez.getRequest().getParameter("action");
        }
        url = "http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/"+action;
        try{Pagez.sendRedirect(url);}catch(Exception ex){logger.error("",ex);}
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }
}
