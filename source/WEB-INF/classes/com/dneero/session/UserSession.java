package com.dneero.session;

import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.HashMap;
import java.io.Serializable;

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
    private int pendingSurveyReferredbyblogid = 0;
    private boolean iseulaok = true;

    public UserSession(){

    }

    public User getUser() {
        if (userid>0){
            return User.get(userid);
        } else {
            return null;
        }
        //return user;
    }

    public void setUser(User user) {
        //this.user = user;
        userid = user.getUserid();
        isSysadmin = false;
        for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
            Userrole userrole = iterator.next();
            if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                isSysadmin = true;
            }
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


    public int getPendingSurveyReferredbyblogid() {
        return pendingSurveyReferredbyblogid;
    }

    public void setPendingSurveyReferredbyblogid(int pendingSurveyReferredbyblogid) {
        this.pendingSurveyReferredbyblogid = pendingSurveyReferredbyblogid;
    }


    public boolean getIseulaok() {
        return iseulaok;
    }

    public void setIseulaok(boolean iseulaok) {
        this.iseulaok = iseulaok;
    }
}
