package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Panelapplication extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int panelapplicationid;
     private int panelid;
     private int userid;
     private Date applicationdate;
     private boolean issysadminreviewed;
     private boolean issysadminrejected;
     private String application;
     private String sysadmincomments;


    public static Panelapplication get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Panelapplication");
        try {
            logger.debug("Panelapplication.get(" + id + ") called.");
            Panelapplication obj = (Panelapplication) HibernateUtil.getSession().get(Panelapplication.class, id);
            if (obj == null) {
                logger.debug("Panelapplication.get(" + id + ") returning new instance because hibernate returned null.");
                return new Panelapplication();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Panelapplication", ex);
            return new Panelapplication();
        }
    }

    // Constructors

    /** default constructor */
    public Panelapplication() {
    }

    public boolean canRead(User user){
        return canEdit(user);
    }

    public boolean canEdit(User user){
        return true;
    }




    // Property accessors

    public int getPanelapplicationid() {
        return panelapplicationid;
    }

    public void setPanelapplicationid(int panelapplicationid) {
        this.panelapplicationid=panelapplicationid;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid=panelid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public Date getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(Date applicationdate) {
        this.applicationdate=applicationdate;
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

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application=application;
    }

    public String getSysadmincomments() {
        return sysadmincomments;
    }

    public void setSysadmincomments(String sysadmincomments) {
        this.sysadmincomments=sysadmincomments;
    }
}