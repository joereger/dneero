package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Surveydiscuss extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int surveydiscussid;
     private int surveyid;
     private Date date;
     private boolean isapproved;
     private String subject;
     private String comment;
     private int userid;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Surveydiscuss get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Surveydiscuss");
        try {
            logger.debug("Surveydiscuss.get(" + id + ") called.");
            Surveydiscuss obj = (Surveydiscuss) HibernateUtil.getSession().get(Surveydiscuss.class, id);
            if (obj == null) {
                logger.debug("Surveydiscuss.get(" + id + ") returning new instance because hibernate returned null.");
                return new Surveydiscuss();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Surveydiscuss", ex);
            return new Surveydiscuss();
        }
    }

    // Constructors

    /** default constructor */
    public Surveydiscuss() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
         if (user.getUserid()==userid){
            return true;
        }
        return false;
    }




    // Property accessors


    public int getSurveydiscussid() {
        return surveydiscussid;
    }

    public void setSurveydiscussid(int surveydiscussid) {
        this.surveydiscussid = surveydiscussid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getIsapproved() {
        return isapproved;
    }

    public void setIsapproved(boolean isapproved) {
        this.isapproved = isapproved;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}