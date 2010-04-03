package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.email.EmailActivationSend;
import com.dneero.helpers.NicknameHelper;
import com.dneero.helpers.TwitanswerFinderAfterAccountInfoChange;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.PaymentMethod;
import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountSettings implements Serializable {

    //Form props
    private String email;
    private String name;
    private int notifyofnewsurveysbyemaileveryexdays = 1;
    private boolean allownoncriticalemails = true;
    private boolean instantnotifybyemailison = false;
    private boolean instantnotifyxmppison = false;
    private String instantnotifyxmppusername = "";
    private String paymethodpaypaladdress;
    private String nickname="";

    //Other props
    private int userid;

    public AccountSettings(){

    }

    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            email = user.getEmail();
            name = user.getName();
            notifyofnewsurveysbyemaileveryexdays = user.getNotifyofnewsurveysbyemaileveryexdays();
            allownoncriticalemails = user.getAllownoncriticalemails();
            instantnotifybyemailison = user.getInstantnotifybyemailison();
            instantnotifyxmppison = user.getInstantnotifyxmppison();
            instantnotifyxmppusername =  user.getInstantnotifyxmppusername();
            paymethodpaypaladdress = user.getPaymethodpaypaladdress();
            nickname = user.getNickname();
        }
    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            boolean emailNeedsToBeReactivated = false;
            User user = userSession.getUser();
            if (!user.getEmail().equals(email)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where email='"+Str.cleanForSQL(email)+"' and userid<>'"+userSession.getUser().getUserid()+"'");
                if (cnt>0){
                    vex.addValidationError("The email address ("+email+") is already in use and was not added to your account.");
                    email = user.getEmail();
                } else {
                    emailNeedsToBeReactivated = true;
                    user.setEmail(email);
                }
            }
            if (!user.getPaymethodpaypaladdress().equals(paymethodpaypaladdress)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where paymethodpaypaladdress='"+Str.cleanForSQL(paymethodpaypaladdress)+"' and userid<>'"+userSession.getUser().getUserid()+"' and paymethodpaypaladdress<>'' and paymethodpaypaladdress IS NOT NULL");
                if (cnt>0){
                    vex.addValidationError("That PayPal address is already in use by another user.");
                    paymethodpaypaladdress = user.getPaymethodpaypaladdress();
                }
            }
            if (nickname!=null && !nickname.equals("")){
                nickname = Str.onlyKeepLettersAndDigits(nickname);
                nickname = nickname.toLowerCase();
                if (!nickname.equals(user.getNickname())){
                    if (NicknameHelper.nicknameExistsAlreadyForSomebodyElse(nickname, user)){
                        vex.addValidationError("Sorry, that nickname is already in use.");
                    }
                }
            } else {
                nickname = "";
            }
            //Throw validation error if necessary
            if (vex.getErrors()!=null && vex.getErrors().length>0){
                throw vex;
            }
            //Set rest of vars
            user.setName(name);
            user.setNotifyofnewsurveysbyemaileveryexdays(notifyofnewsurveysbyemaileveryexdays);
            user.setAllownoncriticalemails(allownoncriticalemails);
            user.setInstantnotifybyemailison(instantnotifybyemailison);
            user.setInstantnotifyxmppison(instantnotifyxmppison);
            user.setInstantnotifyxmppusername(instantnotifyxmppusername);
            user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
            user.setPaymethodpaypaladdress(paymethodpaypaladdress);
            user.setNickname(NicknameHelper.generateUniqueNickname(nickname, user));
            try{
                user.save();
                userid = user.getUserid();
            } catch (GeneralException gex){
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return;
            }
            //Send react email
            if (emailNeedsToBeReactivated){
                EmailActivationSend.sendActivationEmail(user);
            }
            //Find any Twitanswers
            TwitanswerFinderAfterAccountInfoChange.findAndChangeUseridOfTwitanswers(user);
        }
    }

    public TreeMap<String, String> getNotificationFrequencies(){
        TreeMap<String, String> out  = new TreeMap<String, String>();
        out.put("1", "Every Day");
        out.put("7", "Every 7 Days");
        out.put("31", "Every 31 Days");
        out.put("0", "Never");
        return out;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }
}
