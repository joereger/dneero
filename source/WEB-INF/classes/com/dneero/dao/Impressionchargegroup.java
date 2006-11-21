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

public class Impressionchargegroup extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

    // Fields
     private int impressionchargegroupid;
     private int researcherid;
     private Date date;
     private double amt;

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Impressionchargegroup get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Impressionchargegroup");
        try {
            logger.debug("Impressionchargegroup.get(" + id + ") called.");
            Impressionchargegroup obj = (Impressionchargegroup) HibernateUtil.getSession().get(Impressionchargegroup.class, id);
            if (obj == null) {
                logger.debug("Impressionchargegroup.get(" + id + ") returning new instance because hibernate returned null.");
                return new Impressionchargegroup();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Impressionchargegroup", ex);
            return new Impressionchargegroup();
        }
    }

    // Constructors

    /** default constructor */
    public Impressionchargegroup() {
    }


    public boolean canRead(User user){
        if (user.getUserid()==Researcher.get(researcherid).getUserid()){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }


    // Property accessors


    public int getImpressionchargegroupid() {
        return impressionchargegroupid;
    }

    public void setImpressionchargegroupid(int impressionchargegroupid) {
        this.impressionchargegroupid = impressionchargegroupid;
    }


    public int getResearcherid() {
        return researcherid;
    }

    public void setResearcherid(int researcherid) {
        this.researcherid = researcherid;
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


}