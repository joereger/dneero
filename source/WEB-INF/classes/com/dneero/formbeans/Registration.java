package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class Registration {

    //Form props
    private String email;
    private String password;
    private String passwordverify;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String gender;
    private String ethnicity;
    private String income;
    private String maritalstatus;
    private String educationlevel;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Registration(){

    }

    public String registerAction(){
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        if (!password.equals(passwordverify)){
            Jsf.setFacesMessage("Password and Verify Password must match.");
            return null;
        }


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);

        try{
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        Blogger blogger = new Blogger();
        blogger.setUserid(user.getUserid());
        blogger.setBirthdate(birthdate);
        if (com.dneero.util.Num.isinteger(educationlevel)){
            blogger.setEducationlevel(Integer.parseInt(educationlevel));
        }
        if (com.dneero.util.Num.isinteger(ethnicity)){
            blogger.setEthnicity(Integer.parseInt(ethnicity));
        }
        if (com.dneero.util.Num.isinteger(gender)){
            blogger.setGender(Integer.parseInt(gender));
        }
        if (com.dneero.util.Num.isinteger(income)){
            blogger.setIncomerange(Integer.parseInt(income));
        }
        if (com.dneero.util.Num.isinteger(maritalstatus)){
            blogger.setMaritalstatus(Integer.parseInt(maritalstatus));
        }

        try{
            blogger.save();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        return "success";
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

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
