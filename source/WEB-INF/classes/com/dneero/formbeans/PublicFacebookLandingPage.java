package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.systemprops.SystemProperty;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 13, 2007
 * Time: 5:03:27 PM
 */
public class PublicFacebookLandingPage implements Serializable {

    private String dummy = "";
    private String addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY);

    public PublicFacebookLandingPage(){
        load();
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        //Make sure the app responds with the facebook ui
        Jsf.getUserSession().setIsfacebookui(true);

        logger.debug("into PublicFacebookLandingPage and isfacebookui="+Jsf.getUserSession().getIsfacebookui() +" and isfacebookappadded="+Jsf.getUserSession().getIsfacebookappadded());

        //Note: Facebook only allows me to append a single var to the end of my url so I have to do some splitting crap to make things work.
        //The basic format I'm using is action-var1-var2-var3 where the vars are specific to each action.  It's crap but it works.

        //If we should display a specific survey, do so
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
            String[] split = Jsf.getRequestParam("action").split("-");
            if (split.length>=3){
                try{Jsf.redirectResponse("/survey.jsf?s="+split[1]+"&u="+split[2]+"&p=0&permitbeforefacebookappadd=1");return;}catch(Exception ex){logger.error(ex);}
            }
        }

        //Redirect to the public survey list
        if (Jsf.getUserSession().getIsfacebookui() && Jsf.getUserSession().getIsfacebookappadded()){
            try{Jsf.redirectResponse("/publicsurveylist.jsf");return;}catch(Exception ex){logger.error(ex);}
        }
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getAddurl() {
        return addurl;
    }

    public void setAddurl(String addurl) {
        this.addurl = addurl;
    }
}
