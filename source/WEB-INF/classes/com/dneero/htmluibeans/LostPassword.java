package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPassword implements Serializable {

    //Form props
    private String email;
    private String j_captcha_response;

    public LostPassword(){
        
    }

    public String beginView(){
        return "lostpassword";
    }

    public String recoverPassword(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Jsf.getHttpServletRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
             logger.error("", e);
        }
        if (!isCaptchaCorrect){
            Jsf.setFacesMessage("lostpasswordform:j_captcha_response", "You failed to correctly type the letters into the box.");
            return null;
        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                User user = iterator.next();
                LostPasswordSend.sendLostPasswordEmail(user);
            }
        } else {
            Jsf.setFacesMessage("lostpasswordform:email", "Email address not found.");
            return null;
        }

        return "lostpasswordsent";
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
