package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

/**
 * Survey generated by hbm2java
 */

public class Surveycriteriaeducationlevel extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields

     private int surveycriteriaeducationlevelid;
     private int surveycriteriaid;
     //Value of ethnicity drawn from com.dneero.Blogger
     private int educationlevel;




    //Validator
    public void validateRegerEntity() throws GeneralException {
        
    }

    //Loader
    public void load(){

    }

    public static Surveycriteriaeducationlevel get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Surveycriteriaeducationlevel");
        try {
            logger.debug("Surveycriteriaeducationlevel.get(" + id + ") called.");
            Surveycriteriaeducationlevel obj = (Surveycriteriaeducationlevel) HibernateUtil.getSession().get(Surveycriteriaeducationlevel.class, id);
            if (obj == null) {
                logger.debug("Surveycriteriaeducationlevel.get(" + id + ") returning new instance because hibernate returned null.");
                return new Surveycriteriaeducationlevel();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Surveycriteriaeducationlevel", ex);
            return new Surveycriteriaeducationlevel();
        }
    }

    // Constructors

    /** default constructor */
    public Surveycriteriaeducationlevel() {
    }




    // Property accessors

    public int getSurveycriteriaeducationlevelid() {
        return surveycriteriaeducationlevelid;
    }

    public void setSurveycriteriaeducationlevelid(int surveycriteriaeducationlevelid) {
        this.surveycriteriaeducationlevelid = surveycriteriaeducationlevelid;
    }

    public int getSurveycriteriaid() {
        return surveycriteriaid;
    }

    public void setSurveycriteriaid(int surveycriteriaid) {
        this.surveycriteriaid = surveycriteriaid;
    }

    public int getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(int educationlevel) {
        this.educationlevel = educationlevel;
    }


}