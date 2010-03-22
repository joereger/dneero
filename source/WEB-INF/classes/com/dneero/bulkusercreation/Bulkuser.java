package com.dneero.bulkusercreation;

import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.helpers.NicknameHelper;
import com.dneero.util.Str;
import org.apache.commons.validator.EmailValidator;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 21, 2010
 * Time: 8:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bulkuser {

    private String first;
    private String last;
    private String nickname;
    private String email;
    private String password;

    private boolean isvalid = false;
    private String errors = "";

    public Bulkuser(String first, String last, String nickname, String email, String password) {
        this.first = first;
        this.last = last;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        validate();
    }


    public void validate(){
        isvalid = true;
        errors = "";
        //Basic required checks
        if (first==null || first.length()<=0){
            errors = errors + "First Name is required. ";
        }
        if (last==null || last.length()<=0){
            errors = errors + "Last Name is required. ";
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

    public boolean getIsvalid() {
        return isvalid;
    }

    public String getErrors() {
        return errors;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
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
