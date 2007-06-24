package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.session.UserSession;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ChangePassword implements Serializable {

    //Form props
    private String password;
    private String passwordverify;

    public ChangePassword(){

    }

    public String beginView(){
        return "changepassword";
    }

    public String saveAction(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (!password.equals(passwordverify)){
            Jsf.setFacesMessage("Password and Verify Password must match.");
            return null;
        }

        if (password.length()<6){
            Jsf.setFacesMessage("Password must be at least six characters long.");
            return null;
        }


        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            user.setPassword(password);
            try{
                user.save();
            } catch (GeneralException gex){
                logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }
        }


        AccountIndex bean = (AccountIndex)Jsf.getManagedBean("accountIndex");
        return bean.beginView();
        //return "accountindex";
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




}
