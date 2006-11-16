package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminUserDetail  {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private boolean issysadmin = false;
    private String activitypin;


    public SysadminUserDetail(){


    }



    public String beginView(){
        //logger.debug("beginView called:");
        String tmpUserid = Jsf.getRequestParam("userid");
        if (com.dneero.util.Num.isinteger(tmpUserid)){
            logger.debug("beginView called: found userid in request param="+tmpUserid);
            load(Integer.parseInt(tmpUserid));
        }
        return "sysadminuserdetail";
    }

    private void load(int userid){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            this.userid = user.getUserid();
            firstname = user.getFirstname();
            lastname = user.getLastname();
            email = user.getEmail();
            issysadmin = false;
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    issysadmin = true;
                }
            }
        }
    }

    public String save(){
        logger.debug("save() called");
        logger.debug("userid="+userid);
        logger.debug("firstname="+firstname);
        logger.debug("lastname="+lastname);
        logger.debug("email="+email);
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            try{user.save();}catch (Exception ex){logger.error(ex);}
        }
        Jsf.setFacesMessage("User details saved");
        return "sysadminuserdetail";
    }

    public String sendresetpasswordemail(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            LostPasswordSend.sendLostPasswordEmail(user);
        }
        Jsf.setFacesMessage("Password reset email sent");
        return "sysadminuserdetail";
    }

    public String reactivatebyemail(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            EmailActivationSend.sendActivationEmail(user);
        }
        Jsf.setFacesMessage("Reactivation email sent");
        return "sysadminuserdetail";
    }

    public String togglesysadminprivs(){
        if (activitypin.equals("yes, i want to do this")){
            activitypin = "";
            User user = User.get(userid);
            if (user!=null && user.getUserid()>0){
                if (issysadmin){
                    //@todo revoke sysadmin privs doesn't work
                    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                        Userrole userrole = iterator.next();
                        if (userrole.getRoleid()==Userrole.SYSTEMADMIN){
                            iterator.remove();
                        }
                    }
                    try{user.save();} catch (Exception ex){logger.error(ex);}
                    Jsf.setFacesMessage("User is no longer a sysadmin");
                } else {
                    Userrole role = new Userrole();
                    role.setUserid(user.getUserid());
                    role.setRoleid(Userrole.SYSTEMADMIN);
                    user.getUserroles().add(role);
                    try{role.save();} catch (Exception ex){logger.error(ex);}
                    Jsf.setFacesMessage("User is now a sysadmin");
                }
                load(user.getUserid());
            }
        } else {
            Jsf.setFacesMessage("Activity Pin Not correct.");
        }
        return "sysadminuserdetail";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public String getActivitypin() {
        return activitypin;
    }

    public void setActivitypin(String activitypin) {
        this.activitypin = activitypin;
    }

    public boolean getIssysadmin() {
        return issysadmin;
    }

    public void setIssysadmin(boolean issysadmin) {
        this.issysadmin = issysadmin;
    }
}
