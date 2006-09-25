package com.dneero.dao;

import com.dneero.util.GeneralException;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;
// Generated Apr 17, 2006 3:45:22 PM by Hibernate Tools 3.1.0.beta4



/**
 * Marketer generated by hbm2java
 */

public class Researcher extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields    

     private int researcherid;
     private int userid;

     private Set<Invoice> invoices = new HashSet<Invoice>();

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Researcher get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Researcher");
        try {
            logger.debug("Researcher.get(" + id + ") called.");
            Researcher obj = (Researcher) HibernateUtil.getSession().get(Researcher.class, id);
            if (obj == null) {
                logger.debug("Researcher.get(" + id + ") returning new instance because hibernate returned null.");
                return new Researcher();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Researcher", ex);
            return new Researcher();
        }
    }

    // Constructors

    /** default constructor */
    public Researcher() {
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


    
    /** full constructor */
    public Researcher(int researcherid) {
        this.researcherid = researcherid;
    }
    

   
    // Property accessors

    public int getResearcherid() {
        return this.researcherid;
    }
    
    public void setResearcherid(int researcherid) {
        this.researcherid = researcherid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
}
