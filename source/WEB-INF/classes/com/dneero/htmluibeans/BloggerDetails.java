package com.dneero.htmluibeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.Venue;
import com.dneero.finders.DemographicsXML;
import com.dneero.finders.UserProfileCompletenessChecker;
import com.dneero.helpers.TwitanswerFinderAfterAccountInfoChange;
import com.dneero.helpers.VenueUtils;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerDetails implements Serializable {

    private int userid;
    private Date birthdate;
    private DemographicsXML demographicsXML;
    private String venueurl;
    private String venuefocus;
    private int venuecount = 0;
    private boolean isnewblogger;


    public BloggerDetails(){
        
    }


    public void initBean(){
        if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
            Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
            demographicsXML = new DemographicsXML(blogger.getDemographicsxml(), Pagez.getUserSession().getPl(), false);
            birthdate = blogger.getBirthdate();
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
            //WTF is this here?
            birthdate = new Date();
            demographicsXML = new DemographicsXML("", Pagez.getUserSession().getPl(), false);
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
            blogger.setBlogfocus("NA"); //@todo Delete once in production
            blogger.setGender("NA"); //@todo Delete once in production
            blogger.setCity("NA"); //@todo Delete once in production
            blogger.setCountry("NA"); //@todo Delete once in production
            blogger.setEducationlevel("NA"); //@todo Delete once in production
            blogger.setEthnicity("NA"); //@todo Delete once in production
            blogger.setIncomerange("NA"); //@todo Delete once in production
            blogger.setMaritalstatus("NA"); //@todo Delete once in production
            blogger.setState("NA"); //@todo Delete once in production
            blogger.setPolitics("NA"); //@todo Delete once in production
            blogger.setProfession("NA"); //@todo Delete once in production
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
                if (Pagez.getUserSession().getPl().getIsvenuerequired()){
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
            }
            //Throw the error
            if (haveValidationError){
                throw vex;
            }
            //End validation

            blogger.setUserid(Pagez.getUserSession().getUser().getUserid());
            blogger.setBirthdate(birthdate);
            //@todo Validation of Demographic input???
            blogger.setDemographicsxml(demographicsXML.getAsString());
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

            //Find Twitanswers with no blogger status
            TwitanswerFinderAfterAccountInfoChange.findNoBloggerStatusTwitanswers(Pagez.getUserSession().getUser());


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

    public DemographicsXML getDemographicsXML() {
        return demographicsXML;
    }

    public void setDemographicsXML(DemographicsXML demographicsXML) {
        this.demographicsXML = demographicsXML;
    }

    public boolean getIsnewblogger() {
        return isnewblogger;
    }

    public void setIsnewblogger(boolean isnewblogger) {
        this.isnewblogger=isnewblogger;
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
