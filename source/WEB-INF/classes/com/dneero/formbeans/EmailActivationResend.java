package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.RandomString;
import com.dneero.util.GeneralException;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailActivationSend;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class EmailActivationResend implements Serializable {

    //Form props
    private String email;
    private String j_captcha_response;

    public EmailActivationResend(){
        if (Jsf.getUserSession().getUser()!=null){
            email = Jsf.getUserSession().getUser().getEmail();
        }
    }

    public String reSendEmail(){

        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Jsf.getHttpServletRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
        }
        if (!isCaptchaCorrect){
            Jsf.setFacesMessage("resendform:j_captcha_response", "You failed to correctly type the letters into the box.");
            return null;
        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+email+"'").list();
        if (users.size()<=0){
            Jsf.setFacesMessage("resendform:email", "That email address was not found.");
            return null;
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            EmailActivationSend.sendActivationEmail(user);
        }

        return "emailactivationresendcomplete";
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }
}
