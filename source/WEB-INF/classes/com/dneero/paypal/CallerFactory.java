package com.dneero.paypal;

import com.paypal.sdk.services.CallerServices;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.profiles.APIProfile;
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
            profile.setAPIUsername("joe_api1.joereger.com");
            profile.setAPIPassword("HSUYQXF6UN9ULK9E");
            profile.setSignature("AHK9lF0bFy62J27iS5lTA66dSQIVAUXbkCx4hysQRrfGIE9etQ9lIqlj");
            profile.setEnvironment("sandbox");
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
