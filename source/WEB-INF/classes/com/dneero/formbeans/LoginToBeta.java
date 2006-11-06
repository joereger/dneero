package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Iterator;

import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.xmpp.SendXMPPMessage;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class LoginToBeta {

    private String betapassword;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public LoginToBeta(){
        logger.debug("Instanciating a Login object.");
    }

    public String login(){
        logger.debug("login() called.");

        if (!betapassword.equals("diaga")){
            Jsf.setFacesMessage("login:betapassword","Beta betapassword incorrect.");
            return null;
        }
        Jsf.getUserSession().setIsLoggedInToBeta(true);
        return "home";

    }





    public String getBetapassword() {
        return betapassword;
    }

    public void setBetapassword(String betapassword) {
        this.betapassword = betapassword;
    }


}
