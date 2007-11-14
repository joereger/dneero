package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;

import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.LostPasswordSend;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPasswordChoose implements Serializable {

    //Form props
    private String password;
    private String passwordverify;
    private String j_captcha_response;

    public LostPasswordChoose(){

    }

    public void initBean(){

    }

    public String choosePassword() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());

        boolean haveErrors = false;

        if (password==null || password.equals("") || password.length()<6){
            Pagez.getUserSession().setMessage("Password must be at least six characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            Pagez.getUserSession().setMessage("Password and Verify Password must match.");
            haveErrors = true;
        }


        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Pagez.getRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
             logger.error("", e);
        }
        if (!isCaptchaCorrect){
            Pagez.getUserSession().setMessage("You failed to correctly type the letters into the box.");
            haveErrors = true;
        }

        
        if (haveErrors){
            return null;
        }


        if (Pagez.getUserSession().getIsAllowedToResetPasswordBecauseHasValidatedByEmail()){

            User user = Pagez.getUserSession().getUser();
            user.setPassword(password);

            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            Pagez.getUserSession().setIsloggedin(true);
        } else {
            Pagez.getUserSession().setMessage("Sorry, it doesn't appear that you came to this page from an email link.");
            return null;
        }

        Pagez.sendRedirect("/jsp/account/index.jsp");
        return "";
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
