package com.dneero.formbeans;

import com.dneero.util.Jsf;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 13, 2007
 * Time: 5:03:27 PM
 */
public class PublicFacebookLandingPage implements Serializable {

    private String dummy = "";

    public PublicFacebookLandingPage(){
        load();
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{Jsf.redirectResponse("/publicsurveylist.jsf");}catch(Exception ex){logger.error(ex);}
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
