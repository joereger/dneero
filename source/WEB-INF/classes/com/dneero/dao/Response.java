package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Blogger generated by hbm2java
 */

public class Response extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


    // Fields
     private int responseid;
     private int surveyid;
     private int bloggerid;
     private Date responsedate;
     private Survey survey;


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Response get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Response");
        try {
            logger.debug("Response.get(" + id + ") called.");
            Response obj = (Response) HibernateUtil.getSession().get(Response.class, id);
            if (obj == null) {
                logger.debug("Response.get(" + id + ") returning new instance because hibernate returned null.");
                return new Response();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Response", ex);
            return new Response();
        }
    }

    // Constructors

    /** default constructor */
    public Response() {
    }


    public boolean canRead(User user){
        Survey survey = Survey.get(surveyid);
        if (user.getUserid()==Researcher.get(survey.getResearcherid()).getUserid()){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    // Property accessors

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }




}
