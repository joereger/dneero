package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Calendar;

import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.util.Time;
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
        try{
            //Notify via XMPP
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "Somebody logged-in to the dNeero Beta! ("+ Time.dateformatcompactwithtime(Calendar.getInstance())+") from "+Jsf.getHttpServletRequest().getRemoteAddr()+ " "+Jsf.getHttpServletRequest().getRemoteHost());
            xmpp.send();
        } catch (Exception ex){
            logger.error(ex);
        }
        Jsf.getUserSession().setIsLoggedInToBeta(true);
        return "logintobetacomplete";

    }

    

    public String getBetapassword() {
        return betapassword;
    }

    public void setBetapassword(String betapassword) {
        this.betapassword = betapassword;
    }


}
