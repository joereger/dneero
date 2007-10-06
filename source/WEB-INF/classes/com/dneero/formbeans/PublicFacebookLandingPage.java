package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.systemprops.SystemProperty;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.dao.User;
import com.dneero.dao.Survey;

import java.io.Serializable;
import java.net.URLEncoder;

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

        logger.debug("into PublicFacebookLandingPage and isfacebookui="+Jsf.getUserSession().getIsfacebookui());

        //Note: Facebook only allows me to append a single var to the end of my url so I have to do some splitting crap to make things work.
        //The basic format I'm using is action-var1-var2-var3 where the vars are specific to each action.  It's crap but it works.

        //Post add page
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("addedapp")>-1){
            try{Jsf.redirectResponse("/publicsurveylist.jsf?action=addedapp");return;}catch(Exception ex){logger.error(ex);}
        }

        //If we should display a specific survey, do so
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
            String[] split = Jsf.getRequestParam("action").split("-");
            if (split.length>=3){
                //Set the referredbyuserid value in the session
                if (split[2]!=null && Num.isinteger(split[2])){
                    Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(split[2]));
                    String referredbyname = "";
                    if (split[2]!=null && Num.isinteger(split[2])){
                        User userReferer = User.get(Integer.parseInt(split[2]));
                        referredbyname = userReferer.getFirstname()+" "+userReferer.getLastname();
                    }
                    String surveytitle = "";
                    if (split[1]!=null && Num.isinteger(split[1])){
                        Survey surveyTmp = Survey.get(Integer.parseInt(split[1]));
                        surveytitle = surveyTmp.getTitle();
                    }
                    if (Jsf.getUserSession().getFacebookUser()!=null){
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user "+ Jsf.getUserSession().getFacebookUser().getFirst_name() + " " + Jsf.getUserSession().getFacebookUser().getLast_name() + " referred by userid="+split[2]+" ("+referredbyname+") to surveyid="+split[1]+" ("+surveytitle+")");
                        xmpp.send();
                    } else {

                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user Unknown:"+Jsf.getUserSession().getFacebookSessionKey()+" referred by userid="+split[2]+" ("+referredbyname+") to surveyid="+split[1]+" ("+surveytitle+")");
                        xmpp.send();
                    }
                }
                //If the user has the app added, redirect to the survey
                if (Jsf.getUserSession().getIsfacebookui() &&  Jsf.getUserSession().getFacebookUser()!=null && Jsf.getUserSession().getFacebookUser().getHas_added_app()){
                    try{Jsf.redirectResponse("/survey.jsf?s="+split[1]+"&u="+split[2]+"&p=0&show=showSurveyResponseFlashEmbed");return;}catch(Exception ex){logger.error(ex);}
                }
                //If we see this code we may be displaying the app add page which means we'll need a link
                try{
                    String next = URLEncoder.encode("?action=showsurvey"+"-"+split[1]+"-"+split[2], "UTF-8");
                    addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY) + "&next="+next;
                } catch(Exception ex){logger.error(ex);}
            }
        }

        //Redirect to the public survey list
        if (Jsf.getUserSession().getIsfacebookui() && Jsf.getUserSession().getFacebookUser()!=null && Jsf.getUserSession().getFacebookUser().getHas_added_app()){
            try{Jsf.redirectResponse("/publicsurveylist.jsf");return;}catch(Exception ex){logger.error(ex);}
        }

        //User's gonna see the add app page we generate... just debug notification here
        try{
            String referredbyuserid = "";
            String referredtosurveyid = "";
            if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                String[] split = Jsf.getRequestParam("action").split("-");
                if (split.length>=3){
                    referredbyuserid = split[2];
                    referredtosurveyid = split[1];
                }
            }
            if (Jsf.getUserSession().getFacebookUser()!=null){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to "+ Jsf.getUserSession().getFacebookUser().getFirst_name() + " " + Jsf.getUserSession().getFacebookUser().getLast_name() + " referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to Unknown:"+Jsf.getUserSession().getFacebookSessionKey()+" referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            }
        }catch(Exception ex){logger.error(ex);}




       
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
