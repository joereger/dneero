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

public class Venue extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int venueid;
    private int bloggerid;
    private String url;
    private String focus;
    private boolean isresearcherreviewed;
    private boolean issysadminreviewed;
    private boolean isresearcherrejected;
    private boolean issysadminrejected;
    private Date lastsysadminreviewdate;
    private boolean isdueforreview;
    private int scorebysysadmin;
    private boolean isactive;


    public static Venue get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Venue");
        try {
            logger.debug("Venue.get(" + id + ") called.");
            Venue obj = (Venue) HibernateUtil.getSession().get(Venue.class, id);
            if (obj == null) {
                logger.debug("Venue.get(" + id + ") returning new instance because hibernate returned null.");
                return new Venue();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Venue", ex);
            return new Venue();
        }
    }



    // Constructors

    /** default constructor */
    public Venue() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getVenueid() {
        return venueid;
    }

    public void setVenueid(int venueid) {
        this.venueid=venueid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid=bloggerid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus=focus;
    }

    public boolean getIsresearcherreviewed() {
        return isresearcherreviewed;
    }

    public void setIsresearcherreviewed(boolean isresearcherreviewed) {
        this.isresearcherreviewed=isresearcherreviewed;
    }

    public boolean getIssysadminreviewed() {
        return issysadminreviewed;
    }

    public void setIssysadminreviewed(boolean issysadminreviewed) {
        this.issysadminreviewed=issysadminreviewed;
    }

    public boolean getIsresearcherrejected() {
        return isresearcherrejected;
    }

    public void setIsresearcherrejected(boolean isresearcherrejected) {
        this.isresearcherrejected=isresearcherrejected;
    }

    public boolean getIssysadminrejected() {
        return issysadminrejected;
    }

    public void setIssysadminrejected(boolean issysadminrejected) {
        this.issysadminrejected=issysadminrejected;
    }

    public Date getLastsysadminreviewdate() {
        return lastsysadminreviewdate;
    }

    public void setLastsysadminreviewdate(Date lastsysadminreviewdate) {
        this.lastsysadminreviewdate=lastsysadminreviewdate;
    }

    public boolean getIsdueforreview() {
        return isdueforreview;
    }

    public void setIsdueforreview(boolean isdueforreview) {
        this.isdueforreview=isdueforreview;
    }

    public int getScorebysysadmin() {
        return scorebysysadmin;
    }

    public void setScorebysysadmin(int scorebysysadmin) {
        this.scorebysysadmin=scorebysysadmin;
    }

    public boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive=isactive;
    }
}