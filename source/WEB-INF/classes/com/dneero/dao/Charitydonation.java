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

public class Charitydonation extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
    private int charitydonationid;
    private int userid;
    private Date date;
    private String description;
    private double amt;
    private String charityname;
    private int balanceid;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Charitydonation get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Charitydonation");
        try {
            logger.debug("Charitydonation.get(" + id + ") called.");
            Charitydonation obj = (Charitydonation) HibernateUtil.getSession().get(Charitydonation.class, id);
            if (obj == null) {
                logger.debug("Charitydonation.get(" + id + ") returning new instance because hibernate returned null.");
                return new Charitydonation();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Charitydonation", ex);
            return new Charitydonation();
        }
    }

    // Constructors

    /** default constructor */
    public Charitydonation() {
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


    public int getCharitydonationid() {
        return charitydonationid;
    }

    public void setCharitydonationid(int charitydonationid) {
        this.charitydonationid = charitydonationid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getCharityname() {
        return charityname;
    }

    public void setCharityname(String charityname) {
        this.charityname = charityname;
    }

    public int getBalanceid() {
        return balanceid;
    }

    public void setBalanceid(int balanceid) {
        this.balanceid = balanceid;
    }
}
