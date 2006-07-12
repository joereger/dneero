package com.dneero.email;

import com.dneero.dao.User;
import com.dneero.util.RandomString;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 12, 2006
 * Time: 2:25:52 PM
 */
public class EmailActivationSend {

    public static void sendActivationEmail(User user){
        Logger logger = Logger.getLogger(EmailActivationSend.class);

        user.setIsactivatedbyemail(false);
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());

        try{
            user.save();
        } catch (GeneralException gex){
            logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
        }
        
        //@todo make link dynamic for instance base address
        String url = "http://dneero.com/eas?u="+user.getUserid()+"&k="+user.getEmailactivationkey();
        //@todo better email activation email message
        String message = url;
        EmailSend.sendMail("info@dneero.com", user.getEmail(), "dNeero Account Activation", message);

    }

}
