package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import org.apache.log4j.Logger;

/**
 * Offer generated by hbm2java
 */

public class Offercriteriaethnicity extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields

     private int offercriteriaethnicityid;
     private int offercriteriaid;
     //Value of ethnicity drawn from com.dneero.Blogger
     private int ethnicity;




    //Validator
    public void validateRegerEntity() throws GeneralException {
    
    }

    //Loader
    public void load(){

    }

    public static Offercriteriaethnicity get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Offercriteriaethnicity");
        try {
            logger.debug("Offercriteriaethnicity.get(" + id + ") called.");
            Offercriteriaethnicity obj = (Offercriteriaethnicity) HibernateUtil.getSession().get(Offercriteriaethnicity.class, id);
            if (obj == null) {
                logger.debug("Offercriteriaethnicity.get(" + id + ") returning new instance because hibernate returned null.");
                return new Offercriteriaethnicity();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Offercriteriaethnicity", ex);
            return new Offercriteriaethnicity();
        }
    }

    // Constructors

    /** default constructor */
    public Offercriteriaethnicity() {
    }




    // Property accessors

    public int getOffercriteriaethnicityid() {
        return offercriteriaethnicityid;
    }

    public void setOffercriteriaethnicityid(int offercriteriaethnicityid) {
        this.offercriteriaethnicityid = offercriteriaethnicityid;
    }

    public int getOffercriteriaid() {
        return offercriteriaid;
    }

    public void setOffercriteriaid(int offercriteriaid) {
        this.offercriteriaid = offercriteriaid;
    }

    public int getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(int ethnicity) {
        this.ethnicity = ethnicity;
    }


}
