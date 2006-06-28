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
 * Offer generated by hbm2java
 */

public class Offercriteria extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields

     private int offercriteriaid;
     private int offerid;
     private int agemin;
     private int agemax;

    private Set<Offercriteriagender> offercriteriagender = new HashSet<Offercriteriagender>();
    public Set<Offercriteriagender> getOffercriteriagender() {
        return offercriteriagender;
    }
    public void setOffercriteriagender(Set<Offercriteriagender> offercriteriagender) {
        this.offercriteriagender = offercriteriagender;
    }

    private Set<Offercriteriaethnicity> offercriteriaethnicity = new HashSet<Offercriteriaethnicity>();
    public Set<Offercriteriaethnicity> getOffercriteriaethnicity() {
        return offercriteriaethnicity;
    }
    public void setOffercriteriaethnicity(Set<Offercriteriaethnicity> offercriteriaethnicity) {
        this.offercriteriaethnicity = offercriteriaethnicity;
    }

    private Set<Offercriteriamaritalstatus> offercriteriamaritalstatus = new HashSet<Offercriteriamaritalstatus>();
    public Set<Offercriteriamaritalstatus> getOffercriteriamaritalstatus() {
        return offercriteriamaritalstatus;
    }
    public void setOffercriteriamaritalstatus(Set<Offercriteriamaritalstatus> offercriteriamaritalstatus) {
        this.offercriteriamaritalstatus = offercriteriamaritalstatus;
    }

    private Set<Offercriteriaincomerange> offercriteriaincomerange = new HashSet<Offercriteriaincomerange>();
    public Set<Offercriteriaincomerange> getOffercriteriaincomerange() {
        return offercriteriaincomerange;
    }
    public void setOffercriteriaincomerange(Set<Offercriteriaincomerange> offercriteriaincomerange) {
        this.offercriteriaincomerange = offercriteriaincomerange;
    }

    private Set<Offercriteriaeducationlevel> offercriteriaeducationlevel = new HashSet<Offercriteriaeducationlevel>();
    public Set<Offercriteriaeducationlevel> getOffercriteriaeducationlevel() {
        return offercriteriaeducationlevel;
    }
    public void setOffercriteriaeducationlevel(Set<Offercriteriaeducationlevel> offercriteriaeducationlevel) {
        this.offercriteriaeducationlevel = offercriteriaeducationlevel;
    }


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Offercriteria get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Offer");
        try {
            logger.debug("Offercriteria.get(" + id + ") called.");
            Offercriteria obj = (Offercriteria) HibernateUtil.getSession().get(Offercriteria.class, id);
            if (obj == null) {
                logger.debug("Offercriteria.get(" + id + ") returning new instance because hibernate returned null.");
                return new Offercriteria();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Offercriteria", ex);
            return new Offercriteria();
        }
    }

    // Constructors

    /** default constructor */
    public Offercriteria() {
    }




    // Property accessors

    public int getOffercriteriaid() {
        return offercriteriaid;
    }

    public void setOffercriteriaid(int offercriteriaid) {
        this.offercriteriaid = offercriteriaid;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public int getAgemin() {
        return agemin;
    }

    public void setAgemin(int agemin) {
        this.agemin = agemin;
    }

    public int getAgemax() {
        return agemax;
    }

    public void setAgemax(int agemax) {
        this.agemax = agemax;
    }




}