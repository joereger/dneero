package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.session.UserSession;
import com.dneero.session.Roles;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerDetails {

    //Form props
    private Date birthdate;
    private String gender;
    private String ethnicity;
    private String income;
    private String maritalstatus;
    private String educationlevel;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerDetails(){
        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Blogger blogger = userSession.getUser().getBlogger();
            birthdate = blogger.getBirthdate();
            gender = String.valueOf(blogger.getGender());
            ethnicity = String.valueOf(blogger.getEthnicity());
            income = String.valueOf(blogger.getIncomerange());
            maritalstatus = String.valueOf(blogger.getMaritalstatus());
            educationlevel = String.valueOf(blogger.getEducationlevel());
        }
    }

    public String saveAction(){

        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");

        Blogger blogger;
        boolean isnewblogger = false;
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            blogger =  userSession.getUser().getBlogger();
        } else {
            blogger = new Blogger();
            isnewblogger = true;
        }

        if (userSession.getUser()!=null){

            blogger.setUserid(userSession.getUser().getUserid());
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
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }


            boolean hasroleassigned = false;
            if (userSession.getUser()!=null && userSession.getUser().getUserroles()!=null){
                for (Iterator iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Roles.BLOGGER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && userSession.getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(userSession.getUser().getUserid());
                role.setRoleid(Roles.BLOGGER);
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }

            userSession.getUser().refresh();

            if (isnewblogger){
                return "success_newblogger";
            } else {
                return "success";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
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