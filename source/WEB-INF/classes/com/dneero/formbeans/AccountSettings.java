package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.session.UserSession;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountSettings {

    //Form props
    private String email;
    private String firstname;
    private String lastname;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public AccountSettings(){
        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            email = user.getEmail();
            firstname = user.getFirstname();
            lastname = user.getLastname();
        }
    }

    public String saveAction(){

        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            user.setEmail(email);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            try{
                user.save();
                userid = user.getUserid();
            } catch (GeneralException gex){
                logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }
        }



        return "success";
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


}
