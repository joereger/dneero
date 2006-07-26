package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Payblogger extends BasePersistentClass implements java.io.Serializable, RegerEntity {

    public static int STATUS_PENDINGRESEARCHER = 1;
    public static int STATUS_OWED = 2;
    public static int STATUS_PAYERROR = 3;
    public static int STATUS_PAID = 4;

    // Fields
     private int paybloggerid;
     private int bloggerid;
     private int invoiceid;
     private int status;
     private double amt;
     private Set<Paybloggertransaction> paybloggertransactions = new HashSet<Paybloggertransaction>();

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Payblogger get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Payblogger");
        try {
            logger.debug("Payblogger.get(" + id + ") called.");
            Payblogger obj = (Payblogger) HibernateUtil.getSession().get(Payblogger.class, id);
            if (obj == null) {
                logger.debug("Payblogger.get(" + id + ") returning new instance because hibernate returned null.");
                return new Payblogger();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Payblogger", ex);
            return new Payblogger();
        }
    }

    // Constructors

    /** default constructor */
    public Payblogger() {
    }





    // Property accessors

    public int getPaybloggerid() {
        return paybloggerid;
    }

    public void setPaybloggerid(int paybloggerid) {
        this.paybloggerid = paybloggerid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public Set<Paybloggertransaction> getPaybloggertransactions() {
        return paybloggertransactions;
    }

    public void setPaybloggertransactions(Set<Paybloggertransaction> paybloggertransactions) {
        this.paybloggertransactions = paybloggertransactions;
    }

}