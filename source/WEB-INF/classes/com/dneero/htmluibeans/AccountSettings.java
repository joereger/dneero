package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Panel;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.email.EmailActivationSend;
import com.dneero.helpers.UserInputSafe;
import com.dneero.money.PaymentMethod;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountSettings implements Serializable {

    //Form props
    private String email;
    private String firstname;
    private String lastname;
    private int notifyofnewsurveysbyemaileveryexdays = 1;
    private boolean allownoncriticalemails = true;
    private boolean instantnotifybyemailison = false;
    private boolean instantnotifybytwitterison = false;
    private String instantnotifytwitterusername = "";
    private boolean instantnotifyxmppison = false;
    private String instantnotifyxmppusername = "";
    private String paymethodpaypaladdress;
    

    //Other props
    private int userid;

    public AccountSettings(){

    }

    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            email = user.getEmail();
            firstname = user.getFirstname();
            lastname = user.getLastname();
            notifyofnewsurveysbyemaileveryexdays = user.getNotifyofnewsurveysbyemaileveryexdays();
            allownoncriticalemails = user.getAllownoncriticalemails();
            instantnotifybyemailison = user.getInstantnotifybyemailison();
            instantnotifybytwitterison = user.getInstantnotifybytwitterison();
            instantnotifytwitterusername = user.getInstantnotifytwitterusername();
            instantnotifyxmppison = user.getInstantnotifyxmppison();
            instantnotifyxmppusername =  user.getInstantnotifyxmppusername();
            paymethodpaypaladdress = user.getPaymethodpaypaladdress();
        }
    }

    public void saveAction(){

        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            boolean haveValidationError = false;
            boolean emailNeedsToBeReactivated = false;
            User user = userSession.getUser();
            if (!user.getEmail().equals(email)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where email='"+Str.cleanForSQL(email)+"' and userid<>'"+userSession.getUser().getUserid()+"'");
                if (cnt>0){
                    Pagez.getUserSession().setMessage("The email address ("+email+") is already in use and was not added to your account.");
                    haveValidationError = true;
                    email = user.getEmail();
                } else {
                    emailNeedsToBeReactivated = true;
                    user.setEmail(email);
                }
            }
            if (!user.getPaymethodpaypaladdress().equals(paymethodpaypaladdress)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where paymethodpaypaladdress='"+Str.cleanForSQL(paymethodpaypaladdress)+"' and userid<>'"+userSession.getUser().getUserid()+"' and paymethodpaypaladdress<>'' and paymethodpaypaladdress IS NOT NULL");
                if (cnt>0){
                    Pagez.getUserSession().setMessage("That PayPal address is already in use by another user.");
                    paymethodpaypaladdress = user.getPaymethodpaypaladdress();
                    haveValidationError = true;
                }
            }
            user.setFirstname(UserInputSafe.clean(firstname));
            user.setLastname(UserInputSafe.clean(lastname));
            user.setNotifyofnewsurveysbyemaileveryexdays(notifyofnewsurveysbyemaileveryexdays);
            user.setAllownoncriticalemails(allownoncriticalemails);
            user.setInstantnotifybyemailison(instantnotifybyemailison);
            user.setInstantnotifybytwitterison(instantnotifybytwitterison);
            user.setInstantnotifytwitterusername(UserInputSafe.clean(instantnotifytwitterusername));
            user.setInstantnotifyxmppison(instantnotifyxmppison);
            user.setInstantnotifyxmppusername(UserInputSafe.clean(instantnotifyxmppusername));
            user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
            user.setPaymethodpaypaladdress(paymethodpaypaladdress);
            try{
                user.save();
                userid = user.getUserid();
                if (!haveValidationError){
                    Pagez.getUserSession().setMessage("Settings saved!");
                }
            } catch (GeneralException gex){
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return;
            }

            if (emailNeedsToBeReactivated){
                EmailActivationSend.sendActivationEmail(user);
            }
        }
    }

    public ArrayList<String> getNotificationFrequencies(){
        ArrayList<String> out  = new ArrayList<String>();
        out.add("1");
        out.add("7");
        out.add("31");
        out.add("0");
        return out;
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


    public int getNotifyofnewsurveysbyemaileveryexdays() {
        return notifyofnewsurveysbyemaileveryexdays;
    }

    public void setNotifyofnewsurveysbyemaileveryexdays(int notifyofnewsurveysbyemaileveryexdays) {
        this.notifyofnewsurveysbyemaileveryexdays = notifyofnewsurveysbyemaileveryexdays;
    }

    public boolean getAllownoncriticalemails() {
        return allownoncriticalemails;
    }

    public void setAllownoncriticalemails(boolean allownoncriticalemails) {
        this.allownoncriticalemails = allownoncriticalemails;
    }


    public boolean getInstantnotifybyemailison() {
        return instantnotifybyemailison;
    }

    public void setInstantnotifybyemailison(boolean instantnotifybyemailison) {
        this.instantnotifybyemailison = instantnotifybyemailison;
    }

    public boolean getInstantnotifybytwitterison() {
        return instantnotifybytwitterison;
    }

    public void setInstantnotifybytwitterison(boolean instantnotifybytwitterison) {
        this.instantnotifybytwitterison = instantnotifybytwitterison;
    }

    public String getInstantnotifytwitterusername() {
        return instantnotifytwitterusername;
    }

    public void setInstantnotifytwitterusername(String instantnotifytwitterusername) {
        this.instantnotifytwitterusername = instantnotifytwitterusername;
    }

    public boolean getInstantnotifyxmppison() {
        return instantnotifyxmppison;
    }

    public void setInstantnotifyxmppison(boolean instantnotifyxmppison) {
        this.instantnotifyxmppison = instantnotifyxmppison;
    }

    public String getInstantnotifyxmppusername() {
        return instantnotifyxmppusername;
    }

    public void setInstantnotifyxmppusername(String instantnotifyxmppusername) {
        this.instantnotifyxmppusername = instantnotifyxmppusername;
    }


    public String getPaymethodpaypaladdress() {
        return paymethodpaypaladdress;
    }

    public void setPaymethodpaypaladdress(String paymethodpaypaladdress) {
        this.paymethodpaypaladdress = paymethodpaypaladdress;
    }
}
