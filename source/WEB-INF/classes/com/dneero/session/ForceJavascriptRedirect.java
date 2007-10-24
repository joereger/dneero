package com.dneero.session;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 24, 2007
 * Time: 12:48:54 AM
 */
public class ForceJavascriptRedirect implements Serializable {

    private String urltoredirectto = "";


    public String getUrltoredirectto() {
        return urltoredirectto;
    }

    public void setUrltoredirectto(String urltoredirectto) {
        this.urltoredirectto=urltoredirectto;
    }
}
