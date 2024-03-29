package com.dneero.email;

import com.dneero.dao.User;
import com.dneero.dao.Pl;
import com.dneero.util.RandomString;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 12, 2006
 * Time: 2:25:52 PM
 */
public class LostPasswordSend {

    public static void sendLostPasswordEmail(User user){
        Logger logger = Logger.getLogger(LostPasswordSend.class);
        Pl pl = Pl.get(user.getPlid());
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());

        try{
            user.save();
        } catch (GeneralException gex){
            logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
        }

        EmailTemplateProcessor.sendMail(pl.getNameforui()+" Password Recovery", "lostpassword", user);

    }

}
