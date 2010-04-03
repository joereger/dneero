package com.dneero.email;

import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.util.GeneralException;
import com.dneero.util.RandomString;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 12, 2006
 * Time: 2:25:52 PM
 */
public class EmailActivationSend {

    public static void sendActivationEmail(User user){
        Logger logger = Logger.getLogger(EmailActivationSend.class);
        Pl pl = Pl.get(user.getPlid());
        boolean isactivatedbyemail = false;
        if (!pl.getIsemailactivationrequired()){
            isactivatedbyemail = true;
        }
        user.setIsactivatedbyemail(isactivatedbyemail);
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());
        try{user.save();} catch (GeneralException gex){logger.error("registerAction failed: " + gex.getErrorsAsSingleString());}
        if (pl.getIsemailactivationrequired()){
            EmailTemplateProcessor.sendMail(pl.getNameforui()+" Account Activation", "accountactivation", user);
        }
    }

}
