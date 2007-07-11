package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
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
    private String paymethodpaypaladdress;




    //Other props
    private int userid;



    public BloggerDetails(){

    }

    public String beginView(){
        load();
        return "bloggerdetails";
    }

    public void load(){
        UserSession userSession = Jsf.getUserSession();
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
            paymethodpaypaladdress = userSession.getUser().getPaymethodpaypaladdress();
            blogfocus = blogger.getBlogfocus();
        }
    }



    public String saveAction(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        UserSession userSession = Jsf.getUserSession();

        Blogger blogger;
        boolean isnewblogger = false;
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            blogger =  Blogger.get(userSession.getUser().getBloggerid());
        } else {
            blogger = new Blogger();
            blogger.setQuality(0);
            blogger.setQuality90days(0);
            isnewblogger = true;
        }

        if (userSession.getUser()!=null){


            //Validation of answers
            boolean haveValidationError = false;
            Calendar birthdateCal = Time.getCalFromDate(birthdate);
            if (birthdateCal.after(Time.subtractYear(Calendar.getInstance(), 13))){
                Jsf.setFacesMessage("bloggerdetails:birthdate", "You must be at least 13 years of age to use this system.");
                haveValidationError = true;
            }
            long cnt = (Long)HibernateUtil.getSession().createQuery("select count(*) from User where paymethodpaypaladdress='"+paymethodpaypaladdress+"' and userid<>'"+userSession.getUser().getUserid()+"' and paymethodpaypaladdress<>'' and paymethodpaypaladdress IS NOT NULL").uniqueResult();
            if (cnt>0){
                Jsf.setFacesMessage("bloggerdetails:paymethodpaypaladdress", "That PayPal address is already in use by another user.");
                haveValidationError = true;
            }
            if (haveValidationError){
                return null;
            }
            //End validation


            blogger.setUserid(userSession.getUser().getUserid());
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
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            if (isnewblogger){
                userSession.getUser().setBloggerid(blogger.getBloggerid());
                try{userSession.getUser().save();}catch(Exception ex){logger.error(ex);}
            }



            boolean hasroleassigned = false;
            if (userSession.getUser()!=null && userSession.getUser().getUserroles()!=null){
                for (Iterator iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Userrole.BLOGGER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && userSession.getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(userSession.getUser().getUserid());
                role.setRoleid(Userrole.BLOGGER);
                userSession.getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }


            userSession.getUser().setPaymethod(PaymentMethod.PAYMENTMETHODPAYPAL);
            userSession.getUser().setPaymethodpaypaladdress(paymethodpaypaladdress);
            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            userSession.getUser().refresh();

            if (isnewblogger){
                BloggerIndex bean = (BloggerIndex)Jsf.getManagedBean("bloggerIndex");
                bean.setMsg("Profile saved successfully.");
                return bean.beginView();
                //return "bloggerindex";
            } else {
                BloggerIndex bean = (BloggerIndex)Jsf.getManagedBean("bloggerIndex");
                bean.setMsg("Profile saved successfully.");
                return bean.beginView();
                //return "bloggerindex";
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

    public String getPaymethodpaypaladdress() {
        return paymethodpaypaladdress;
    }

    public void setPaymethodpaypaladdress(String paymethodpaypaladdress) {
        this.paymethodpaypaladdress = paymethodpaypaladdress;
    }


    
}
