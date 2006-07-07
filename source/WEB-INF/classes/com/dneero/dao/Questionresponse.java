package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Questionresponse extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields
     private int questionresponseid;
     private int questionid;
     private int bloggerid;
     private String name;
     private String value;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Questionresponse get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Questionresponse");
        try {
            logger.debug("Questionresponse.get(" + id + ") called.");
            Questionresponse obj = (Questionresponse) HibernateUtil.getSession().get(Questionresponse.class, id);
            if (obj == null) {
                logger.debug("Questionresponse.get(" + id + ") returning new instance because hibernate returned null.");
                return new Questionresponse();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Questionresponse", ex);
            return new Questionresponse();
        }
    }

    // Constructors

    /** default constructor */
    public Questionresponse() {
    }






    // Property accessors

    public int getQuestionresponseid() {
        return questionresponseid;
    }

    public void setQuestionresponseid(int questionresponseid) {
        this.questionresponseid = questionresponseid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}