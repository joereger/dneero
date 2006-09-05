package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.dao.User;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailSend;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 1, 2006
 * Time: 11:42:10 AM
 */
public class BloggerEarningsRevshareInvite {

    private String email;
    private String message;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String invite(){
        User user = Jsf.getUserSession().getUser();
        String link = "http://dneero.com/invite.jsf?referredby="+user.getUserid();
        String message = "You've been invited to make money with your blog.  Fill out surveys, quickly and easily, posting them to your blog.  It's easy and you'll make money.";
        try{
            HtmlEmail email = new HtmlEmail();
            email.addTo(this.email);
            email.setFrom(user.getEmail());
            email.setSubject("dNeero Invitation from "+user.getFirstname()+" "+user.getLastname()+" - Make Money with your Blog!" );
            email.setHtmlMsg("<html><font face=arial size=+1 color=#00ff00>"+message+"</font><a href='"+link+"'>Click Here</a></html>");
            email.setTextMsg(message+" "+link);
            EmailSend.sendMail(email);
        } catch (Exception e){
            logger.error(e);
        }

        return "bloggerearningsrevshare";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
