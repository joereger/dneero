package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Emailinvitebatchaddress extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int emailinvitebatchaddressid;
     private int emailinvitebatchid;
     private String email;
     private Date sentdate;




    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Emailinvitebatchaddress get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Emailinvitebatchaddress");
        try {
            logger.debug("Emailinvitebatchaddress.get(" + id + ") called.");
            Emailinvitebatchaddress obj = (Emailinvitebatchaddress) HibernateUtil.getSession().get(Emailinvitebatchaddress.class, id);
            if (obj == null) {
                logger.debug("Emailinvitebatchaddress.get(" + id + ") returning new instance because hibernate returned null.");
                return new Emailinvitebatchaddress();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Emailinvitebatchaddress", ex);
            return new Emailinvitebatchaddress();
        }
    }

    // Constructors

    /** default constructor */
    public Emailinvitebatchaddress() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors


    public int getEmailinvitebatchid() {
        return emailinvitebatchid;
    }

    public void setEmailinvitebatchid(int emailinvitebatchid) {
        this.emailinvitebatchid = emailinvitebatchid;
    }

    public int getEmailinvitebatchaddressid() {
        return emailinvitebatchaddressid;
    }

    public void setEmailinvitebatchaddressid(int emailinvitebatchaddressid) {
        this.emailinvitebatchaddressid = emailinvitebatchaddressid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getSentdate() {
        return sentdate;
    }

    public void setSentdate(Date sentdate) {
        this.sentdate = sentdate;
    }
}
