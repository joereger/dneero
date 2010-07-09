package com.dneero.startup.dbversion;

import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.finders.SurveyCriteriaXMLOld;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.util.Util;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

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

        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("UPDATE survey SET surveycriteriaxml=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = iterator.next();
            try{
                logger.debug("===========surveyid="+survey.getSurveyid());
                SurveyCriteriaXMLOld scXMLold = new SurveyCriteriaXMLOld(survey.getCriteriaxml());
                SurveyCriteriaXML scXMLnew = new SurveyCriteriaXML("", Pl.get(survey.getPlid()));
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
                //Demographic fields
                if (1==1){
                    String name = "Gender";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getGender()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Ethnicity";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getEthnicity()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Marital Status";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getMaritalstatus()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Income";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getIncome()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Education";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getEducationlevel()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "City";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getCity()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "State";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getState()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Country";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getCountry()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Profession";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getProfession()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Politics";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getPolitics()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                if (1==1){
                    String name = "Social Media Focus";
                    int demographicid = getDemographicid(name, survey.getPlid());
                    if (demographicid>0){
                        scXMLnew.getDemographicsXML().setValues(demographicid, Util.stringArrayToArrayList(scXMLold.getBlogfocus()));
                    } else {logger.debug("surveyid="+survey.getSurveyid()+" plid="+survey.getPlid()+" demographicid<=0 for '"+name+"'");}
                }
                //Extract the new xml and save
                survey.setSurveycriteriaxml(scXMLnew.getSurveyCriteriaAsString());
                survey.save();
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        logger.debug("doPostHibernateUpgrade() finish");
    }

    private int getDemographicid(String name, int plid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", plid))
                                            .add(Restrictions.eq("name", name))
                                           .setCacheable(true)
                                           .list();
            if (demographics!=null && demographics.size()>0){
                for (Iterator<Demographic> dit = demographics.iterator(); dit.hasNext();) {
                    Demographic demographic = dit.next();
                    return demographic.getDemographicid();
                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return 0;
    }





}