package com.dneero.bulkusercreation;

import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.eula.EulaHelper;
import com.dneero.helpers.CreateEmptyBloggerProfile;
import com.dneero.helpers.NicknameHelper;
import com.dneero.htmlui.Pagez;
import com.dneero.money.PaymentMethod;
import com.dneero.sir.SocialInfluenceRating;
import com.dneero.util.RandomString;
import com.dneero.util.Str;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 21, 2010
 * Time: 8:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bulkuser {

    private String name;
    private String nickname;
    private String email;
    private String password;

    private boolean isvalid = false;
    private String errors = "";
    private User user;

    public Bulkuser(String name, String nickname, String email, String password) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        validate();
    }


    public void validate(){
        isvalid = true;
        errors = "";
        //Basic required checks
        if (name==null || name.length()<=0){
            errors = errors + "Name is required. ";
        }
        if (nickname==null || nickname.length()<=0){
            errors = errors + "Nick Name is required. ";
        }
        if (email==null || email.length()<=0){
            errors = errors + "Email is required. ";
        }
        if (password==null || password.length()<=0){
            errors = errors + "Password is required. ";
        }
        //Check email form
        email=email.toLowerCase();
        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)){ errors = errors + "Not a valid email address. ";}
        //Check email in db
        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){  errors = errors + "That email address is already in use."; }
        //Check nickname in db
        nickname = Str.onlyKeepLettersAndDigits(nickname);
        nickname =  nickname.toLowerCase();
        if (NicknameHelper.nicknameExistsAlready(nickname)){ errors = errors + "Nickname already in use. "; }
        //Set the isvalid flag
        if (errors.length()>0){
            isvalid = false;
        }
    }

    public void createUser(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            User user = new User();
            user.setPlid(Pagez.getUserSession().getPl().getPlid());
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setIsactivatedbyemail(true);
            user.setIsqualifiedforrevshare(true);
            user.setReferredbyuserid(Pagez.getUserSession().getReferredbyOnlyUsedForSignup());
            user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
            user.setEmailactivationlastsent(new Date());
            user.setCreatedate(new Date());
            user.setPaymethodpaypaladdress("");
            user.setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
            user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
            user.setPaymethodcreditcardid(0);
            user.setChargemethodcreditcardid(0);
            user.setBloggerid(0);
            user.setResearcherid(0);
            user.setNotifyofnewsurveysbyemaileveryexdays(1);
            user.setNotifyofnewsurveyslastsent(new Date());
            user.setAllownoncriticalemails(true);
            user.setInstantnotifybyemailison(false);
            user.setInstantnotifyxmppison(false);
            user.setInstantnotifyxmppusername("");
            user.setIsenabled(true);
            user.setFacebookappremoveddate(new Date());
            user.setIsfacebookappremoved(false);
            user.setResellercode(RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7));
            user.setResellerpercent(0.0);
            user.setCurrentbalance(0.0);
            user.setCurrentbalanceblogger(0.0);
            user.setCurrentbalanceresearcher(0.0);
            user.setLastlogindate(new java.util.Date());
            user.setNickname(NicknameHelper.generateUniqueNickname(nickname, null));
            user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
            user.setSirdate(new Date());
            user.setSirdebug("");
            user.setSirpoints(0.0);
            user.setSirrank(0);
            user.setIsanonymous(false);
            try{
                user.save();
            } catch (Exception ex){
                logger.error("", ex);
            }

            //Eula
            Usereula usereula = new Usereula();
            usereula.setDate(new Date());
            usereula.setEulaid(EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEulaid());
            usereula.setUserid(user.getUserid());
            usereula.setIp("");
            try{
                usereula.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
            user.getUsereulas().add(usereula);

            //Create the blogger
            CreateEmptyBloggerProfile.create(user);

            //Set to this user
            this.user = user;
        } catch (Exception ex){
            logger.error("", ex);
        }
    }



    public boolean getIsvalid() {
        return isvalid;
    }

    public String getErrors() {
        return errors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
