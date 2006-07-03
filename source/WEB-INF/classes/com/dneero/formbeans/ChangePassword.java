package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.session.UserSession;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ChangePassword {

    //Form props
    private String password;
    private String passwordverify;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public ChangePassword(){

    }

    public String saveAction(){

        if (!password.equals(passwordverify)){
            Jsf.setFacesMessage("Password and Verify Password must match.");
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



        return "success";
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
