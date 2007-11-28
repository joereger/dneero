package com.dneero.htmlui;

import com.dneero.facebook.FacebookUser;
import com.dneero.dao.User;
import com.dneero.dao.Userrole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 12:51:06 PM
 */
public class UserSession implements Serializable {

    //Note: Only use primitives to simplify clustering.
    //Example: userid as int and then getUser() calls on the clustered/cached hibernate layer.

    private int userid;
    private boolean isloggedin = false;
    private int currentSurveyid;
    private boolean isAllowedToResetPasswordBecauseHasValidatedByEmail = false;
    private int referredbyOnlyUsedForSignup = 0;
    private boolean isSysadmin = false;
    private boolean isLoggedInToBeta = false;
    private int pendingSurveyResponseSurveyid = 0;
    private String pendingSurveyResponseAsString = "";
    private int pendingSurveyReferredbyuserid = 0;
    private boolean iseulaok = true;
    private int emailinvitesurveyiduserisinvitedto = 0;
    private String emailinvitesubject = "";
    private String emailinvitemessage = "";
    private ArrayList<String> emailinviteaddresses = new ArrayList<String>();
    private int surveystakentoday = 0;
    private FacebookUser facebookUser = null;
    private String facebookSessionKey = "";
    private ArrayList<FacebookUser> facebookFriends = null;
    private boolean isfacebookui = false;
    private String message = "";

    public UserSession(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("New UserSession created.");
        try{
            logger.debug("trying to set isfacebookui in UserSession");
            //Set the userSession var isfacebookui if we see anything that remotely resembles facebook activity
            if (Pagez.getRequest().getParameter("auth_token")!=null){
                logger.debug("setting isfacebook=true because of auth_token request param");
                isfacebookui=true;
            }
            if (Pagez.getRequest().getParameter("fb_sig_session_key")!=null){
                logger.debug("setting isfacebook=true because of fb_sig_session_key request param");
                isfacebookui=true;
            }
            if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
                logger.debug("setting isfacebook=true because of showsurvey request param");
                isfacebookui=true;
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        logger.debug("isfacebookui="+isfacebookui);
    }

    public User getUser() {
        if (userid>0){
            return User.get(userid);
        } else {
            return null;
        }
    }

    public void leaveFacebookui(){
        isfacebookui = false;
        facebookSessionKey = "";
        isloggedin = false;
        userid = 0;
    }


    public void setUser(User user) {
        if (user!=null){
            userid = user.getUserid();
            isSysadmin = false;
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    isSysadmin = true;
                }
            }
        } else {
            userid = 0;
        }
    }

    public boolean getIsloggedin() {
        return isloggedin;
    }

    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }

    public int getCurrentSurveyid() {
        return currentSurveyid;
    }

    public void setCurrentSurveyid(int currentSurveyid) {
        this.currentSurveyid = currentSurveyid;
    }

    public boolean getIsAllowedToResetPasswordBecauseHasValidatedByEmail() {
        return isAllowedToResetPasswordBecauseHasValidatedByEmail;
    }

    public void setAllowedToResetPasswordBecauseHasValidatedByEmail(boolean allowedToResetPasswordBecauseHasValidatedByEmail) {
        isAllowedToResetPasswordBecauseHasValidatedByEmail = allowedToResetPasswordBecauseHasValidatedByEmail;
    }

    public int getReferredbyOnlyUsedForSignup() {
        return referredbyOnlyUsedForSignup;
    }

    public void setReferredbyOnlyUsedForSignup(int referredbyOnlyUsedForSignup) {
        this.referredbyOnlyUsedForSignup = referredbyOnlyUsedForSignup;
    }

    public boolean getIsSysadmin() {
        return isSysadmin;
    }

    public void setSysadmin(boolean sysadmin) {
        isSysadmin = sysadmin;
    }

    public boolean getIsLoggedInToBeta() {
        return isLoggedInToBeta;
    }

    public void setIsLoggedInToBeta(boolean isLoggedInToBeta) {
        this.isLoggedInToBeta = isLoggedInToBeta;
    }

    public int getPendingSurveyResponseSurveyid() {
        return pendingSurveyResponseSurveyid;
    }

    public void setPendingSurveyResponseSurveyid(int pendingSurveyResponseSurveyid) {
        this.pendingSurveyResponseSurveyid = pendingSurveyResponseSurveyid;
    }

    public String getPendingSurveyResponseAsString() {
        return pendingSurveyResponseAsString;
    }

    public void setPendingSurveyResponseAsString(String pendingSurveyResponseAsString) {
        this.pendingSurveyResponseAsString = pendingSurveyResponseAsString;
    }


    public int getPendingSurveyReferredbyuserid() {
        return pendingSurveyReferredbyuserid;
    }

    public void setPendingSurveyReferredbyuserid(int pendingSurveyReferredbyuserid) {
        this.pendingSurveyReferredbyuserid = pendingSurveyReferredbyuserid;
    }

    public boolean getIseulaok() {
        return iseulaok;
    }

    public void setIseulaok(boolean iseulaok) {
        this.iseulaok = iseulaok;
    }


    public ArrayList<String> getEmailinviteaddresses() {
        return emailinviteaddresses;
    }

    public void setEmailinviteaddresses(ArrayList<String> emailinviteaddresses) {
        this.emailinviteaddresses = emailinviteaddresses;
    }

    public int getEmailinvitesurveyiduserisinvitedto() {
        return emailinvitesurveyiduserisinvitedto;
    }

    public void setEmailinvitesurveyiduserisinvitedto(int emailinvitesurveyiduserisinvitedto) {
        this.emailinvitesurveyiduserisinvitedto = emailinvitesurveyiduserisinvitedto;
    }

    public String getEmailinvitesubject() {
        return emailinvitesubject;
    }

    public void setEmailinvitesubject(String emailinvitesubject) {
        this.emailinvitesubject = emailinvitesubject;
    }

    public String getEmailinvitemessage() {
        return emailinvitemessage;
    }

    public void setEmailinvitemessage(String emailinvitemessage) {
        this.emailinvitemessage = emailinvitemessage;
    }

    public int getSurveystakentoday() {
        return surveystakentoday;
    }

    public void setSurveystakentoday(int surveystakentoday) {
        this.surveystakentoday = surveystakentoday;
    }

    public String getFacebookSessionKey() {
        return facebookSessionKey;
    }

    public void setFacebookSessionKey(String facebookSessionKey) {
        this.facebookSessionKey = facebookSessionKey;
    }


    public boolean getIsfacebookui() {
        //Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("getIsfacebookui()="+isfacebookui);
        return isfacebookui;
    }

    public void setIsfacebookui(boolean isfacebookui) {
        this.isfacebookui = isfacebookui;
    }

    public FacebookUser getFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser = facebookUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (this.message==null || this.message.equals("")){
            this.message = message;
        } else {
            this.message = this.message + "<br/>" + message;
        }
        if (message==null || message.equals("")){
            this.message = "";
        }
    }

    public ArrayList<FacebookUser> getFacebookFriends() {
        return facebookFriends;
    }

    public void setFacebookFriends(ArrayList<FacebookUser> facebookFriends) {
        this.facebookFriends=facebookFriends;
    }
}
