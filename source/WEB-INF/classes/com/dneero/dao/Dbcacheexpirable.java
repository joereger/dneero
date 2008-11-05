package com.dneero.dao;

import com.dneero.dao.hibernate.HibernateUtilDbcache;
import com.dneero.dao.hibernate.BasePersistentClassDbcache;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Blogger generated by hbm2java
 */

public class Dbcacheexpirable extends BasePersistentClassDbcache implements java.io.Serializable, AuthControlled {


     // Fields
     private int dbcacheexpirableid;
     private Date date;
     private Date expirationdate;
     private String grp;
     private String keyname;
     private Object val;

    public static Dbcacheexpirable get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Dbcacheexpirable");
        try {
            logger.debug("Dbcacheexpirable.get(" + id + ") called.");
            Dbcacheexpirable obj = (Dbcacheexpirable) HibernateUtilDbcache.getSession().get(Dbcacheexpirable.class, id);
            if (obj == null) {
                logger.debug("Dbcacheexpirable.get(" + id + ") returning new instance because hibernate returned null.");
                return new Dbcacheexpirable();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Dbcacheexpirable", ex);
            return new Dbcacheexpirable();
        }
    }


    // Constructors

    /** default constructor */
    public Dbcacheexpirable() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getDbcacheexpirableid() {
        return dbcacheexpirableid;
    }

    public void setDbcacheexpirableid(int dbcacheexpirableid) {
        this.dbcacheexpirableid=dbcacheexpirableid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate=expirationdate;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp=grp;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname=keyname;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val=val;
    }
}