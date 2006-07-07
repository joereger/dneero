package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Questionconfig extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields
     private int questionconfigid;
     private int questionid;
     private String name;
     private String value;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Questionconfig get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Questionconfig");
        try {
            logger.debug("Questionconfig.get(" + id + ") called.");
            Questionconfig obj = (Questionconfig) HibernateUtil.getSession().get(Questionconfig.class, id);
            if (obj == null) {
                logger.debug("Questionconfig.get(" + id + ") returning new instance because hibernate returned null.");
                return new Questionconfig();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Questionconfig", ex);
            return new Questionconfig();
        }
    }

    // Constructors

    /** default constructor */
    public Questionconfig() {
    }






    // Property accessors

    public int getQuestionconfigid() {
        return questionconfigid;
    }

    public void setQuestionconfigid(int questionconfigid) {
        this.questionconfigid = questionconfigid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
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