package com.dneero.session;

import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 12:51:06 PM
 */
public class UserSession {
    Logger logger = Logger.getLogger(this.getClass().getName());
    public static final String SESSIONLOOKUPKEY = "dneerosessionkey";
    private com.dneero.dao.User user;
    private boolean isloggedin = false;

    public UserSession(){
        //Used for anonymous access
        logger.debug("New UserSession created.");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsloggedin() {
        return isloggedin;
    }

    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }


}
