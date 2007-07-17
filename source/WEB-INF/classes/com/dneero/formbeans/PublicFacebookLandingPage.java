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

        //Make sure the app responds with the facebook ui
        Jsf.getUserSession().setIsfacebookui(true);

        //If we should display a specific survey, do so
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
            String[] split = Jsf.getRequestParam("action").split("-");
            if (split.length>=3){
                //try{Jsf.redirectResponse("/survey.jsf?surveyid="+split[1]+"&userid="+split[2]);return;}catch(Exception ex){logger.error(ex);}
                try{Jsf.redirectResponse("/publicsurveylist.jsf");return;}catch(Exception ex){logger.error(ex);}
            }
        }


        //Redirect to the public survey list
        try{Jsf.redirectResponse("/publicsurveylist.jsf");return;}catch(Exception ex){logger.error(ex);}
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
