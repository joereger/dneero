package com.dneero.dao;

import com.dneero.util.GeneralException;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
// Generated Apr 17, 2006 3:45:23 PM by Hibernate Tools 3.1.0.beta4



/**
 * Blogger generated by hbm2java
 */

public class Blogger extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int bloggerid;
     private int userid;
     private Date birthdate;
     private String gender;
     private String ethnicity;
     private String maritalstatus;
     private String incomerange;
     private String educationlevel;
     private String state;
     private String city;
     private String profession;
     private String politics;
     private String blogfocus;
     private double quality;
     private double quality90days;
     private double socialinfluencerating;
     private double socialinfluencerating90days;
     private int socialinfluenceratingranking;
     private int socialinfluenceratingranking90days;
     private Set<Response> responses = new HashSet<Response>();
     private Set<Impressionpaymentgroup> impressionpaymentgroups = new HashSet<Impressionpaymentgroup>();
     private Set<Panelmembership> panelmemberships = new HashSet<Panelmembership>();

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Blogger get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Blogger");
        try {
            logger.debug("Blogger.get(" + id + ") called.");
            Blogger obj = (Blogger) HibernateUtil.getSession().get(Blogger.class, id);
            if (obj == null) {
                logger.debug("Blogger.get(" + id + ") returning new instance because hibernate returned null.");
                return new Blogger();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Blogger", ex);
            return new Blogger();
        }
    }

    // Constructors

    /** default constructor */
    public Blogger() {
    }

    
    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }
    

   
    // Property accessors

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBloggerid() {
        return this.bloggerid;
    }
    
    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
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

    public String getIncomerange() {
        return incomerange;
    }

    public void setIncomerange(String incomerange) {
        this.incomerange = incomerange;
    }

    public String getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String educationlevel) {
        this.educationlevel = educationlevel;
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

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getQuality90days() {
        return quality90days;
    }

    public void setQuality90days(double quality90days) {
        this.quality90days = quality90days;
    }


    public Set<Impressionpaymentgroup> getImpressionpaymentgroups() {
        return impressionpaymentgroups;
    }

    public void setImpressionpaymentgroups(Set<Impressionpaymentgroup> impressionpaymentgroups) {
        this.impressionpaymentgroups = impressionpaymentgroups;
    }

    public Set<Panelmembership> getPanelmemberships() {
        return panelmemberships;
    }

    public void setPanelmemberships(Set<Panelmembership> panelmemberships) {
        this.panelmemberships = panelmemberships;
    }


    public double getSocialinfluencerating() {
        return socialinfluencerating;
    }

    public void setSocialinfluencerating(double socialinfluencerating) {
        this.socialinfluencerating = socialinfluencerating;
    }

    public double getSocialinfluencerating90days() {
        return socialinfluencerating90days;
    }

    public void setSocialinfluencerating90days(double socialinfluencerating90days) {
        this.socialinfluencerating90days = socialinfluencerating90days;
    }

    public int getSocialinfluenceratingranking() {
        return socialinfluenceratingranking;
    }

    public void setSocialinfluenceratingranking(int socialinfluenceratingranking) {
        this.socialinfluenceratingranking = socialinfluenceratingranking;
    }

    public int getSocialinfluenceratingranking90days() {
        return socialinfluenceratingranking90days;
    }

    public void setSocialinfluenceratingranking90days(int socialinfluenceratingranking90days) {
        this.socialinfluenceratingranking90days = socialinfluenceratingranking90days;
    }

    public String getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String blogfocus) {
        this.blogfocus = blogfocus;
    }
}
