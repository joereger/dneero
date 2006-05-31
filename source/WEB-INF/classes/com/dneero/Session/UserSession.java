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

    private com.dneero.dao.User user;
    public static final String SESSIONLOOKUPKEY = "dneerosessionkey";

    public UserSession(){
        //Used for anonymous access
        logger.debug("New UserSession created.");
    }

    public void login(String email, String password){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
