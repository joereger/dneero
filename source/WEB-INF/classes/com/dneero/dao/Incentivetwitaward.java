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

public class Incentivetwitaward extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int incentivetwitawardid;
    private int userid;
    private int twitaskincentiveid;
    private int twitanswerid;
    private boolean isvalid;
    private Date date;
    private String misc1;
    private String misc2;
    private String misc3;
    private String misc4;
    private String misc5;

    public static Incentivetwitaward get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Incentivetwitaward");
        try {
            logger.debug("Incentivetwitaward.get(" + id + ") called.");
            Incentivetwitaward obj = (Incentivetwitaward) HibernateUtil.getSession().get(Incentivetwitaward.class, id);
            if (obj == null) {
                logger.debug("Incentivetwitaward.get(" + id + ") returning new instance because hibernate returned null.");
                return new Incentivetwitaward();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Incentivetwitaward", ex);
            return new Incentivetwitaward();
        }
    }


    // Constructors

    /** default constructor */
    public Incentivetwitaward() {
    }

    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getIncentivetwitawardid() {
        return incentivetwitawardid;
    }

    public void setIncentivetwitawardid(int incentivetwitawardid) {
        this.incentivetwitawardid=incentivetwitawardid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public int getTwitaskincentiveid() {
        return twitaskincentiveid;
    }

    public void setTwitaskincentiveid(int twitaskincentiveid) {
        this.twitaskincentiveid=twitaskincentiveid;
    }

    public int getTwitanswerid() {
        return twitanswerid;
    }

    public void setTwitanswerid(int twitanswerid) {
        this.twitanswerid=twitanswerid;
    }

    public boolean getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid=isvalid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public String getMisc1() {
        return misc1;
    }

    public void setMisc1(String misc1) {
        this.misc1=misc1;
    }

    public String getMisc2() {
        return misc2;
    }

    public void setMisc2(String misc2) {
        this.misc2=misc2;
    }

    public String getMisc3() {
        return misc3;
    }

    public void setMisc3(String misc3) {
        this.misc3=misc3;
    }

    public String getMisc4() {
        return misc4;
    }

    public void setMisc4(String misc4) {
        this.misc4=misc4;
    }

    public String getMisc5() {
        return misc5;
    }

    public void setMisc5(String misc5) {
        this.misc5=misc5;
    }
}