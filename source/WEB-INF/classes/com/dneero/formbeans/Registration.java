package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.RandomString;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.session.UserSession;
import com.dneero.session.PersistentLogin;
import com.dneero.email.EmailActivationSend;
import com.dneero.money.PaymentMethod;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.eula.EulaHelper;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.octo.captcha.service.CaptchaServiceException;

import javax.servlet.http.Cookie;
import java.util.*;

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
    private String eula;
    private String invitationcode;

    //private String temp;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Registration(){
        eula = EulaHelper.getMostRecentEula().getEula();
    }

    public String registerAction(){
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        //Validation
        boolean haveErrors = false;

        if (!invitationcode.equals("diaga")){
            Jsf.setFacesMessage("registrationForm:invitationcode", "The dNeero beta is currently by invitation only.  We'll open the beta to the public very soon.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            Jsf.setFacesMessage("registrationForm:password", "Password and Verify Password must match.");
            haveErrors = true;
        }


        if (!eula.equals(EulaHelper.getMostRecentEula().getEula())){
            logger.debug("eula="+eula);
            logger.debug("EulaHelper.getMostRecentEula().getEula()="+EulaHelper.getMostRecentEula().getEula());
            Jsf.setFacesMessage("registrationForm:eula", "The end user license can't be edited.");
            eula = EulaHelper.getMostRecentEula().getEula();
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

        //Create the user
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
        user.setPaymethodpaypaladdress("");
        user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
        user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
        user.setPaymethodcreditcardid(0);
        user.setChargemethodcreditcardid(0);
        user.setBloggerid(0);
        user.setResearcherid(0);
        try{
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        //Eula version check
        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        usereula.setEulaid(EulaHelper.getMostRecentEula().getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Jsf.getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }
        user.getUsereulas().add(usereula);

        //Pending survey save
        //Note: this code also on Login and PublicSurveyTake
        if (Jsf.getUserSession().getPendingSurveyResponseSurveyid()>0){
            if (!Jsf.getUserSession().getPendingSurveyResponseAsString().equals("")){
                Responsepending responsepending = new Responsepending();
                responsepending.setUserid(user.getUserid());
                responsepending.setReferredbyblogid(Jsf.getUserSession().getPendingSurveyReferredbyblogid());
                responsepending.setResponseasstring(Jsf.getUserSession().getPendingSurveyResponseAsString());
                responsepending.setSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
                try{responsepending.save();}catch (Exception ex){logger.error(ex);}
                Jsf.getUserSession().setPendingSurveyResponseSurveyid(0);
                Jsf.getUserSession().setPendingSurveyReferredbyblogid(0);
                Jsf.getUserSession().setPendingSurveyResponseAsString("");
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
        userSession.setIsLoggedInToBeta(Jsf.getUserSession().getIsLoggedInToBeta());
        userSession.setIseulaok(true);
        //Set persistent login cookie
        Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Jsf.getHttpServletRequest());
        //Add a cookies to the response
        for (int j = 0; j < cookies.length; j++) {
            Jsf.getHttpServletResponse().addCookie(cookies[j]);
        }
        Jsf.bindObjectToExpressionLanguage("#{userSession}", userSession);

        //Redir if https is on
        if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
            try{
                logger.debug("redirecting to https - "+ BaseUrl.get(true)+"account/index.jsf");
                Jsf.redirectResponse(BaseUrl.get(true)+"account/index.jsf");
                return null;
            } catch (Exception ex){
                logger.error(ex);
                return "accountindex";
            }
        } else {
            return "accountindex";
        }
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


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }


    public String getInvitationcode() {
        return invitationcode;
    }

    public void setInvitationcode(String invitationcode) {
        this.invitationcode = invitationcode;
    }
}
