package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailTemplateProcessor;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import java.util.List;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Sep 1, 2006
 * Time: 11:42:10 AM
 */
public class BloggerEarningsRevshareInvite implements Serializable {

    private String email;
    private String message;

    public BloggerEarningsRevshareInvite(){}

    public String invite(){
        User user = Jsf.getUserSession().getUser();
        List existingusers = HibernateUtil.getSession().createQuery("from User where email='"+email+"'").list();
        if (existingusers.size()<=0){
            EmailTemplateProcessor.sendMail("dNeero Invitation from "+user.getFirstname()+" "+user.getLastname()+" - Make Money with your Blog!", "inviteblogger", user, null, this.email, user.getEmail());
            return "bloggerearningsrevshare";
        } else {
            Jsf.setFacesMessage("inviteform:email", "A user with that email already exists.");
            return null;
        }
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
