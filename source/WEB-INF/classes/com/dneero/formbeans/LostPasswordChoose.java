package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.LostPasswordSend;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPasswordChoose {

    //Form props
    private String password;
    private String passwordverify;
    private String j_captcha_response;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public LostPasswordChoose(){

    }

    public String choosePassword(){

        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Jsf.getHttpServletRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
        }
        if (!isCaptchaCorrect){
            Jsf.setFacesMessage("lostpasswordchooseform:j_captcha_response", "You failed to correctly type the letters into the box.");
            return null;
        }

        if (Jsf.getUserSession().getIsAllowedToResetPasswordBecauseHasValidatedByEmail()){

            User user = Jsf.getUserSession().getUser();
            user.setPassword(password);

            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            Jsf.getUserSession().setIsloggedin(true);
        } else {
            Jsf.setFacesMessage("lostpasswordchooseform:email", "Sorry, it doesn't appear that you came to this page from an email link.");
            return null;
        }

        return "home";
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
    }


    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }
}
