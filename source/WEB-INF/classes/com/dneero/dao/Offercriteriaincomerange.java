package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

/**
 * Offer generated by hbm2java
 */

public class Offercriteriaincomerange extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields

     private int offercriteriaincomerangeid;
     private int offercriteriaid;
     //Value of ethnicity drawn from com.dneero.Blogger
     private int incomerange;




    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Offercriteriaincomerange get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Offercriteriaincomerange");
        try {
            logger.debug("Offercriteriaincomerange.get(" + id + ") called.");
            Offercriteriaincomerange obj = (Offercriteriaincomerange) HibernateUtil.getSession().get(Offercriteriaincomerange.class, id);
            if (obj == null) {
                logger.debug("Offercriteriaincomerange.get(" + id + ") returning new instance because hibernate returned null.");
                return new Offercriteriaincomerange();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Offercriteriaincomerange", ex);
            return new Offercriteriaincomerange();
        }
    }

    // Constructors

    /** default constructor */
    public Offercriteriaincomerange() {
    }




    // Property accessors

    public int getOffercriteriaincomerangeid() {
        return offercriteriaincomerangeid;
    }

    public void setOffercriteriaincomerangeid(int offercriteriaincomerangeid) {
        this.offercriteriaincomerangeid = offercriteriaincomerangeid;
    }

    public int getOffercriteriaid() {
        return offercriteriaid;
    }

    public void setOffercriteriaid(int offercriteriaid) {
        this.offercriteriaid = offercriteriaid;
    }

    public int getIncomerange() {
        return incomerange;
    }

    public void setIncomerange(int incomerange) {
        this.incomerange = incomerange;
    }


}
