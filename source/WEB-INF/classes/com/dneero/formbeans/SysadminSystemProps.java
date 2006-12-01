package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;

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
    public String paypalapiusername;
    public String paypalapipassword;
    public String paypalsignature;
    public String paypalenvironment;
    public String issslon;

    public SysadminSystemProps(){
        baseurl = SystemProperty.getProp(SystemProperty.PROP_BASEURL);
        sendxmpp = SystemProperty.getProp(SystemProperty.PROP_SENDXMPP);
        smtpoutboundserver = SystemProperty.getProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER);
        iseverythingpasswordprotected = SystemProperty.getProp(SystemProperty.PROP_ISEVERYTHINGPASSWORDPROTECTED);
        paypalapiusername = SystemProperty.getProp(SystemProperty.PROP_PAYPALAPIUSERNAME);
        paypalapipassword = SystemProperty.getProp(SystemProperty.PROP_PAYPALAPIPASSWORD);
        paypalsignature = SystemProperty.getProp(SystemProperty.PROP_PAYPALSIGNATURE);
        paypalenvironment = SystemProperty.getProp(SystemProperty.PROP_PAYPALENVIRONMENT);
        issslon = SystemProperty.getProp(SystemProperty.PROP_ISSSLON);
    }

    public String saveProps(){
        try{
            SystemProperty.setProp(SystemProperty.PROP_BASEURL, baseurl);
            SystemProperty.setProp(SystemProperty.PROP_SENDXMPP, sendxmpp);
            SystemProperty.setProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER, smtpoutboundserver);
            SystemProperty.setProp(SystemProperty.PROP_ISEVERYTHINGPASSWORDPROTECTED, iseverythingpasswordprotected);
            SystemProperty.setProp(SystemProperty.PROP_PAYPALAPIUSERNAME, paypalapiusername);
            SystemProperty.setProp(SystemProperty.PROP_PAYPALAPIPASSWORD, paypalapipassword);
            SystemProperty.setProp(SystemProperty.PROP_PAYPALSIGNATURE, paypalsignature);
            SystemProperty.setProp(SystemProperty.PROP_PAYPALENVIRONMENT, paypalenvironment);
            SystemProperty.setProp(SystemProperty.PROP_ISSSLON, issslon);
            BaseUrl.refresh();
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


    public String getPaypalapiusername() {
        return paypalapiusername;
    }

    public void setPaypalapiusername(String paypalapiusername) {
        this.paypalapiusername = paypalapiusername;
    }

    public String getPaypalapipassword() {
        return paypalapipassword;
    }

    public void setPaypalapipassword(String paypalapipassword) {
        this.paypalapipassword = paypalapipassword;
    }

    public String getPaypalsignature() {
        return paypalsignature;
    }

    public void setPaypalsignature(String paypalsignature) {
        this.paypalsignature = paypalsignature;
    }

    public String getPaypalenvironment() {
        return paypalenvironment;
    }

    public void setPaypalenvironment(String paypalenvironment) {
        this.paypalenvironment = paypalenvironment;
    }

    public String getIssslon() {
        return issslon;
    }

    public void setIssslon(String issslon) {
        this.issslon = issslon;
    }
}
