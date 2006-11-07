package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.systemprops.SystemProperty;

/**
 * User: Joe Reger Jr
 * Date: Oct 6, 2006
 * Time: 3:35:02 AM
 */
public class SysadminSystemProps {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String baseurl;
    public String sendxmpp;
    public String smtpoutboundserver;
    public String iseverythingpasswordprotected;

    public SysadminSystemProps(){
        baseurl = SystemProperty.getProp(SystemProperty.PROP_BASEURL);
        sendxmpp = SystemProperty.getProp(SystemProperty.PROP_SENDXMPP);
        smtpoutboundserver = SystemProperty.getProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER);
        iseverythingpasswordprotected = SystemProperty.getProp(SystemProperty.PROP_ISEVERYTHINGPASSWORDPROTECTED);
    }

    public String saveProps(){
        try{
            SystemProperty.setProp(SystemProperty.PROP_BASEURL, baseurl);
            SystemProperty.setProp(SystemProperty.PROP_SENDXMPP, sendxmpp);
            SystemProperty.setProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER, smtpoutboundserver);
            SystemProperty.setProp(SystemProperty.PROP_ISEVERYTHINGPASSWORDPROTECTED, iseverythingpasswordprotected);
            Jsf.setFacesMessage("Save complete.");
        } catch (Exception ex){
            logger.error(ex);
        }

        return "sysadminsystemprops";
    }


    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getSendxmpp() {
        return sendxmpp;
    }

    public void setSendxmpp(String sendxmpp) {
        this.sendxmpp = sendxmpp;
    }


    public String getSmtpoutboundserver() {
        return smtpoutboundserver;
    }

    public void setSmtpoutboundserver(String smtpoutboundserver) {
        this.smtpoutboundserver = smtpoutboundserver;
    }


    public String getIseverythingpasswordprotected() {
        return iseverythingpasswordprotected;
    }

    public void setIseverythingpasswordprotected(String iseverythingpasswordprotected) {
        this.iseverythingpasswordprotected = iseverythingpasswordprotected;
    }
}
