package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Util;
import com.dneero.util.Num;
import com.dneero.session.UserSession;
import com.dneero.dao.User;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Sep 1, 2006
 * Time: 12:46:48 PM
 */
public class InviteLandingPage implements Serializable {

    private String referredby;

    public InviteLandingPage(){
        //@todo Refactor Non-Empty Constructor
        load();
    }
    
    public String beginView(){
        load();
        return "invitelandingpage";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciated");
        logger.debug("Jsf.getRequestParam(\"referredby\")="+Jsf.getRequestParam("referredby"));
        if (Num.isinteger(Jsf.getRequestParam("referredby"))){
            User user = User.get(Integer.parseInt(Jsf.getRequestParam("referredby")));
            if (user!=null && user.getUserid()>0){
                Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(Jsf.getRequestParam("referredby")));
                referredby = user.getFirstname() + " " + user.getLastname();
            } else {
                Jsf.getUserSession().setReferredbyOnlyUsedForSignup(0);
            }

        }
    }

    public String getReferredby() {
        return referredby;
    }

    public void setReferredby(String referredby) {
        this.referredby = referredby;
    }


}
