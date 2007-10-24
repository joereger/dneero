package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.systemprops.SystemProperty;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.facebook.FacebookPendingReferrals;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.session.ForceJavascriptRedirect;

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
    private String urltoredirectto = "";

    public PublicFacebookLandingPage(){
        load();
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        //NOTE: ANY REDIRECT ON THIS PAGE MUST USE appendFacebookStuff() TO PASS REQUEST VARS TO NEXT SCREEN
        //THIS IS BECAUSE IE SUCKS WIND AND CREATES A NEW SESSION NEXT PAGE... FIREFOX DOES NOT, AS WOULD BE EXPECTED

        //Make sure the app responds with the facebook ui
        Jsf.getUserSession().setIsfacebookui(true);

        logger.debug("into PublicFacebookLandingPage and isfacebookui="+Jsf.getUserSession().getIsfacebookui());

        //Note: Facebook only allows me to append a single var to the end of my url so I have to do some splitting crap to make things work.
        //The basic format I'm using is action-var1-var2-var3 where the vars are specific to each action.  It's crap but it works.

        //Need to record impressions
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
            RecordImpression.record(Jsf.getHttpServletRequest());
        }

        //Need to set referred by userid in the usersession
        try{
            if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                String[] split = Jsf.getRequestParam("action").split("-");
                if (split.length>=3){
                    //Set the referredbyuserid value in the session
                    if (split[2]!=null && Num.isinteger(split[2])){
                        Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(split[2]));
                    }
                }
            }
        } catch (Exception ex) {logger.error("",ex);}

        //Save referral state to the database if we have a facebookuid
        try{
            if (Jsf.getUserSession().getReferredbyOnlyUsedForSignup()>0){
                if (Jsf.getUserSession().getFacebookUser()!=null && Num.isinteger(Jsf.getUserSession().getFacebookUser().getUid())){
                    FacebookPendingReferrals.addReferredBy(Jsf.getUserSession().getReferredbyOnlyUsedForSignup(), Integer.parseInt(Jsf.getUserSession().getFacebookUser().getUid()));
                }
            }
        } catch (Exception ex){logger.error("",ex);}




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
                    //Notify admins
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
                    urltoredirectto = appendFacebookStuff("/survey.jsf?s="+split[1]+"&u="+split[2]+"&p=0");
                    try{Jsf.redirectResponse(urltoredirectto);return;}catch(Exception ex){logger.error("",ex);}
                    return;
                } else {
                    //If we see this code we may be displaying the app add page which means we'll need a link
                    try{
                        urltoredirectto = appendFacebookStuff("/facebookappadd.jsf");
                        try{Jsf.redirectResponse(urltoredirectto);return;}catch(Exception ex){logger.error("",ex);}
                        return;
                    } catch(Exception ex){logger.error("",ex);}
                }
            }
        }

        //Post add page
        if (Jsf.getRequestParam("addedapp")!=null && Jsf.getRequestParam("addedapp").equals("1")){
            //Notify admins
            if (Jsf.getUserSession().getFacebookUser()!=null){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user "+ Jsf.getUserSession().getFacebookUser().getFirst_name() + " " + Jsf.getUserSession().getFacebookUser().getLast_name() + " just added app!");
                xmpp.send();
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user Unknown just added app!");
                xmpp.send();
            }
            urltoredirectto = appendFacebookStuff("/publicsurveylist.jsf?addedapp=1");
            try{Jsf.redirectResponse(urltoredirectto);return;}catch(Exception ex){logger.error("",ex);}
            return;
        }

        //Redirect to the public survey list
        if (Jsf.getUserSession().getIsfacebookui() && Jsf.getUserSession().getFacebookUser()!=null && Jsf.getUserSession().getFacebookUser().getHas_added_app()){
            urltoredirectto = appendFacebookStuff("/publicsurveylist.jsf");
            try{Jsf.redirectResponse(urltoredirectto);return;}catch(Exception ex){logger.error("",ex);}
            return;
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

            //Notify admins
            logger.debug("Facebook app add page shown to user");
            logger.debug("Jsf.getUserSession().getIsfacebookui():"+Jsf.getUserSession().getIsfacebookui());
            if (Jsf.getUserSession().getFacebookUser()!=null){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to "+ Jsf.getUserSession().getFacebookUser().getFirst_name() + " " + Jsf.getUserSession().getFacebookUser().getLast_name() + " referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to Unknown:"+Jsf.getUserSession().getFacebookSessionKey()+" referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            }
            urltoredirectto = appendFacebookStuff("/facebookappadd.jsf");
            try{Jsf.redirectResponse(urltoredirectto);return;}catch(Exception ex){logger.error("",ex);}
            return;
        }catch(Exception ex){logger.error("",ex);}
    }


    private String appendFacebookStuff(String url){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (url.indexOf("?")<=-1){
            url = url + "?";
        }
        if (Jsf.getRequestParam("auth_token")!=null){
            url = url + "&auth_token=" + Jsf.getRequestParam("auth_token");
        }
        if (Jsf.getRequestParam("fb_sig_session_key")!=null){
            url = url + "&fb_sig_session_key=" + Jsf.getRequestParam("fb_sig_session_key");
        }
        if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
            url = url + "&action=" + Jsf.getRequestParam("action");
        }
        if (Jsf.getRequestParam("addedapp")!=null && Jsf.getRequestParam("addedapp").equals("1")){
            url = url + "&addedapp=1";
        }
        logger.debug("url = "+url);
        return url;
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }


    public String getUrltoredirectto() {
        return urltoredirectto;
    }

    public void setUrltoredirectto(String urltoredirectto) {
        this.urltoredirectto=urltoredirectto;
    }
}
