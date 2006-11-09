package com.dneero.money.paypal;

import com.paypal.sdk.services.CallerServices;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.profiles.APIProfile;
import com.dneero.systemprops.SystemProperty;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 5, 2006
 * Time: 11:38:17 AM
 */
public class CallerFactory {

    CallerServices caller;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public CallerFactory(){

    }

    private void initialize(){
        caller = new CallerServices();
        try{
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
            profile.setAPIUsername(SystemProperty.getProp(SystemProperty.PROP_PAYPALAPIUSERNAME));
            profile.setAPIPassword(SystemProperty.getProp(SystemProperty.PROP_PAYPALAPIPASSWORD));
            profile.setSignature(SystemProperty.getProp(SystemProperty.PROP_PAYPALSIGNATURE));
            profile.setEnvironment(SystemProperty.getProp(SystemProperty.PROP_PAYPALENVIRONMENT));
            caller.setAPIProfile(profile);
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public CallerServices getCaller(){
        if (caller==null){
            initialize();
        }
        return caller;
    }



}
