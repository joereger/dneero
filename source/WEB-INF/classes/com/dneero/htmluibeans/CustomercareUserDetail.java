package com.dneero.htmluibeans;


import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;
import com.dneero.helpers.DeleteUser;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.scheduledjobs.CurrentBalanceUpdater;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.util.*;
import com.dneero.mail.MailNotify;
import com.dneero.mail.MailtypeSimple;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class CustomercareUserDetail implements Serializable {

    private static String CORRECTPWD = "pupper";
    private int userid;
    private String name;
    private String email;
    private String paypaladdress="";
    private int referredbyuserid=0;
    private String facebookuid="";
    private boolean issysadmin = false;
    private boolean iscustomercare = false;
    private String activitypin="";
    private double amt;
    private String reason;
    private int fundstype=1;
    private boolean isenabled = true;
    private User user;
    private Researcher researcher;
    private boolean onlyshowsuccessfultransactions = false;
    private boolean onlyshownegativeamountbalance = false;
    private double resellerpercent;
    private String pwd="";
    private String messagetousersubject="";
    private String messagetouser="";
    private String twitterusername = "";



    public CustomercareUserDetail(){

    }



    public void initBean(){
        User user = null;
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("userid")));
        }
        if (user!=null && user.getUserid()>0){
            this.userid = user.getUserid();
            this.user = user;
            name = user.getName();
            email = user.getEmail();
            referredbyuserid = user.getReferredbyuserid();
            paypaladdress = user.getPaymethodpaypaladdress();
            isenabled = user.getIsenabled();
            issysadmin = false;
            facebookuid = String.valueOf(user.getFacebookuserid());
            twitterusername = user.getInstantnotifytwitterusername();
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    issysadmin = true;
                }
                if (userrole.getRoleid()== Userrole.CUSTOMERCARE){
                    iscustomercare = true;
                }
            }
            if (user.getResearcherid()>0){
                researcher = Researcher.get(user.getResearcherid());
            }





        }
    }

    public void save() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called");
        logger.debug("userid="+userid);
        logger.debug("name="+name);
        logger.debug("email="+email);
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            user.setName(name);
            user.setEmail(email);
            user.setReferredbyuserid(referredbyuserid);
            user.setPaymethodpaypaladdress(paypaladdress);
            user.setInstantnotifytwitterusername(twitterusername);
            if (Num.isinteger(facebookuid)){
                user.setFacebookuserid(Integer.parseInt(facebookuid));
            }
            try{user.save();}catch (Exception ex){logger.error("",ex);}
        }

    }

    public String sendusermessage() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        Mail mail = new Mail();
        mail.setIsflaggedforcustomercare(false);
        mail.setSubject(messagetousersubject);
        mail.setDate(new Date());
        mail.setUserid(user.getUserid());
        mail.setIsread(false);
        try{
            mail.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        Mailchild mailchild = new Mailchild();
        mailchild.setMailid(mail.getMailid());
        mailchild.setDate(new Date());
        mailchild.setIsfromcustomercare(true);
        mailchild.setMailtypeid(MailtypeSimple.TYPEID);
        mailchild.setVar1(messagetouser);
        mailchild.setVar2("");
        mailchild.setVar3("");
        mailchild.setVar4("");
        mailchild.setVar5("");
        try{
            mailchild.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }
        //Send notification
        MailNotify.notify(mail);
        //Pagez.getUserSession().setMessage("Message sent to user.");
        return "sysadminuserdetail";
    }


    public String sendresetpasswordemail() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            LostPasswordSend.sendLostPasswordEmail(user);
        }
        Pagez.getUserSession().setMessage("Password reset email sent");
        return "sysadminuserdetail";
    }

    public String reactivatebyemail() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            EmailActivationSend.sendActivationEmail(user);
        }
        Pagez.getUserSession().setMessage("Reactivation email sent");
        return "sysadminuserdetail";
    }

    public String toggleisenabled() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user.getIsenabled()){
            //Disable
            user.setIsenabled(false);
            isenabled = false;
            Pagez.getUserSession().setMessage("Account disabled.");
        } else {
            // Enable
            user.setIsenabled(true);
            isenabled = true;
            Pagez.getUserSession().setMessage("Account enabled.");
        }
        try{user.save();}catch (Exception ex){logger.error("",ex);}
        return "sysadminuserdetail";
    }

    public String togglesysadminprivs() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("togglesysadminprivs()");
        if (pwd!=null && pwd.equals(CORRECTPWD)){
            if (activitypin.equals("yes, i want to do this")){
                activitypin = "";
                User user = User.get(userid);
                if (user!=null && user.getUserid()>0){
                    issysadmin = false;
                    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                        Userrole userrole = iterator.next();
                        if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                            issysadmin = true;
                        }
                    }
                    if (issysadmin){
                        logger.debug("is a sysadmin");
                        //@todo revoke sysadmin privs doesn't work
                        //int userroleidtodelete=0;
                        for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                            Userrole userrole = iterator.next();
                            logger.debug("found roleid="+userrole.getRoleid());
                            if (userrole.getRoleid()==Userrole.SYSTEMADMIN){
                                logger.debug("removing it from iterator");
                                iterator.remove();
                            }
                        }
                        try{user.save();} catch (Exception ex){logger.error("",ex);}
                        issysadmin = false;
                        Pagez.getUserSession().setMessage("User is no longer a sysadmin");
                    } else {
                        Userrole role = new Userrole();
                        role.setUserid(user.getUserid());
                        role.setRoleid(Userrole.SYSTEMADMIN);
                        user.getUserroles().add(role);
                        try{role.save();} catch (Exception ex){logger.error("",ex);}
                        issysadmin = true;
                        Pagez.getUserSession().setMessage("User is now a sysadmin");
                    }
                    initBean();
                }
            } else {
                Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
            }
        } else {
            Pagez.getUserSession().setMessage("Fail. Password not valid.");
        }
        return "sysadminuserdetail";
    }

    public String togglecustomercareprivs() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("togglecustomercareprivs()");
        if (pwd!=null && pwd.equals(CORRECTPWD)){
            if (activitypin.equals("yes, i want to do this")){
                activitypin = "";
                User user = User.get(userid);
                if (user!=null && user.getUserid()>0){
                    iscustomercare = false;
                    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                        Userrole userrole = iterator.next();
                        if (userrole.getRoleid()== Userrole.CUSTOMERCARE){
                            iscustomercare = true;
                        }
                    }
                    if (iscustomercare){
                        logger.debug("is a sysadmin");
                        //@todo revoke customercare privs doesn't work
                        //int userroleidtodelete=0;
                        for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                            Userrole userrole = iterator.next();
                            logger.debug("found roleid="+userrole.getRoleid());
                            if (userrole.getRoleid()==Userrole.CUSTOMERCARE){
                                logger.debug("removing it from iterator");
                                iterator.remove();
                            }
                        }
                        try{user.save();} catch (Exception ex){logger.error("",ex);}
                        iscustomercare = false;
                        Pagez.getUserSession().setMessage("User is no longer a customer care rep");
                    } else {
                        Userrole role = new Userrole();
                        role.setUserid(user.getUserid());
                        role.setRoleid(Userrole.CUSTOMERCARE);
                        user.getUserroles().add(role);
                        try{role.save();} catch (Exception ex){logger.error("",ex);}
                        iscustomercare = true;
                        Pagez.getUserSession().setMessage("User is now a customer care rep");
                    }
                    initBean();
                }
            } else {
                Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
            }
        } else {
            Pagez.getUserSession().setMessage("Fail. Password not valid.");
        }
        return "sysadminuserdetail";
    }

    public void deleteuser() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("deleteuser()");
        if (pwd!=null && pwd.equals(CORRECTPWD)){
            if (activitypin.equals("yes, i want to do this")){
                activitypin = "";
                User user = User.get(userid);
                DeleteUser.delete(user);
            } else {
                Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
            }
        } else {
            Pagez.getUserSession().setMessage("Fail. Password not valid.");
        }
    }

    public String runResearcherRemainingBalanceOperations() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            if (user.getResearcherid()>0){
                try{ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
                task.setResearcherid(user.getResearcherid());
                task.execute(null);} catch (Exception ex){logger.error("",ex);}
                Pagez.getUserSession().setMessage("Task run.");
            }
        }
        return "sysadminuserdetail";
    }

    public String runCurrentBalanceUpdater() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            CurrentBalanceUpdater.processUser(user);
        }
        Pagez.getUserSession().setMessage("User's current balances updated.");
        return "sysadminuserdetail";
    }

    public String giveusermoney() throws ValidationException {
        if (pwd!=null && pwd.equals(CORRECTPWD)){
            User user = User.get(userid);
            if (user!=null && user.getUserid()>0){
                //Specify funds type
                boolean isbloggermoney = false;
                boolean isresearchermoney = false;
                boolean isreferralmoney = false;
                boolean isresellermoney = false;
                if (fundstype==1){
                    isbloggermoney = true;
                    isresearchermoney = false;
                    isreferralmoney = false;
                    isresellermoney = false;
                } else if (fundstype==2){
                    isbloggermoney = false;
                    isresearchermoney = true;
                    isreferralmoney = false;
                    isresellermoney = false;
                } else if (fundstype==3){
                    isbloggermoney = false;
                    isresearchermoney = false;
                    isreferralmoney = true;
                    isresellermoney = false;
                } else if (fundstype==4){
                    isbloggermoney = false;
                    isresearchermoney = false;
                    isreferralmoney = false;
                    isresellermoney = true;
                }
                //Move it
                MoveMoneyInAccountBalance.pay(user, amt, "Manual transaction: "+reason, false, false, "", isresearchermoney, isbloggermoney, isreferralmoney, isresellermoney);
            }
            initBean();
            Pagez.getUserSession().setMessage("$" + Str.formatForMoney(amt) + " given to user account balance.");
        } else {
            Pagez.getUserSession().setMessage("Fail. Password not valid.");
        }
        return "sysadminuserdetail";
    }

    public String takeusermoney() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            //Specify funds type
            boolean isbloggermoney = false;
            boolean isresearchermoney = false;
            boolean isreferralmoney = false;
            boolean isresellermoney = false;
            if (fundstype==1){
                isbloggermoney = true;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==2){
                isbloggermoney = false;
                isresearchermoney = true;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==3){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = true;
                isresellermoney = false;
            } else if (fundstype==4){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = true;
            }
            //Move it
            MoveMoneyInAccountBalance.charge(user, amt, "Manual transaction: "+reason, isresearchermoney, isbloggermoney, isreferralmoney, isresellermoney);
        }
        initBean();
        Pagez.getUserSession().setMessage("$"+ Str.formatForMoney(amt)+" taken from user account balance");
        return "sysadminuserdetail";
    }

    public String updateresellerpercent() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            user.setResellerpercent(resellerpercent);
            try{user.save();} catch (Exception ex){logger.error("",ex);}
        }
        initBean();
        Pagez.getUserSession().setMessage("Reseller Percent updated.");
        return "sysadminuserdetail";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }





    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher=researcher;
    }

    public String getPaypaladdress() {
        return paypaladdress;
    }

    public void setPaypaladdress(String paypaladdress) {
        this.paypaladdress=paypaladdress;
    }

    public int getReferredbyuserid() {
        return referredbyuserid;
    }

    public void setReferredbyuserid(int referredbyuserid) {
        this.referredbyuserid=referredbyuserid;
    }

    public boolean getOnlyshowsuccessfultransactions() {
        return onlyshowsuccessfultransactions;
    }

    public void setOnlyshowsuccessfultransactions(boolean onlyshowsuccessfultransactions) {
        this.onlyshowsuccessfultransactions=onlyshowsuccessfultransactions;
    }

    public boolean getOnlyshownegativeamountbalance() {
        return onlyshownegativeamountbalance;
    }

    public void setOnlyshownegativeamountbalance(boolean onlyshownegativeamountbalance) {
        this.onlyshownegativeamountbalance=onlyshownegativeamountbalance;
    }

    public String getFacebookuid() {
        return facebookuid;
    }

    public void setFacebookuid(String facebookuid) {
        this.facebookuid=facebookuid;
    }

    public double getResellerpercent() {
        return resellerpercent;
    }

    public void setResellerpercent(double resellerpercent) {
        this.resellerpercent = resellerpercent;
    }

    public int getFundstype() {
        return fundstype;
    }

    public void setFundstype(int fundstype) {
        this.fundstype = fundstype;
    }

    public boolean getIscustomercare() {
        return iscustomercare;
    }

    public void setIscustomercare(boolean iscustomercare) {
        this.iscustomercare=iscustomercare;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd=pwd;
    }

    public String getMessagetousersubject() {
        return messagetousersubject;
    }

    public void setMessagetousersubject(String messagetousersubject) {
        this.messagetousersubject=messagetousersubject;
    }

    public String getMessagetouser() {
        return messagetouser;
    }

    public void setMessagetouser(String messagetouser) {
        this.messagetouser=messagetouser;
    }

    public String getTwitterusername() {
        return twitterusername;
    }

    public void setTwitterusername(String twitterusername) {
        this.twitterusername=twitterusername;
    }
}
