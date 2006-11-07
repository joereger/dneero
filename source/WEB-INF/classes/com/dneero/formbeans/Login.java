package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.xmpp.SendXMPPMessage;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpSession;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class Login {

    private String email;
    private String password;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Login(){
        logger.debug("Instanciating a Login object.");
    }

    public String login(){
        logger.debug("login() called.");

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
            //@todo Remove beta password set on login before going live... duh
            userSession.setIsLoggedInToBeta(Jsf.getUserSession().getIsLoggedInToBeta());

            //Notify customer care group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
            xmpp.send();

            Jsf.bindObjectToExpressionLanguage("#{userSession}", userSession);

//            FacesContext ctx = FacesContext.getCurrentInstance();
//            ValueBinding binding = ctx.getApplication().createValueBinding("#{userSession}");
//            binding.setValue(ctx, userSession);



            return "success";
        }

        return "failure";
    }

    public String logout(){
        UserSession userSession = new UserSession();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ValueBinding binding = ctx.getApplication().createValueBinding("#{userSession}");
        binding.setValue(ctx, userSession);
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


}
