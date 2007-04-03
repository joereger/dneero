package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.io.Serializable;

import com.dneero.dao.User;
import com.dneero.dao.Betainvite;
import com.dneero.dao.hibernate.HibernateUtil;
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
public class LoginToBeta implements Serializable {

    private String betapassword;

    public LoginToBeta(){

    }

    public String beginView(){
        return "logintobeta";
    }

    public String login(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("login() called.");

        if (betapassword.toLowerCase().trim().equals("diaga")){
            try{
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "An unknown person logged-in to the dNeero Beta! ("+ Time.dateformatcompactwithtime(Calendar.getInstance())+") from "+Jsf.getHttpServletRequest().getRemoteAddr()+ " "+Jsf.getHttpServletRequest().getRemoteHost());
                xmpp.send();
            } catch (Exception ex){
                logger.error(ex);
            }
            Jsf.getUserSession().setIsLoggedInToBeta(true);
            return "logintobetacomplete";
        }

        List betainvites = HibernateUtil.getSession().createQuery("from Betainvite").list();
        for (Iterator iterator = betainvites.iterator(); iterator.hasNext();) {
            Betainvite betainvite = (Betainvite) iterator.next();
            if (betainvite.getPassword().trim().toLowerCase().equals(betapassword.toLowerCase().trim())){
                try{
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, betainvite.getName()+" logged-in to the dNeero Beta! ("+ Time.dateformatcompactwithtime(Calendar.getInstance())+") from "+Jsf.getHttpServletRequest().getRemoteAddr()+ " "+Jsf.getHttpServletRequest().getRemoteHost());
                    xmpp.send();
                } catch (Exception ex){
                    logger.error(ex);
                }
                betainvite.setHasloggedin(true);
                betainvite.setDatelastloggedin(new Date());
                betainvite.setNumberoftimesloggedin(betainvite.getNumberoftimesloggedin()+1);
                try{betainvite.save();}catch(Exception ex){logger.error(ex);}
                Jsf.getUserSession().setIsLoggedInToBeta(true);
                return "logintobetacomplete";
            }
        }

        Jsf.setFacesMessage("login:betapassword","Beta betapassword incorrect.");
        return null;
    }

    

    public String getBetapassword() {
        return betapassword;
    }

    public void setBetapassword(String betapassword) {
        this.betapassword = betapassword;
    }


}
