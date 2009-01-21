package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Panelmembership extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int panelmembershipid;
     private int panelid;
     private int bloggerid;
     private boolean issysadminreviewed;
     private boolean issysadminrejected;




    public static Panelmembership get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Panelmembership");
        try {
            logger.debug("Panelmembership.get(" + id + ") called.");
            Panelmembership obj = (Panelmembership) HibernateUtil.getSession().get(Panelmembership.class, id);
            if (obj == null) {
                logger.debug("Panelmembership.get(" + id + ") returning new instance because hibernate returned null.");
                return new Panelmembership();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Panelmembership", ex);
            return new Panelmembership();
        }
    }

    // Constructors

    /** default constructor */
    public Panelmembership() {
    }

    public boolean canRead(User user){

        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getPanelmembershipid() {
        return panelmembershipid;
    }

    public void setPanelmembershipid(int panelmembershipid) {
        this.panelmembershipid = panelmembershipid;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public boolean getIssysadminreviewed() {
        return issysadminreviewed;
    }

    public void setIssysadminreviewed(boolean issysadminreviewed) {
        this.issysadminreviewed=issysadminreviewed;
    }

    public boolean getIssysadminrejected() {
        return issysadminrejected;
    }

    public void setIssysadminrejected(boolean issysadminrejected) {
        this.issysadminrejected=issysadminrejected;
    }
}
