package com.dneero.anonymous;

import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.helpers.CreateEmptyBloggerProfile;
import com.dneero.helpers.NicknameHelper;
import com.dneero.htmlui.Pagez;
import com.dneero.money.PaymentMethod;
import com.dneero.sir.SocialInfluenceRating;
import com.dneero.util.GeneralException;
import com.dneero.util.RandomString;
import com.dneero.xmpp.SendXMPPMessage;
import org.apache.log4j.Logger;

import java.util.Date;


public class AnonHelper {

    
    public static User createAnonymousUser(){
        Logger logger = Logger.getLogger(AnonHelper.class);
        User user = new User();
        user.setEmail("");
        user.setPlid(Pagez.getUserSession().getPl().getPlid());
        user.setPassword("");
        String nick = NicknameHelper.generateUniqueAnonymousNickname("Anonymous", user);
        user.setName(nick);
        user.setNickname(nick);
        user.setIsactivatedbyemail(true);  //Auto-activated by email... done because user will have to enter email in account settings
        user.setIsqualifiedforrevshare(true);
        user.setReferredbyuserid(0);
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
        user.setFacebookuserid(0);
        user.setFacebookappremoveddate(new Date());
        user.setIsfacebookappremoved(false);
        user.setResellercode(RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7));
        user.setResellerpercent(0.0);
        user.setCurrentbalance(0.0);
        user.setCurrentbalanceblogger(0.0);
        user.setCurrentbalanceresearcher(0.0);
        user.setLastlogindate(new java.util.Date());
        user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
        user.setSirdate(new Date());
        user.setSirdebug("");
        user.setSirpoints(0.0);
        user.setSirrank(0);
        user.setIsanonymous(true);
        try{
            user.save();
        } catch (GeneralException gex){
            logger.debug("Facebook auto-register failed: " + gex.getErrorsAsSingleString());
        }

        //Now create an accompanying blogger
        Blogger blogger = CreateEmptyBloggerProfile.create(user);

        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New Anonymous User");
        xmpp.send();

        //Setup the userSession to be logged in now
        Pagez.getUserSession().setUser(user);
        Pagez.getUserSession().setIsloggedin(true);
        Pagez.getUserSession().setIsLoggedInToBeta(true);
        Pagez.getUserSession().setIseulaok(true);
        Pagez.setUserSessionAndUpdateCache(Pagez.getUserSession());

        return user;
    }




}
