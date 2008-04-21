package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Rankuser extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int rankuserid;
    private int rankid;
    private int userid;
    private Date date;
    private int responseid;
    private int points;
    private double normalizedpoints;
    private User user;


    public static Rankuser get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Rankuser");
        try {
            logger.debug("Rankuser.get(" + id + ") called.");
            Rankuser obj = (Rankuser) HibernateUtil.getSession().get(Rankuser.class, id);
            if (obj == null) {
                logger.debug("Rankuser.get(" + id + ") returning new instance because hibernate returned null.");
                return new Rankuser();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Rankuser", ex);
            return new Rankuser();
        }
    }

    // Constructors

    /** default constructor */
    public Rankuser() {
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




    // Property accessors

    public int getRankuserid() {
        return rankuserid;
    }

    public void setRankuserid(int rankuserid) {
        this.rankuserid = rankuserid;
    }

    public int getRankid() {
        return rankid;
    }

    public void setRankid(int rankid) {
        this.rankid = rankid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }



    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getNormalizedpoints() {
        return normalizedpoints;
    }

    public void setNormalizedpoints(double normalizedpoints) {
        this.normalizedpoints = normalizedpoints;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}