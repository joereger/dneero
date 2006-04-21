package com.dneero.dao;

import com.dneero.util.GeneralException;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
// Generated Apr 17, 2006 3:45:26 PM by Hibernate Tools 3.1.0.beta4



/**
 * Offerformat generated by hbm2java
 */

public class Offerformat extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields    

     private int offerformatid;
     private int offerid;
     private int formatid;

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Offerformat get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Offerformat");
        try {
            logger.debug("Offerformat.get(" + id + ") called.");
            Offerformat obj = (Offerformat) HibernateUtil.getSession().get(Offerformat.class, id);
            if (obj == null) {
                logger.debug("Offerformat.get(" + id + ") returning new instance because hibernate returned null.");
                return new Offerformat();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Offerformat", ex);
            return new Offerformat();
        }
    }

    // Constructors

    /** default constructor */
    public Offerformat() {
    }

    
    /** full constructor */
    public Offerformat(int offerformatid, int offerid, int formatid) {
        this.offerformatid = offerformatid;
        this.offerid = offerid;
        this.formatid = formatid;
    }
    

   
    // Property accessors

    public int getOfferformatid() {
        return this.offerformatid;
    }
    
    public void setOfferformatid(int offerformatid) {
        this.offerformatid = offerformatid;
    }

    public int getOfferid() {
        return this.offerid;
    }
    
    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public int getFormatid() {
        return this.formatid;
    }
    
    public void setFormatid(int formatid) {
        this.formatid = formatid;
    }
   








}
