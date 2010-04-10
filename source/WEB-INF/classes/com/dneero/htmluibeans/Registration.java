package com.dneero.htmluibeans;

import com.dneero.dao.Responsepending;
import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.eula.EulaHelper;
import com.dneero.helpers.CreateEmptyBloggerProfile;
import com.dneero.helpers.NicknameHelper;
import com.dneero.helpers.TwitanswerFinderAfterAccountInfoChange;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.ValidationException;
import com.dneero.iptrack.Activitytype;
import com.dneero.iptrack.RecordIptrackUtil;
import com.dneero.money.PaymentMethod;
import com.dneero.session.PersistentLogin;
import com.dneero.sir.SocialInfluenceRating;
import com.dneero.util.GeneralException;
import com.dneero.util.RandomString;
import com.dneero.util.Str;
import com.dneero.xmpp.SendXMPPMessage;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private String eula;
    private boolean displaytempresponsesavedmessage;
    private String nickname;



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
        eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula();
        
    }

    public void registerAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("registerAction called:  email="+email+" password="+password+" nickname="+nickname);

        //Validation
        boolean haveErrors = false;

        if (password==null || password.equals("") || password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            haveErrors = true;
        }

//        if (!password.equals(passwordverify)){
//            vex.addValidationError("Password and Verify Password must match.");
//            haveErrors = true;
//        }
        if (email==null){
            email="";
            vex.addValidationError("Not a valid email address.");
            haveErrors = true;
        }
        email=email.toLowerCase();
        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)){
            vex.addValidationError("Not a valid email address.");
            haveErrors = true;
        }



        if (nickname!=null && !nickname.equals("")){
            nickname = Str.onlyKeepLettersAndDigits(nickname);
            nickname =  nickname.toLowerCase();
            if (NicknameHelper.nicknameExistsAlready(nickname)){
                vex.addValidationError("Sorry, that nickname already exists.");
                haveErrors = true;
            }
        } else {
            nickname = "";
        }

//        if (twitterusername!=null && !twitterusername.equals("")){
//            if (TwitterUsernameAlreadyInUse.usernameExistsAlready(twitterusername)){
//                vex.addValidationError("Sorry, that Twitter Username is already in use.");
//                haveErrors = true;
//            }
//        } else {
//            twitterusername = "";
//        }


        //@todo need to check for lcase(firstname), lcase(lastname), email in the database... people are changing caps on name and creating another account.


        //if (eula==null || !eula.trim().equals(EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula().trim())){
            //@todo Registration EULA validation
            //logger.debug("eula="+eula);
            //logger.debug("EulaHelper.getMostRecentEula().getEula()="+EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula());
            //vex.addValidationError("The end user license can't be edited.");
            //eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula();
            //haveErrors = true;
        //}



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
        user.setPlid(Pagez.getUserSession().getPl().getPlid());
        user.setEmail(email);
        user.setPassword(password);
        user.setName("");
        user.setIsactivatedbyemail(!Pagez.getUserSession().getPl().getIsemailactivationrequired());
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
        user.setInstantnotifyxmppison(false);
        user.setInstantnotifyxmppusername("");
        user.setIsenabled(true);
        user.setFacebookappremoveddate(new Date());
        user.setIsfacebookappremoved(false);
        user.setResellercode(RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7));
        user.setResellerpercent(0.0);
        user.setCurrentbalance(0.0);
        user.setCurrentbalanceblogger(0.0);
        user.setCurrentbalanceresearcher(0.0);
        user.setLastlogindate(new java.util.Date());
        user.setNickname(NicknameHelper.generateUniqueNickname(nickname, user));
        user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
        user.setSirdate(new Date());
        user.setSirdebug("");
        user.setSirpoints(0.0);
        user.setSirrank(0);
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
        usereula.setEulaid(EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Pagez.getRequest().getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Apologies for the trouble.  Please try again.");
        }
        user.getUsereulas().add(usereula);

        //If PL doesn't require the blogger demographic info, create an empty one
        if (!Pagez.getUserSession().getPl().getIsbloggerdemographicrequired()){
            CreateEmptyBloggerProfile.create(user);    
        }

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
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New User: "+ user.getNickname() +  " ("+user.getEmail()+")");
        xmpp.send();

        //Log user in
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setIsloggedin(true);
        userSession.setIsLoggedInToBeta(Pagez.getUserSession().getIsLoggedInToBeta());
        userSession.setIseulaok(true);
        userSession.setIsfacebookui(Pagez.getUserSession().getIsfacebookui());
        userSession.setFacebookSessionKey(Pagez.getUserSession().getFacebookSessionKey());
        userSession.setWhereToRedirectToAfterSignup(Pagez.getUserSession().getWhereToRedirectToAfterSignup());
        userSession.setTwitaskQuestionFromHomepage(Pagez.getUserSession().getTwitaskQuestionFromHomepage());
        userSession.setSurveyTitleFromHomepage(Pagez.getUserSession().getSurveyTitleFromHomepage());
        userSession.setSurveyDescriptionFromHomepage(Pagez.getUserSession().getSurveyDescriptionFromHomepage());
        //Set persistent login cookie
        Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
        //Add a cookies to the response
        for (int j = 0; j < cookies.length; j++) {
            Pagez.getResponse().addCookie(cookies[j]);
        }
        //Put userSession object into cache
        Pagez.setUserSessionAndUpdateCache(userSession);
        //Find any Twitanswers
        TwitanswerFinderAfterAccountInfoChange.findAndChangeUseridOfTwitanswers(user);
        //Record Iptrack Activity
        RecordIptrackUtil.record(Pagez.getRequest(), Pagez.getUserSession().getUser().getUserid(), Activitytype.SIGNUP);
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


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

}
