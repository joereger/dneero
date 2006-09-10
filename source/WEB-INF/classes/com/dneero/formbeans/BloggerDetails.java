package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.Bloggerbilling;
import com.dneero.dao.Researcherbilling;
import com.dneero.session.UserSession;
import com.dneero.session.Roles;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerDetails {

    //Form props
    private Date birthdate;
    private int notifyofnewsurveysbyemail=1;
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

    private String billingname;
    private String billingaddress1;
    private String billingaddress2;
    private String billingcity;
    private String billingstate;
    private String billingzip;
    private int billingpaymentmethod;
    private String ccnum;
    private int ccexpmonth;
    private int ccexpyear;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerDetails(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Blogger blogger = userSession.getUser().getBlogger();
            birthdate = blogger.getBirthdate();
            if (blogger.getNotifyofnewsurveysbyemail()){
                notifyofnewsurveysbyemail = 1;
            } else {
                notifyofnewsurveysbyemail = 0;
            }
            gender = String.valueOf(blogger.getGender());
            ethnicity = String.valueOf(blogger.getEthnicity());
            income = String.valueOf(blogger.getIncomerange());
            maritalstatus = String.valueOf(blogger.getMaritalstatus());
            educationlevel = String.valueOf(blogger.getEducationlevel());
            state = String.valueOf(blogger.getState());
            city = String.valueOf(blogger.getCity());
            profession = String.valueOf(blogger.getProfession());
            politics = String.valueOf(blogger.getPolitics());
            if (userSession.getUser().getBlogger().getBloggerbilling()!=null){
                Bloggerbilling bloggerbilling = userSession.getUser().getBlogger().getBloggerbilling();
                billingname = bloggerbilling.getBillingname();
                billingaddress1 = bloggerbilling.getBillingaddress1();
                billingaddress2 = bloggerbilling.getBillingaddress2();
                billingcity = bloggerbilling.getBillingcity();
                billingstate = bloggerbilling.getBillingstate();
                billingzip = bloggerbilling.getBillingzip();
                billingpaymentmethod = bloggerbilling.getBillingpaymentmethod();
                ccnum = bloggerbilling.getCcnum();
                ccexpmonth = bloggerbilling.getCcexpmonth();
                ccexpyear = bloggerbilling.getCcexpyear();
            }
        }
    }

    public String saveAction(){

        UserSession userSession = Jsf.getUserSession();

        Blogger blogger;
        boolean isnewblogger = false;
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            blogger =  userSession.getUser().getBlogger();
        } else {
            blogger = new Blogger();
            blogger.setQuality(0);
            blogger.setQuality90days(0);
            blogger.setLastnewsurveynotificationsenton(new Date());
            isnewblogger = true;
        }

        if (userSession.getUser()!=null){

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
            if (notifyofnewsurveysbyemail==0){
                blogger.setNotifyofnewsurveysbyemail(false);
            } else {
                blogger.setNotifyofnewsurveysbyemail(true);
            }

            userSession.getUser().setBlogger(blogger);

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
                userSession.getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }

            Bloggerbilling bloggerbilling = userSession.getUser().getBlogger().getBloggerbilling();
            if (bloggerbilling ==null){
                bloggerbilling = new Bloggerbilling();
            }

            bloggerbilling.setBloggerid(userSession.getUser().getBlogger().getBloggerid());
            bloggerbilling.setBillingname(billingname);
            bloggerbilling.setBillingaddress1(billingaddress1);
            bloggerbilling.setBillingaddress2(billingaddress2);
            bloggerbilling.setBillingcity(billingcity);
            bloggerbilling.setBillingstate(billingstate);
            bloggerbilling.setBillingzip(billingzip);
            bloggerbilling.setBillingpaymentmethod(billingpaymentmethod);
            bloggerbilling.setCcnum(ccnum);
            bloggerbilling.setCcexpmonth(ccexpmonth);
            bloggerbilling.setCcexpyear(ccexpyear);

            userSession.getUser().getBlogger().setBloggerbilling(bloggerbilling);

            try{
                bloggerbilling.save();
            } catch (GeneralException gex){
                logger.debug("Save blogger billing failed: " + gex.getErrorsAsSingleString());
                String message = "Blogger Details save failed: " + gex.getErrorsAsSingleString();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                return null;
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

    public String getBillingname() {
        return billingname;
    }

    public void setBillingname(String billingname) {
        this.billingname = billingname;
    }

    public String getBillingaddress1() {
        return billingaddress1;
    }

    public void setBillingaddress1(String billingaddress1) {
        this.billingaddress1 = billingaddress1;
    }

    public String getBillingaddress2() {
        return billingaddress2;
    }

    public void setBillingaddress2(String billingaddress2) {
        this.billingaddress2 = billingaddress2;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingstate() {
        return billingstate;
    }

    public void setBillingstate(String billingstate) {
        this.billingstate = billingstate;
    }

    public String getBillingzip() {
        return billingzip;
    }

    public void setBillingzip(String billingzip) {
        this.billingzip = billingzip;
    }

    public int getBillingpaymentmethod() {
        return billingpaymentmethod;
    }

    public void setBillingpaymentmethod(int billingpaymentmethod) {
        this.billingpaymentmethod = billingpaymentmethod;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCcexpmonth() {
        return ccexpmonth;
    }

    public void setCcexpmonth(int ccexpmonth) {
        this.ccexpmonth = ccexpmonth;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }

    public int getNotifyofnewsurveysbyemail() {
        return notifyofnewsurveysbyemail;
    }

    public void setNotifyofnewsurveysbyemail(int notifyofnewsurveysbyemail) {
        this.notifyofnewsurveysbyemail = notifyofnewsurveysbyemail;
    }
}
