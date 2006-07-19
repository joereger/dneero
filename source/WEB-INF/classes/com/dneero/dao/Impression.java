package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;

/**
 * Blogger generated by hbm2java
 */

public class Impression extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields
     private int impressionid;
     private int surveyid;
     private int blogid;
     private String referer;
     private int totalimpressions;
     private Set<Impressiondetail> impressiondetails = new HashSet<Impressiondetail>();



    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Impression get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Impression");
        try {
            logger.debug("Impression.get(" + id + ") called.");
            Impression obj = (Impression) HibernateUtil.getSession().get(Impression.class, id);
            if (obj == null) {
                logger.debug("Impression.get(" + id + ") returning new instance because hibernate returned null.");
                return new Impression();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Impression", ex);
            return new Impression();
        }
    }

    // Constructors

    /** default constructor */
    public Impression() {
    }






    // Property accessors

    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid = impressionid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public int getTotalimpressions() {
        return totalimpressions;
    }

    public void setTotalimpressions(int totalimpressions) {
        this.totalimpressions = totalimpressions;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public Set<Impressiondetail> getImpressiondetails() {
        return impressiondetails;
    }

    public void setImpressiondetails(Set<Impressiondetail> impressiondetails) {
        this.impressiondetails = impressiondetails;
    }
}
