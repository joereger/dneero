package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.session.AuthControlled;
import com.dneero.session.Authorization;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Supportissue extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

    public static int STATUS_OPEN = 0;
    public static int STATUS_DNEEROWORKING = 1;
    public static int STATUS_CLOSED = 2;


    // Fields
     private int supportissueid;
     private int userid;
     private Date datetime;
     private int status;
     private String subject;
     private Set<Supportissuecomm> supportissuecomms = new HashSet<Supportissuecomm>();

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Supportissue get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Supportissue");
        try {
            logger.debug("Supportissue.get(" + id + ") called.");
            Supportissue obj = (Supportissue) HibernateUtil.getSession().get(Supportissue.class, id);
            if (obj == null) {
                logger.debug("Supportissue.get(" + id + ") returning new instance because hibernate returned null.");
                return new Supportissue();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Supportissue", ex);
            return new Supportissue();
        }
    }

    // Constructors

    /** default constructor */
    public Supportissue() {
    }


    public boolean canRead(User user){
        if (user.getUserid()==userid || Authorization.isUserSysadmin(user)){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    // Property accessors

    public int getSupportissueid() {
        return supportissueid;
    }

    public void setSupportissueid(int supportissueid) {
        this.supportissueid = supportissueid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Supportissuecomm> getSupportissuecomms() {
        return supportissuecomms;
    }

    public void setSupportissuecomms(Set<Supportissuecomm> supportissuecomms) {
        this.supportissuecomms = supportissuecomms;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
