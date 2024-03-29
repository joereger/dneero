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

public class Surveyincentiveoption extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int surveyincentiveoptionid;
    private int surveyincentiveid;
    private String name;
    private String value;


    public static Surveyincentiveoption get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Surveyincentiveoption");
        try {
            logger.debug("Surveyincentiveoption.get(" + id + ") called.");
            Surveyincentiveoption obj = (Surveyincentiveoption) HibernateUtil.getSession().get(Surveyincentiveoption.class, id);
            if (obj == null) {
                logger.debug("Surveyincentiveoption.get(" + id + ") returning new instance because hibernate returned null.");
                return new Surveyincentiveoption();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Surveyincentiveoption", ex);
            return new Surveyincentiveoption();
        }
    }


    // Constructors

    /** default constructor */
    public Surveyincentiveoption() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getSurveyincentiveoptionid() {
        return surveyincentiveoptionid;
    }

    public void setSurveyincentiveoptionid(int surveyincentiveoptionid) {
        this.surveyincentiveoptionid=surveyincentiveoptionid;
    }

    public int getSurveyincentiveid() {
        return surveyincentiveid;
    }

    public void setSurveyincentiveid(int surveyincentiveid) {
        this.surveyincentiveid=surveyincentiveid;
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