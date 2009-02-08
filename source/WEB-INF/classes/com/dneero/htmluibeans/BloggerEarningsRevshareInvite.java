package com.dneero.htmluibeans;


import com.dneero.util.Str;
import com.dneero.dao.User;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
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

    private String email = "";
    private String message = "";

    public BloggerEarningsRevshareInvite(){}

    public void initBean(){


    }

    public void invite() throws ValidationException {
        ValidationException vex = new ValidationException();
        User user = Pagez.getUserSession().getUser();
        StringBuffer err = new StringBuffer();

        if (email!=null && !email.equals("")){
            String[] individualemails = email.split("\\n");
            for (int i = 0; i < individualemails.length; i++) {
                String individualemail = individualemails[i];
                individualemail = individualemail.trim();

                List existingusers = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(individualemail)+"'").list();
                if (existingusers.size()<=0){
                    Pl pl = Pl.get(user.getPlid());
                    String[] args = new String[10];
                    args[0]= message;
                    EmailTemplateProcessor.sendMail(pl.getNameforui()+" Invitation from "+user.getFirstname()+" "+user.getLastname()+" - Make Money with your Blog!", "inviteblogger", user, args, individualemail, user.getEmail());
                } else {
                    //err.append("A user with the email address '"+individualemail+"' already exists. ");
                }
                
            }
        }

        if (err.length()<=0){
            email = "";
        } else {
            vex.addValidationError(err.toString());
            throw vex;
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
