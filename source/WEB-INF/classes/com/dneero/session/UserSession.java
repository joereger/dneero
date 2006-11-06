package com.dneero.session;

import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 12:51:06 PM
 */
public class UserSession {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private com.dneero.dao.User user;
    private boolean isloggedin = false;
    private int currentSurveyid;
    private boolean isAllowedToResetPasswordBecauseHasValidatedByEmail = false;
    private int referredbyOnlyUsedForSignup = 0;
    private boolean isSysadmin = false;
    private boolean isLoggedInToBeta = false;

    public UserSession(){
        //Used for anonymous access
        logger.debug("New UserSession created.");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        isSysadmin = false;
        for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
            Userrole userrole = iterator.next();
            if (userrole.getRoleid()==Roles.SYSTEMADMIN){
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
}
