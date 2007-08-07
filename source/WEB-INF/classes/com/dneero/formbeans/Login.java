package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.User;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.session.PersistentLogin;
import com.dneero.session.SurveysTakenToday;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.eula.EulaHelper;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
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

    public String beginView(){
        return "login";
    }

    public String login(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("login() called.");
        logger.debug("keepmeloggedin="+keepmeloggedin);
        List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.email='"+email+"' AND user.password='"+password+"'").setMaxResults(1).list();
        if (users.size()==0){
            Jsf.setFacesMessage("Email/password incorrect.");
            return null;
        }
        for (Iterator it = users.iterator(); it.hasNext(); ) {
            User user = (User)it.next();
            if (user.getIsenabled()){
                //Create a new session so that I can manually move stuff over and guarantee it's clean
                UserSession userSession = new UserSession();
                userSession.setUser(user);
                userSession.setIsloggedin(true);
                userSession.setIsLoggedInToBeta(Jsf.getUserSession().getIsLoggedInToBeta());
                userSession.setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                userSession.setIsfacebookappadded(Jsf.getUserSession().getIsfacebookappadded());
                userSession.setIsfacebookui(Jsf.getUserSession().getIsfacebookui());
                userSession.setTempFacebookUserid(Jsf.getUserSession().getTempFacebookUserid());
                userSession.setFacebookSessionKey(Jsf.getUserSession().getFacebookSessionKey());

                //Check the eula
                if (!EulaHelper.isUserUsingMostRecentEula(user)){
                    userSession.setIseulaok(false);
                } else {
                    userSession.setIseulaok(true);
                }

                //Set persistent login cookie, if necessary
                if (keepmeloggedin){
                    //Get all possible cookies to set
                    Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Jsf.getHttpServletRequest());
                    //Add a cookies to the response
                    for (int j = 0; j < cookies.length; j++) {
                        Jsf.getHttpServletResponse().addCookie(cookies[j]);
                    }
                }

                //Pending survey save
                //Note: this code also on Resitration and PublicSurveyTake
                if (Jsf.getUserSession().getPendingSurveyResponseSurveyid()>0){
                    if (!Jsf.getUserSession().getPendingSurveyResponseAsString().equals("")){
                        Responsepending responsepending = new Responsepending();
                        responsepending.setUserid(user.getUserid());
                        responsepending.setReferredbyuserid(Jsf.getUserSession().getPendingSurveyReferredbyuserid());
                        responsepending.setResponseasstring(Jsf.getUserSession().getPendingSurveyResponseAsString());
                        responsepending.setSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
                        try{responsepending.save();}catch (Exception ex){logger.error(ex);}
                        Jsf.getUserSession().setPendingSurveyResponseSurveyid(0);
                        Jsf.getUserSession().setPendingSurveyReferredbyuserid(0);
                        Jsf.getUserSession().setPendingSurveyResponseAsString("");
                    }
                }

                //Facebook
                //If login is successful, app is added and we have a facebookid but it's not stored with the account
                if (Jsf.getUserSession().getIsfacebookui()){
                    if (Jsf.getUserSession().getTempFacebookUserid()>0){
                        if (Jsf.getUserSession().getTempFacebookUserid()!=user.getFacebookuserid()){
                            List<User> userswiththisfacebookid = HibernateUtil.getSession().createCriteria(User.class)
                                                   .add(Restrictions.eq("facebookuserid", Jsf.getUserSession().getTempFacebookUserid()))
                                                   .setCacheable(false)
                                                   .list();
                            //If no other account has this facebookid in use, save it
                            if (userswiththisfacebookid.size()==0){
                                user.setFacebookuserid(Jsf.getUserSession().getTempFacebookUserid());
                                try{user.save();}catch(Exception ex){logger.error(ex);}
                            } else {
                                //@todo What to do here?
                                logger.error("User logged-on but we already have that facebookid("+Jsf.getUserSession().getTempFacebookUserid()+") in the database.");
                            }
                        }
                    }
                }

                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                xmpp.send();

                //This is where the new UserSession is actually bound to Jsf.getUserSession()
                Jsf.bindObjectToExpressionLanguage("#{userSession}", userSession);

                //Redir if https is on
                if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
                    try{
                        logger.debug("redirecting to https - "+BaseUrl.get(true)+"account/index.jsf");
                        Jsf.redirectResponse(BaseUrl.get(true)+"account/index.jsf");
                        return null;
                    } catch (Exception ex){
                        logger.error(ex);
                        AccountIndex bean = (AccountIndex)Jsf.getManagedBean("accountIndex");
                        return bean.beginView();
                        //return "accountindex";
                    }
                } else {
                    AccountIndex bean = (AccountIndex)Jsf.getManagedBean("accountIndex");
                    return bean.beginView();
                    //return "accountindex";
                }
            } else {
                Jsf.setFacesMessage("This account is not active.  Please contact the system administrator if you feel this is an error.");
                return null;
            }
        }

        return null;
    }

    public String logout(){
        UserSession userSession = new UserSession();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ValueBinding binding = ctx.getApplication().createValueBinding("#{userSession}");
        binding.setValue(ctx, userSession);
        //Persistent Logout
        Jsf.getHttpServletResponse().addCookie(PersistentLogin.createCookieToClearPersistentLogin(Jsf.getHttpServletRequest()));
        return "logout_success";
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
