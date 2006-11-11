package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Survey;
import com.dneero.dao.Blog;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;
import com.dneero.util.Time;

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

            //Now check the age requirements
            if (blogger.getBirthdate().before(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("survey not included because birthdate is before.");
            }
            if (blogger.getBirthdate().after(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("survey not included because birthdate is after.");
            }

            //Now check this blogger's blogs to see if they fulfill the criteria
            if (surveyfitsblogger){
                logger.debug("so far the survey fits so we're going to check blogfocus and quality.");
                logger.debug("blogger.getBlogs().size()="+blogger.getBlogs().size());
                if (blogger.getBlogs().size()>0){
                    boolean atleastoneblogfulfills = false;
                    for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
                        Blog blog = iterator.next();
                        logger.debug("considering blogid="+blog.getBlogid());
                        if (Util.arrayContains(scXml.getBlogfocus(), blog.getBlogfocus())){
                            logger.debug("passes blogfocus");
                            if (blog.getQuality()>=scXml.getBlogquality()){
                                if (blog.getQuality90days()>=scXml.getBlogquality90days()){
                                    atleastoneblogfulfills = true;
                                } else {
                                    logger.debug("fails blog quality 90 days");
                                }
                            } else {
                                logger.debug("fails blog quality all time");
                            }
                        } else {
                            logger.debug("survey not included because of blogfocus");
                        }
                    }
                    if (!atleastoneblogfulfills){
                        surveyfitsblogger = false;
                    }
                } else {
                    logger.debug("no blogs found for bloggerid="+blogger.getBloggerid());
                }
            }



            //If it hasn't been booted by now, keep it
            if (surveyfitsblogger){
                this.surveys.add(survey);
            }


        }

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
