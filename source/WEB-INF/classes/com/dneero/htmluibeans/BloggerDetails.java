package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;


import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.PaymentMethod;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerDetails implements Serializable {

    //Form props
    private Date birthdate;
    private String gender;
    private String ethnicity;
    private String income;
    private String maritalstatus;
    private String educationlevel;
    private String state;
    private String city;
    private String profession;
    private String blogfocus;
    private String politics;

    private boolean isnewblogger;




    //Other props
    private int userid;



    public BloggerDetails(){
        
    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(userSession.getUser().getBloggerid());
            birthdate = blogger.getBirthdate();
            gender = String.valueOf(blogger.getGender());
            ethnicity = String.valueOf(blogger.getEthnicity());
            income = String.valueOf(blogger.getIncomerange());
            maritalstatus = String.valueOf(blogger.getMaritalstatus());
            educationlevel = String.valueOf(blogger.getEducationlevel());
            state = String.valueOf(blogger.getState());
            city = String.valueOf(blogger.getCity());
            profession = String.valueOf(blogger.getProfession());
            politics = String.valueOf(blogger.getPolitics());
            blogfocus = blogger.getBlogfocus();
        } else {
            birthdate = new Date();
        }
    }



    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

        Blogger blogger;
        isnewblogger = false;
        if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            blogger =  Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
        } else {
            blogger = new Blogger();
            blogger.setQuality(0);
            blogger.setQuality90days(0);
            isnewblogger = true;
        }

        if (Pagez.getUserSession().getUser()!=null){


            //Validation of answers
            boolean haveValidationError = false;
            Calendar birthdateCal = Time.getCalFromDate(birthdate);
            logger.debug("birthdateCal="+Time.dateformatfordb(birthdateCal));
            if (birthdateCal.after(Time.subtractYear(Calendar.getInstance(), 13))){
                vex.addValidationError("You must be at least 13 years of age to use this system.");
                haveValidationError = true;
            }
            if (birthdateCal.before(Time.subtractYear(Calendar.getInstance(), 110))){
                vex.addValidationError("Please check your date and enter the year in YYYY format (i.e. 1975).");
                haveValidationError = true;
            }
            if (haveValidationError){
                throw vex;
            }
            //End validation


            blogger.setUserid(Pagez.getUserSession().getUser().getUserid());
            blogger.setBirthdate(birthdate);
            blogger.setEducationlevel(educationlevel);
            blogger.setEthnicity(ethnicity);
            blogger.setGender(gender);
            blogger.setIncomerange(income);
            blogger.setMaritalstatus(maritalstatus);
            blogger.setState(state);
            blogger.setCity(city);
            blogger.setProfession(profession);
            blogger.setPolitics(politics);
            blogger.setBlogfocus(blogfocus);

            try{
                blogger.save();
            } catch (GeneralException gex){
                vex.addValidationError("Error saving record.");
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                throw vex;
            }

            if (isnewblogger){
                Pagez.getUserSession().getUser().setBloggerid(blogger.getBloggerid());
                try{Pagez.getUserSession().getUser().save();}catch(Exception ex){logger.error("",ex);}
            }



            boolean hasroleassigned = false;
            if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getUserroles()!=null){
                for (Iterator iterator = Pagez.getUserSession().getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Userrole.BLOGGER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && Pagez.getUserSession().getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(Pagez.getUserSession().getUser().getUserid());
                role.setRoleid(Userrole.BLOGGER);
                Pagez.getUserSession().getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    vex.addValidationError("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    throw vex;
                }
            }


           
            try{
                Pagez.getUserSession().getUser().save();
            } catch (GeneralException gex){
                vex.addValidationError("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                throw vex;
            }

            Pagez.getUserSession().getUser().refresh();


        } else {
            vex.addValidationError("UserSession.getUser() is null.  Please log in.");
            throw vex;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String blogfocus) {
        this.blogfocus = blogfocus;
    }

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }


    public boolean getIsnewblogger() {
        return isnewblogger;
    }

    public void setIsnewblogger(boolean isnewblogger) {
        this.isnewblogger=isnewblogger;
    }
}
