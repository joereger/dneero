package com.dneero.dao;
// Generated Apr 17, 2006 3:45:24 PM by Hibernate Tools 3.1.0.beta4

import com.dneero.util.GeneralException;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;


/**
 * Survey generated by hbm2java
 */

public class Survey extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

    public static int STATUS_DRAFT = 1;
    public static int STATUS_WAITINGFORSTARTDATE = 2;
    public static int STATUS_OPEN = 3;
    public static int STATUS_CLOSED = 4;

     // Fields
     private int surveyid;
     private int researcherid;
     private String title;
     private String description;
     private String criteriaxml;
     private double willingtopayperrespondent;
     private int numberofrespondentsrequested;
     private double willingtopaypercpm;
     private int maxdisplaysperblog;
     private int maxdisplaystotal;
     private Date startdate;
     private Date enddate;
     private String template;
     private int status;
     private Set<Question> questions = new HashSet<Question>();
     private Set<Response> responses = new HashSet<Response>();
     private Set<Impression> impressions = new HashSet<Impression>();


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Survey get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Survey");
        try {
            logger.debug("Survey.get(" + id + ") called.");
            Survey obj = (Survey) HibernateUtil.getSession().get(Survey.class, id);
            if (obj == null) {
                logger.debug("Survey.get(" + id + ") returning new instance because hibernate returned null.");
                return new Survey();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Survey", ex);
            return new Survey();
        }
    }

    // Constructors

    /** default constructor */
    public Survey() {
    }


    public boolean canRead(User user){
        if (user.getUserid()==Researcher.get(researcherid).getUserid()){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    /** minimal constructor */
    public Survey(int surveyid, String title, double willingtopayperrespondent, int numberofrespondentsrequested, Date startdate, Date enddate) {
        this.surveyid = surveyid;
        this.title = title;
        this.willingtopayperrespondent = willingtopayperrespondent;
        this.numberofrespondentsrequested = numberofrespondentsrequested;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    /** full constructor */
    public Survey(int surveyid, String title, String description, double willingtopayperrespondent, int numberofrespondentsrequested, Date startdate, Date enddate) {
        this.surveyid = surveyid;
        this.title = title;
        this.description = description;
        this.willingtopayperrespondent = willingtopayperrespondent;
        this.numberofrespondentsrequested = numberofrespondentsrequested;
        this.startdate = startdate;
        this.enddate = enddate;
    }



    // Property accessors

    public int getSurveyid() {
        return this.surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getResearcherid() {
        return researcherid;
    }

    public void setResearcherid(int researcherid) {
        this.researcherid = researcherid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCriteriaxml() {
        return criteriaxml;
    }

    public void setCriteriaxml(String criteriaxml) {
        this.criteriaxml = criteriaxml;
    }

    public double getWillingtopayperrespondent() {
        return this.willingtopayperrespondent;
    }

    public void setWillingtopayperrespondent(double willingtopayperrespondent) {
        this.willingtopayperrespondent = willingtopayperrespondent;
    }

    public int getNumberofrespondentsrequested() {
        return this.numberofrespondentsrequested;
    }

    public void setNumberofrespondentsrequested(int numberofrespondentsrequested) {
        this.numberofrespondentsrequested = numberofrespondentsrequested;
    }

    public Date getStartdate() {
        return this.startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return this.enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public double getWillingtopaypercpm() {
        return willingtopaypercpm;
    }

    public void setWillingtopaypercpm(double willingtopaypercpm) {
        this.willingtopaypercpm = willingtopaypercpm;
    }

    public int getMaxdisplaysperblog() {
        return maxdisplaysperblog;
    }

    public void setMaxdisplaysperblog(int maxdisplaysperblog) {
        this.maxdisplaysperblog = maxdisplaysperblog;
    }

    public int getMaxdisplaystotal() {
        return maxdisplaystotal;
    }

    public void setMaxdisplaystotal(int maxdisplaystotal) {
        this.maxdisplaystotal = maxdisplaystotal;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }

    public Set<Impression> getImpressions() {
        return impressions;
    }

    public void setImpressions(Set<Impression> impressions) {
        this.impressions = impressions;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
}
