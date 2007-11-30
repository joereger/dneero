package com.dneero.dao;
// Generated Apr 17, 2006 3:45:24 PM by Hibernate Tools 3.1.0.beta4

import com.dneero.util.GeneralException;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.cache.providers.CacheFactory;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;


/**
 * Survey generated by hbm2java
 */

public class Survey extends BasePersistentClass implements java.io.Serializable, AuthControlled {

    public static int STATUS_DRAFT = 1;
    public static int STATUS_WAITINGFORSTARTDATE = 2;
    public static int STATUS_WAITINGFORFUNDS = 3;
    public static int STATUS_OPEN = 4;
    public static int STATUS_CLOSED = 5;


     // Fields
     private int surveyid;
     private int researcherid;
     private String title;
     private String description;
     private String criteriaxml;
     private double willingtopayperrespondent = 2.5;
     private int numberofrespondentsrequested  = 1000;
     private double willingtopaypercpm = 10;
     private int maxdisplaysperblog = 1000;
     private int maxdisplaystotal = 250000;
     private Date startdate;
     private Date enddate;
     private String template;
     private int status;
     private boolean embedjavascript = true;
     private boolean embedflash = true;
     private boolean embedlink = true;
     private int publicsurveydisplays;
     private boolean isspotlight = false;
     private boolean ischarityonly = false;
     private boolean isresultshidden = false;
     private boolean isaccesscodeonly= false;
     private String accesscode = "";

     private Set<Question> questions = new HashSet<Question>();
     private Set<Response> responses = new HashSet<Response>();
     private Set<Impression> impressions = new HashSet<Impression>();
     private Set<Surveypanel> surveypanels = new HashSet<Surveypanel>();
     private Set<Surveydiscuss> surveydiscusses = new HashSet<Surveydiscuss>();


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

    public void flushSurveyEmbeddingCache(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String cacheGroup = "embeddedsurveycache";
        CacheFactory.getCacheProvider().flush(cacheGroup+"/"+"surveyid-"+surveyid);
    }

    public void save() throws GeneralException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called and is accessing Survey.java");
        //Start experimental
        try{
            Throwable t = new Throwable();
            logger.debug("Survey.save(surveyid="+surveyid+", status="+status+") called by: "+t.getStackTrace()[1]);
            if (t.getStackTrace().length>2){logger.debug("                                 : "+t.getStackTrace()[2]);}
            if (t.getStackTrace().length>3){logger.debug("                                 : "+t.getStackTrace()[3]);}
            if (t.getStackTrace().length>4){logger.debug("                                 : "+t.getStackTrace()[4]);}
            //String err = "Survey.save(surveyid="+surveyid+", status="+status+") called by: "+t.getStackTrace()[1];
            //SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, err);
            //xmpp.send();
        }catch(Exception ex){logger.error("",ex);}
        //try{throw new Exception(" ");}catch( Exception e ){
        //    logger.debug( "Survey.save() called by: " + e.getStackTrace()[1].getClassName() + "." +e.getStackTrace()[1].getMethodName() + "()" );
        //}
        //End Experimental
        flushSurveyEmbeddingCache();
        super.save();
    }

    public void delete() throws HibernateException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("delete() called and is accessing Survey.java");
        flushSurveyEmbeddingCache();
        super.delete();
    }

    public void refresh() throws HibernateException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("refresh() called and is accessing Survey.java");
        flushSurveyEmbeddingCache();
        super.refresh();
    }


    public boolean canRead(User user){
        return true;    
    }

    public boolean canEdit(User user){
        if (user.getUserid()==Researcher.get(researcherid).getUserid()){
            return true;
        }
        return false;
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


    public boolean getEmbedjavascript() {
        return embedjavascript;
    }

    public void setEmbedjavascript(boolean embedjavascript) {
        this.embedjavascript = embedjavascript;
    }

    public boolean getEmbedflash() {
        return embedflash;
    }

    public void setEmbedflash(boolean embedflash) {
        this.embedflash = embedflash;
    }

    public boolean getEmbedlink() {
        return embedlink;
    }

    public void setEmbedlink(boolean embedlink) {
        this.embedlink = embedlink;
    }


    public Set<Surveypanel> getSurveypanels() {
        return surveypanels;
    }

    public void setSurveypanels(Set<Surveypanel> surveypanels) {
        this.surveypanels = surveypanels;
    }


    public int getPublicsurveydisplays() {
        return publicsurveydisplays;
    }

    public void setPublicsurveydisplays(int publicsurveydisplays) {
        this.publicsurveydisplays = publicsurveydisplays;
    }

    public boolean isIsspotlight() {
        return isspotlight;
    }
    public boolean getIsspotlight() {
        return isspotlight;
    }

    public void setIsspotlight(boolean isspotlight) {
        this.isspotlight = isspotlight;
    }


    public Set<Surveydiscuss> getSurveydiscusses() {
        return surveydiscusses;
    }

    public void setSurveydiscusses(Set<Surveydiscuss> surveydiscusses) {
        this.surveydiscusses = surveydiscusses;
    }

    public boolean getIscharityonly() {
        return ischarityonly;
    }

    public void setIscharityonly(boolean ischarityonly) {
        this.ischarityonly = ischarityonly;
    }

    public boolean getIsresultshidden() {
        return isresultshidden;
    }

    public void setIsresultshidden(boolean isresultshidden) {
        this.isresultshidden = isresultshidden;
    }


    public boolean getIsaccesscodeonly() {
        return isaccesscodeonly;
    }

    public void setIsaccesscodeonly(boolean isaccesscodeonly) {
        this.isaccesscodeonly=isaccesscodeonly;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode=accesscode;
    }
}
