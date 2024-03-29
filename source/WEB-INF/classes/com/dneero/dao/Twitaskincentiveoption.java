package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.scheduledjobs.CurrentBalanceUpdater;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import java.util.Date;

/**
 * Blogger generated by hbm2java
 */

public class Twitaskincentiveoption extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int twitaskincentiveoptionid;
    private int twitaskincentiveid;
    private String name;
    private String value;


    public static Twitaskincentiveoption get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Twitaskincentiveoption");
        try {
            logger.debug("Twitaskincentiveoption.get(" + id + ") called.");
            Twitaskincentiveoption obj = (Twitaskincentiveoption) HibernateUtil.getSession().get(Twitaskincentiveoption.class, id);
            if (obj == null) {
                logger.debug("Twitaskincentiveoption.get(" + id + ") returning new instance because hibernate returned null.");
                return new Twitaskincentiveoption();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Twitaskincentiveoption", ex);
            return new Twitaskincentiveoption();
        }
    }


    // Constructors

    /** default constructor */
    public Twitaskincentiveoption() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getTwitaskincentiveoptionid() {
        return twitaskincentiveoptionid;
    }

    public void setTwitaskincentiveoptionid(int twitaskincentiveoptionid) {
        this.twitaskincentiveoptionid=twitaskincentiveoptionid;
    }

    public int getTwitaskincentiveid() {
        return twitaskincentiveid;
    }

    public void setTwitaskincentiveid(int twitaskincentiveid) {
        this.twitaskincentiveid=twitaskincentiveid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value=value;
    }
}