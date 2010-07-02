package com.dneero.startup.dbversion;

import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.db.DbConfig;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.finders.SurveyCriteriaXMLOld;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version87 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            try{
                SurveyCriteriaXMLOld scXMLold = new SurveyCriteriaXMLOld(survey.getCriteriaxml());
                SurveyCriteriaXML scXMLnew = new SurveyCriteriaXML("");
                //Move values from old to new
                scXMLnew.setAgemax(scXMLold.getAgemax());
                scXMLnew.setAgemin(scXMLold.getAgemin());
                scXMLnew.setDayssincelastsurvey(scXMLold.getDayssincelastsurvey());
                scXMLnew.setDneerousagemethods(scXMLold.getDneerousagemethods());
                scXMLnew.setMinsocialinfluencepercentile(scXMLold.getMinsocialinfluencepercentile());
                scXMLnew.setPanelids(scXMLold.getPanelids());
                scXMLnew.setSuperpanelids(scXMLold.getSuperpanelids());
                scXMLnew.setTotalsurveystakenatleast(scXMLold.getTotalsurveystakenatleast());
                scXMLnew.setTotalsurveystakenatmost(scXMLold.getTotalsurveystakenatmost());
                //Extract the new xml and save
                survey.setCriteriaxml(scXMLnew.getSurveyCriteriaAsString());
                survey.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }


        logger.debug("doPostHibernateUpgrade() finish");
    }







}