package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.RandomString;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.session.UserSession;
import com.dneero.session.Roles;
import com.dneero.email.EmailActivationSend;
import com.octo.captcha.service.CaptchaServiceException;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class Registration {

    //Form props
    private String email;
    private String password;
    private String passwordverify;
    private String firstname;
    private String lastname;
    private String j_captcha_response;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Registration(){

    }

    public String registerAction(){
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        //Validation
        boolean haveErrors = false;
        if (!password.equals(passwordverify)){
            Jsf.setFacesMessage("registrationForm:password", "Password and Verify Password must match.");
            haveErrors = true;
        }

        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Jsf.getHttpServletRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
        }
        if (!isCaptchaCorrect){
            Jsf.setFacesMessage("registrationForm:j_captcha_response", "You failed to correctly type the letters into the box.");
            haveErrors = true;
        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+email+"'").list();
        if (users.size()>0){
            Jsf.setFacesMessage("registrationForm:email", "That email address is already in use.");
        }

        if (haveErrors){
            return null;
        }


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setIsactivatedbyemail(false);
        user.setIsqualifiedforrevshare(false);
        user.setReferredbyuserid(Jsf.getUserSession().getReferredbyOnlyUsedForSignup());
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());
        user.setCreatedate(new Date());

        try{
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        //Send the activation email
        EmailActivationSend.sendActivationEmail(user);

        //Log the user in
        UserSession userSession = Jsf.getUserSession();
        userSession.setUser(user);
        userSession.setIsloggedin(true);

        return "success";
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }
}
