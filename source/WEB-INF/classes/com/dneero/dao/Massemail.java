package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.session.Authorization;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Massemail extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

    public static int STATUS_NEW = 0;
    public static int STATUS_PROCESSING = 1;
    public static int STATUS_COMPLETE = 2;

    // Fields
     private int massemailid;
     private int status;
     private int lastuseridprocessed;
     private Date date;
     private String subject;
     private String txtmessage;
     private String htmlmessage;
     private boolean issenttobloggers;
     private boolean issenttoresearchers;
     private boolean issenttouserswhooptoutofnoncriticalemails;




    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Massemail get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Massemail");
        try {
            logger.debug("Massemail.get(" + id + ") called.");
            Massemail obj = (Massemail) HibernateUtil.getSession().get(Massemail.class, id);
            if (obj == null) {
                logger.debug("Massemail.get(" + id + ") returning new instance because hibernate returned null.");
                return new Massemail();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Massemail", ex);
            return new Massemail();
        }
    }

    // Constructors

    /** default constructor */
    public Massemail() {
    }

    public boolean canRead(User user){
        if (Authorization.isUserSysadmin(user)){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors




    public int getMassemailid() {
        return massemailid;
    }

    public void setMassemailid(int massemailid) {
        this.massemailid = massemailid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLastuseridprocessed() {
        return lastuseridprocessed;
    }

    public void setLastuseridprocessed(int lastuseridprocessed) {
        this.lastuseridprocessed = lastuseridprocessed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTxtmessage() {
        return txtmessage;
    }

    public void setTxtmessage(String txtmessage) {
        this.txtmessage = txtmessage;
    }

    public String getHtmlmessage() {
        return htmlmessage;
    }

    public void setHtmlmessage(String htmlmessage) {
        this.htmlmessage = htmlmessage;
    }

    public boolean getIssenttobloggers() {
        return issenttobloggers;
    }

    public void setIssenttobloggers(boolean issenttobloggers) {
        this.issenttobloggers = issenttobloggers;
    }

    public boolean getIssenttoresearchers() {
        return issenttoresearchers;
    }

    public void setIssenttoresearchers(boolean issenttoresearchers) {
        this.issenttoresearchers = issenttoresearchers;
    }


    public boolean getIssenttouserswhooptoutofnoncriticalemails() {
        return issenttouserswhooptoutofnoncriticalemails;
    }

    public void setIssenttouserswhooptoutofnoncriticalemails(boolean issenttouserswhooptoutofnoncriticalemails) {
        this.issenttouserswhooptoutofnoncriticalemails = issenttouserswhooptoutofnoncriticalemails;
    }
}