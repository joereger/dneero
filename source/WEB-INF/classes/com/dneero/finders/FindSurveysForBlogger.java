package com.dneero.finders;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;
import com.dneero.util.Time;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * User: Joe Reger Jr
 * Date: Jun 27, 2006
 * Time: 7:50:51 AM
 */
public class FindSurveysForBlogger {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private List surveys;
    private Blogger blogger;

    public FindSurveysForBlogger(Blogger blogger){
        logger.debug("start building criteria to FindSurveysForBlogger");

        this.surveys = new ArrayList();
        HibernateUtil.getSession().saveOrUpdate(blogger);
        this.blogger = blogger;



        //The idea here is that I'm not doing an actual XML query.
        //I'm just making sure that all of the terms are in the XML record itself.
        //This reduces the set to a manageable number which I can then parse
        //with jdom and do more granular and absolute checking.  It's
        //not ideal and once MySQL's Xpath support goes live I should consider it.

        //Create the criteria
        Criteria crit = HibernateUtil.getSession().createCriteria(Survey.class);
        //Status
        crit.add(Restrictions.eq("status", Survey.STATUS_OPEN));
        //Gender
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getGender()+"%"));
        //Ethnicity
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getEthnicity()+"%"));
        //Marital Status
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getMaritalstatus()+"%"));
        //Income Range
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getIncomerange()+"%"));
        //Education Level
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getEducationlevel()+"%"));
        //State
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getState()+"%"));
        //City
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getCity()+"%"));
        //Profession
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getProfession()+"%"));
        //Politics
        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getPolitics()+"%"));

        //Run the query and get the preliminary results
        List<Survey> surveys = crit.list();
        logger.debug("initial list from db: surveys.size()=" + surveys.size());
        for (Iterator it = surveys.iterator(); it.hasNext(); ) {
            Survey survey = (Survey)it.next();
            logger.debug("Seeing if surveyid="+survey.getSurveyid()+" works for this blogger.");
            boolean surveyfitsblogger = true;

            SurveyCriteriaXML scXml = new SurveyCriteriaXML(survey.getCriteriaxml());

            if (surveyfitsblogger && !Util.arrayContains(scXml.getGender(), blogger.getGender())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of gender.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getEthnicity(), blogger.getEthnicity())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of ethnicity.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getMaritalstatus(), blogger.getMaritalstatus())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of maritalstatus.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getIncome(), blogger.getIncomerange())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of incomerange.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getEducationlevel(), blogger.getEducationlevel())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of educationlevel.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getState(), blogger.getState())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of state.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getCity(), blogger.getCity())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of city.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getProfession(), blogger.getProfession())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of profession.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getPolitics(), blogger.getPolitics())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of politics.");
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getBlogfocus(), blogger.getBlogfocus())){
                surveyfitsblogger = false;
                logger.debug("survey not included because of blogfocus.");
            }

            //Now check the age requirements
            if (surveyfitsblogger && blogger.getBirthdate().before(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("survey not included because birthdate is before.");
            }
            if (surveyfitsblogger && blogger.getBirthdate().after(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("survey not included because birthdate is after.");
            }

            //Quality
            if (surveyfitsblogger && blogger.getQuality()<scXml.getBlogquality()){
                surveyfitsblogger = false;
                logger.debug("survey not included because of blog quality.");
            }

            //Quality 90 days
            if (surveyfitsblogger && blogger.getQuality90days()<scXml.getBlogquality90days()){
                surveyfitsblogger = false;
                logger.debug("survey not included because of blog quality 90 days.");
            }


            //Social Influence Rating
            if (surveyfitsblogger){
                int maxranking = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), scXml.getMinsocialinfluencepercentile());
                if (blogger.getSocialinfluenceratingranking()>maxranking){
                    surveyfitsblogger = false;
                    logger.debug("survey not included because of socialinfluenceranking.  maxranking="+maxranking+" blogger.getSocialinfluenceratingranking()="+blogger.getSocialinfluenceratingranking());
                }
            }

            //Social Influence Rating 90 days
            if (surveyfitsblogger){
                int maxranking90days = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), scXml.getMinsocialinfluencepercentile90days());
                if (blogger.getSocialinfluenceratingranking90days()>maxranking90days){
                    surveyfitsblogger = false;
                    logger.debug("survey not included because of socialinfluenceranking90days.  maxranking90days="+maxranking90days+" blogger.getSocialinfluenceratingranking90days()="+blogger.getSocialinfluenceratingranking90days());
                }
            }
   

            //If it hasn't been booted by now, keep it
            if (surveyfitsblogger){
                this.surveys.add(survey);
            }
        }

        //Add surveys that blogger is on panel for
        for (Iterator<Panelmembership> iterator = blogger.getPanelmemberships().iterator(); iterator.hasNext();) {
            Panelmembership panelmembership = iterator.next();
            List results = HibernateUtil.getSession().createQuery("from Surveypanel where panelid='"+panelmembership.getPanelid()+"'").list();
            for (Iterator iterator1 = results.iterator(); iterator1.hasNext();) {
                Surveypanel surveypanel = (Surveypanel) iterator1.next();
                Survey survey = Survey.get(surveypanel.getSurveyid());
                if (survey.getStatus()==Survey.STATUS_OPEN){
                    boolean alreadyInList = false;
                    for (Iterator<Survey> iterator2 = surveys.iterator(); iterator2.hasNext();) {
                        Survey survey1 = iterator2.next();
                        if (survey.getSurveyid()==survey1.getSurveyid()){
                            alreadyInList = true;
                        }
                    }
                    if (!alreadyInList){
                        this.surveys.add(survey);
                    }
                }
            }
        }


    }

    public static boolean isBloggerQualifiedToTakeSurvey(Blogger blogger, Survey survey){
        if (blogger==null){
            return false;    
        }
        FindSurveysForBlogger fsfb = new FindSurveysForBlogger(blogger);
        for (Iterator iterator = fsfb.getSurveys().iterator(); iterator.hasNext();) {
            Survey tmpSurvey = (Survey) iterator.next();
            if (tmpSurvey.getSurveyid()==survey.getSurveyid()){
                return true;
            }
        }
        return false;   
    }


    public List getSurveys() {
        return surveys;
    }

    public void setSurveys(List surveys) {
        this.surveys = surveys;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

}
