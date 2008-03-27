package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.apache.commons.validator.EmailValidator;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.*;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.session.PersistentLogin;
import com.dneero.email.EmailActivationSend;
import com.dneero.money.PaymentMethod;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.eula.EulaHelper;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.helpers.UserInputSafe;
import com.dneero.facebook.FacebookPendingReferrals;
import com.dneero.cache.providers.CacheFactory;
import com.octo.captcha.service.CaptchaServiceException;

import javax.servlet.http.Cookie;
import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class Registration implements Serializable {

    //Form props
    private String email;
    private String password;
    private String passwordverify;
    private String firstname;
    private String lastname;
    private String j_captcha_response;
    private String captchaId;
    private String eula;
    private boolean displaytempresponsesavedmessage;


    //private String temp;

    //Other props
    private int userid;

    public Registration(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        displaytempresponsesavedmessage = false;
        if (Pagez.getUserSession().getPendingSurveyResponseAsString()!=null && !Pagez.getUserSession().getPendingSurveyResponseAsString().equals("")){
            displaytempresponsesavedmessage = true;
        }
        eula = EulaHelper.getMostRecentEula().getEula();
        
    }

    public void registerAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        //Validation
        boolean haveErrors = false;

        if (password==null || password.equals("") || password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            haveErrors = true;
        }

        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)){
            vex.addValidationError("Not a valid email address.");
            haveErrors = true;
        }

        if (firstname==null || firstname.equals("")){
            vex.addValidationError("First Name can't be blank.");
            haveErrors = true;
        }

        if (lastname==null || lastname.equals("")){
            vex.addValidationError("Last Name can't be blank.");
            haveErrors = true;
        }

        //@todo need to check for lcase(firstname), lcase(lastname), email in the database... people are changing caps on name and creating another account.


        if (eula==null || !eula.trim().equals(EulaHelper.getMostRecentEula().getEula().trim())){
            //@todo Registration EULA validation
            //logger.debug("eula="+eula);
            //logger.debug("EulaHelper.getMostRecentEula().getEula()="+EulaHelper.getMostRecentEula().getEula());
            //vex.addValidationError("The end user license can't be edited.");
            //eula = EulaHelper.getMostRecentEula().getEula();
            //haveErrors = true;
        }

//        boolean isCaptchaCorrect = false;
//        try {
//            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, j_captcha_response);
//        } catch (CaptchaServiceException e) {
//             //should not happen, may be thrown if the id is not valid
//        }
//        if (!isCaptchaCorrect){
//            vex.addValidationError("You failed to correctly type the letters into the box.");
//            haveErrors = true;
//        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            vex.addValidationError("That email address is already in use.");
            haveErrors = true;
        }

        if (haveErrors){
            throw vex;
        }

        //Create the user
        //@todo Use http://www.jasypt.org/ to encrypt password
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setIsactivatedbyemail(false);
        user.setIsqualifiedforrevshare(true);
        user.setReferredbyuserid(Pagez.getUserSession().getReferredbyOnlyUsedForSignup());
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());
        user.setCreatedate(new Date());
        user.setPaymethodpaypaladdress("");
        user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
        user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
        user.setPaymethodcreditcardid(0);
        user.setChargemethodcreditcardid(0);
        user.setBloggerid(0);
        user.setResearcherid(0);
        user.setNotifyofnewsurveysbyemaileveryexdays(1);
        user.setNotifyofnewsurveyslastsent(new Date());
        user.setAllownoncriticalemails(true);
        user.setInstantnotifybyemailison(false);
        user.setInstantnotifybytwitterison(false);
        user.setInstantnotifytwitterusername("");
        user.setInstantnotifyxmppison(false);
        user.setInstantnotifyxmppusername("");
        user.setIsenabled(true);
        user.setFacebookappremoveddate(new Date());
        user.setIsfacebookappremoved(false);
        user.setResellercode(RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7));
        user.setResellerpercent(0.0);
        try{
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Apologies for the trouble.  Please try again.");
        }

        //Eula version check
        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        usereula.setEulaid(EulaHelper.getMostRecentEula().getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Pagez.getRequest().getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Apologies for the trouble.  Please try again.");
        }
        user.getUsereulas().add(usereula);

        //Pending survey save
        //Note: this code also on Login and PublicSurvey
        if (Pagez.getUserSession().getPendingSurveyResponseSurveyid()>0){
            if (!Pagez.getUserSession().getPendingSurveyResponseAsString().equals("")){
                Responsepending responsepending = new Responsepending();
                responsepending.setUserid(user.getUserid());
                responsepending.setReferredbyuserid(Pagez.getUserSession().getPendingSurveyReferredbyuserid());
                responsepending.setResponseasstring(Pagez.getUserSession().getPendingSurveyResponseAsString());
                responsepending.setSurveyid(Pagez.getUserSession().getPendingSurveyResponseSurveyid());
                try{responsepending.save();}catch (Exception ex){logger.error("",ex);}
                Pagez.getUserSession().setPendingSurveyResponseSurveyid(0);
                Pagez.getUserSession().setPendingSurveyReferredbyuserid(0);
                Pagez.getUserSession().setPendingSurveyResponseAsString("");
            }
        }

        //Send the activation email
        EmailActivationSend.sendActivationEmail(user);

        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New dNeero User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+")");
        xmpp.send();

        //Log user in
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setIsloggedin(true);
        userSession.setIsLoggedInToBeta(Pagez.getUserSession().getIsLoggedInToBeta());
        userSession.setIseulaok(true);
        userSession.setIsfacebookui(Pagez.getUserSession().getIsfacebookui());
        userSession.setFacebookSessionKey(Pagez.getUserSession().getFacebookSessionKey());
        //Set persistent login cookie
        Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
        //Add a cookies to the response
        for (int j = 0; j < cookies.length; j++) {
            Pagez.getResponse().addCookie(cookies[j]);
        }
        //Put userSession object into cache
        Pagez.setUserSessionAndUpdateCache(userSession);
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


    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId=captchaId;
    }

    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }


    public boolean getDisplaytempresponsesavedmessage() {
        return displaytempresponsesavedmessage;
    }

    public void setDisplaytempresponsesavedmessage(boolean displaytempresponsesavedmessage) {
        this.displaytempresponsesavedmessage = displaytempresponsesavedmessage;
    }
}
