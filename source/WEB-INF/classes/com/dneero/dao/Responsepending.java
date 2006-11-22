package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Responsepending extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int responsependingid;
     private int userid;
     private int surveyid;
     private String responseasstring;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Responsepending get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Responsepending");
        try {
            logger.debug("Responsepending.get(" + id + ") called.");
            Responsepending obj = (Responsepending) HibernateUtil.getSession().get(Responsepending.class, id);
            if (obj == null) {
                logger.debug("Responsepending.get(" + id + ") returning new instance because hibernate returned null.");
                return new Responsepending();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Responsepending", ex);
            return new Responsepending();
        }
    }

    // Constructors

    /** default constructor */
    public Responsepending() {
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


    public int getResponsependingid() {
        return responsependingid;
    }

    public void setResponsependingid(int responsependingid) {
        this.responsependingid = responsependingid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getResponseasstring() {
        return responseasstring;
    }

    public void setResponseasstring(String responseasstring) {
        this.responseasstring = responseasstring;
    }


    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }
}
