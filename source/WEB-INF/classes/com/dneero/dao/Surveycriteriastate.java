package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

/**
 * Survey generated by hbm2java
 */

public class Surveycriteriastate extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields

     private int surveycriteriastateid;
     private int surveycriteriaid;
     private int state;




    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Surveycriteriastate get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Surveycriteriastate");
        try {
            logger.debug("Surveycriteriastate.get(" + id + ") called.");
            Surveycriteriastate obj = (Surveycriteriastate) HibernateUtil.getSession().get(Surveycriteriastate.class, id);
            if (obj == null) {
                logger.debug("Surveycriteriastate.get(" + id + ") returning new instance because hibernate returned null.");
                return new Surveycriteriastate();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Surveycriteriastate", ex);
            return new Surveycriteriastate();
        }
    }

    // Constructors

    /** default constructor */
    public Surveycriteriastate() {
    }




    // Property accessors

    public int getSurveycriteriastateid() {
        return surveycriteriastateid;
    }

    public void setSurveycriteriastateid(int surveycriteriastateid) {
        this.surveycriteriastateid = surveycriteriastateid;
    }

    public int getSurveycriteriaid() {
        return surveycriteriaid;
    }

    public void setSurveycriteriaid(int surveycriteriaid) {
        this.surveycriteriaid = surveycriteriaid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}