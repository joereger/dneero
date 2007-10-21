package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.systemprops.BaseUrl;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Aug 28, 2007
 * Time: 10:54:28 AM
 */
public class PublicFacebookexitui implements Serializable {

    private String dummy="";

    public PublicFacebookexitui(){
        load();
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Clear the isfacebookui flag
        Jsf.getUserSession().leaveFacebookui();
        try{Jsf.redirectResponse(BaseUrl.get(false));}catch(Exception ex){logger.error("",ex);}
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
