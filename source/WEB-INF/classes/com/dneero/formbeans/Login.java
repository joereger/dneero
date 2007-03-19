package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.session.PersistentLogin;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.eula.EulaHelper;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpSession;
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciating a Login object.");
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
            UserSession userSession = new UserSession();
            userSession.setUser(user);
            userSession.setIsloggedin(true);
            userSession.setIsLoggedInToBeta(Jsf.getUserSession().getIsLoggedInToBeta());

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
                    responsepending.setReferredbyblogid(Jsf.getUserSession().getPendingSurveyReferredbyblogid());
                    responsepending.setResponseasstring(Jsf.getUserSession().getPendingSurveyResponseAsString());
                    responsepending.setSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
                    try{responsepending.save();}catch (Exception ex){logger.error(ex);}
                    Jsf.getUserSession().setPendingSurveyResponseSurveyid(0);
                    Jsf.getUserSession().setPendingSurveyReferredbyblogid(0);
                    Jsf.getUserSession().setPendingSurveyResponseAsString("");
                }
            }

            //Notify via XMPP
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
            xmpp.send();

            Jsf.bindObjectToExpressionLanguage("#{userSession}", userSession);

            //Redir if https is on
            if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
                try{
                    logger.debug("redirecting to https - "+BaseUrl.get(true)+"account/index.jsf");
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
