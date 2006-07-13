package com.dneero.email;

import com.dneero.dao.User;
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

        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());

        try{
            user.save();
        } catch (GeneralException gex){
            logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
        }

        //@todo make link dynamic for instance base address
        String url = "http://dneero.com/lps?u="+user.getUserid()+"&k="+user.getEmailactivationkey();
        //@todo better lost password email message
        String message = url;

        try{
            HtmlEmail email = new HtmlEmail();
            email.addTo(user.getEmail());
            email.setFrom(EmailSendThread.DEFAULTFROM);
            email.setSubject("dNeero Password Recovery Message");
            email.setHtmlMsg("<html><font face=arial size=+1 color=#00ff00>"+message+"</font></html>");
            email.setTextMsg(message);
            EmailSend.sendMail(email);
        } catch (Exception e){
            logger.error(e);
        }

    }

}
