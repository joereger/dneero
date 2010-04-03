package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class EmailActivationResend implements Serializable {

    //Form props
    private String email;


    public EmailActivationResend(){

    }


    public void initBean(){
        if (Pagez.getUserSession().getUser()!=null){
            email = Pagez.getUserSession().getUser().getEmail();
        }
    }

    public void reSendEmail() throws ValidationException {
        ValidationException vex = new ValidationException();


        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (email==null || email.equals("") || users.size()<=0){
            vex.addValidationError("That email address was not found.");
            throw vex;
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            EmailActivationSend.sendActivationEmail(user);
        }
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    
}
