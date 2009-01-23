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
import com.dneero.dao.Venue;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.PaymentMethod;
import com.dneero.finders.UserProfileCompletenessChecker;
import com.dneero.helpers.VenueUtils;


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
    private String country;
    private String venueurl;
    private String venuefocus;
    private int venuecount = 0;

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
            country = blogger.getCountry();
            venueurl = "";
            venuefocus = "";
            venuecount = 0;
            if (blogger.getVenues()!=null && blogger.getVenues().size()>0){
                for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                    Venue venue=iterator.next();
                    if (venue.getIsactive()){
                        venuecount = venuecount + 1;
                    }
                }
            }
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
            if (Pagez.getUserSession().getUser().getFacebookuserid()<=0){
                venuecount = 0;
                if (blogger.getVenues()!=null && blogger.getVenues().size()>0){
                    for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                        Venue venue=iterator.next();
                        if (venue.getIsactive()){
                            venuecount = venuecount + 1;
                        }
                    }
                }
                if (venuecount==0 && (venueurl.equals("") || venuefocus.equals(""))){
                    vex.addValidationError("You must provide at least one posting venue url and focus.");
                    haveValidationError = true;
                }
                if (VenueUtils.isVenueUrlInUse(venueurl)){
                    vex.addValidationError("The new venue url you provided is already in use.");
                    haveValidationError = true;
                }
                if(venueurl!=null && !venueurl.equals("") && venuefocus!=null && !venuefocus.equals("")){
                    if (venuecount==5){
                        vex.addValidationError("Sorry, you can have at most 5 posting venues.");
                        haveValidationError = true;
                    }
                }
            }
            //Throw the error
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
            blogger.setBlogfocus("");
            blogger.setCountry(country);

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

            if (!venueurl.equals("")){
                //Go get a new venue object or one that's inactive.
                Venue venue = VenueUtils.getNewOrInactive(venueurl);
                //When a blogger takes over another blogger's url (extremely rare) we need to refresh that blogger
                int currentbloggerid = venue.getBloggerid();
                Blogger bloggerOther = null;
                if (currentbloggerid!=blogger.getBloggerid()){
                    bloggerOther = Blogger.get(currentbloggerid);
                }
                venue.setBloggerid(blogger.getBloggerid());
                venue.setUrl(VenueUtils.cleanForSaveToDb(venueurl));
                venue.setFocus(venuefocus);
                venue.setIsdueforreview(true);
                venue.setIsresearcherrejected(false);
                venue.setIsresearcherreviewed(false);
                venue.setIssysadminrejected(false);
                venue.setIssysadminreviewed(false);
                venue.setLastsysadminreviewdate(new Date());
                venue.setScorebysysadmin(0);
                venue.setIsactive(true);
                //Also set the blogger focus to the last used venuefocus... something of a hack but works for now
                blogger.setBlogfocus(venuefocus);
                try{
                    venue.save();
                    blogger.save();
                    blogger.refresh();
                    if (bloggerOther!=null){
                        bloggerOther.refresh();
                    }
                } catch (GeneralException gex){
                    vex.addValidationError("Error saving venue record: "+gex.getErrorsAsSingleString());
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

            if (!UserProfileCompletenessChecker.isProfileComplete(Pagez.getUserSession().getUser())){
                Pagez.getUserSession().setIsbloggerprofileok(false);
            } else {
                Pagez.getUserSession().setIsbloggerprofileok(true);
            }


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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country=country;
    }

    public String getVenueurl() {
        return venueurl;
    }

    public void setVenueurl(String venueurl) {
        this.venueurl=venueurl;
    }

    public String getVenuefocus() {
        return venuefocus;
    }

    public void setVenuefocus(String venuefocus) {
        this.venuefocus=venuefocus;
    }

    public int getVenuecount() {
        return venuecount;
    }

    public void setVenuecount(int venuecount) {
        this.venuecount=venuecount;
    }
}
