package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Revshare extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int revshareid;
     private int sourcebloggerid;
     private int targetbloggerid;
     private double amt;
     private Date date;
     private int paybloggerid;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Revshare get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Revshare");
        try {
            logger.debug("Revshare.get(" + id + ") called.");
            Revshare obj = (Revshare) HibernateUtil.getSession().get(Revshare.class, id);
            if (obj == null) {
                logger.debug("Revshare.get(" + id + ") returning new instance because hibernate returned null.");
                return new Revshare();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Revshare", ex);
            return new Revshare();
        }
    }

    // Constructors

    /** default constructor */
    public Revshare() {
    }


    public boolean canRead(User user){
        if (user.getUserid()==Blogger.get(targetbloggerid).getUserid()){
            return true;
        } 
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    // Property accessors

    public int getRevshareid() {
        return revshareid;
    }

    public void setRevshareid(int revshareid) {
        this.revshareid = revshareid;
    }

    public int getSourcebloggerid() {
        return sourcebloggerid;
    }

    public void setSourcebloggerid(int sourcebloggerid) {
        this.sourcebloggerid = sourcebloggerid;
    }

    public int getTargetbloggerid() {
        return targetbloggerid;
    }

    public void setTargetbloggerid(int targetbloggerid) {
        this.targetbloggerid = targetbloggerid;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPaybloggerid() {
        return paybloggerid;
    }

    public void setPaybloggerid(int paybloggerid) {
        this.paybloggerid = paybloggerid;
    }
}
