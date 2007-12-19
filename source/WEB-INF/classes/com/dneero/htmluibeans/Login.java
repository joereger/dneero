package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.User;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;

import com.dneero.util.Str;
import com.dneero.util.Num;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.session.PersistentLogin;
import com.dneero.session.SurveysTakenToday;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.eula.EulaHelper;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;

import javax.servlet.http.Cookie;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class Login implements Serializable {

    private String email;
    private String password;
    private boolean keepmeloggedin = true;

    public Login(){

    }

    public void initBean(){
        //Facebook
        //If app is added and we have a facebookid but it's not stored with the account
        
//        if (Pagez.getUserSession().getIsfacebookui()){
//            if (Pagez.getUserSession().getFacebookUser()!=null && Pagez.getUserSession().getFacebookUser().getUid()!=null && Pagez.getUserSession().getFacebookUser().getUid().length()>0){
//                if (Num.isinteger(Pagez.getUserSession().getFacebookUser().getUid())){
//                    List<User> userswiththisfacebookid = HibernateUtil.getSession().createCriteria(User.class)
//                                           .add(Restrictions.eq("facebookuserid", Integer.parseInt(Pagez.getUserSession().getFacebookUser().getUid())))
//                                           .setCacheable(true)
//                                           .list();
//                    //If no other account has this facebookid in use, save it
//                    if (userswiththisfacebookid.size()==0){
//
//                        user.setFacebookuserid(Integer.parseInt(Pagez.getUserSession().getFacebookUser().getUid()));
//                        try{user.save();}catch(Exception ex){logger.error("",ex);}
//                    } else {
//                        logger.error("User logged-on but we already have that facebookuid("+Pagez.getUserSession().getFacebookUser().getUid()+") in the database.");
//                        for (Iterator<User> iterator=userswiththisfacebookid.iterator(); iterator.hasNext();) {
//                            User user1=iterator.next();
//
//                        }
//                    }
//
//                }
//            }
//        }
    }


    public void login() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("login() called.");
        logger.debug("keepmeloggedin="+keepmeloggedin);
        List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.email='"+ Str.cleanForSQL(email)+"' AND user.password='"+Str.cleanForSQL(password)+"'").setMaxResults(1).list();
        if (users.size()==0){
            vex.addValidationError("Email/password incorrect.");
            throw vex;
        }
        for (Iterator it = users.iterator(); it.hasNext(); ) {
            User user = (User)it.next();
            if (user.getIsenabled()){
                //Create a new session so that I can manually move stuff over and guarantee it's clean
                UserSession userSession = new UserSession();
                userSession.setUser(user);
                userSession.setIsloggedin(true);
                userSession.setIsLoggedInToBeta(Pagez.getUserSession().getIsLoggedInToBeta());
                userSession.setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                userSession.setIsfacebookui(Pagez.getUserSession().getIsfacebookui());
                userSession.setFacebookSessionKey(Pagez.getUserSession().getFacebookSessionKey());

                //Check the eula
                if (!EulaHelper.isUserUsingMostRecentEula(user)){
                    userSession.setIseulaok(false);
                } else {
                    userSession.setIseulaok(true);
                }

                //Set persistent login cookie, if necessary
                if (keepmeloggedin){
                    //Get all possible cookies to set
                    Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
                    //Add a cookies to the response
                    for (int j = 0; j < cookies.length; j++) {
                        Pagez.getResponse().addCookie(cookies[j]);
                    }
                }

                //Pending survey save
                //Note: this code also on Resitration and PublicSurvey
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

                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                xmpp.send();

                //This is where the new UserSession is actually bound to Pagez.getUserSession()
                Pagez.setUserSessionAndUpdateCache(userSession);


            } else {
                vex.addValidationError("This account is not active.  Please contact the system administrator if you feel this is an error.");
                throw vex;
            }
        }
    }

    public void logout() throws ValidationException{
        Pagez.setUserSession(new UserSession());
        Pagez.setUserSessionAndUpdateCache(new UserSession());
        //Persistent Logout
        Pagez.getResponse().addCookie(PersistentLogin.createCookieToClearPersistentLogin(Pagez.getRequest()));
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


    public boolean getKeepmeloggedin() {
        return keepmeloggedin;
    }

    public void setKeepmeloggedin(boolean keepmeloggedin) {
        this.keepmeloggedin = keepmeloggedin;
    }
}
