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
        logger.debug("surveys.size()=" + surveys.size());
        for (Iterator it = surveys.iterator(); it.hasNext(); ) {
            Survey survey = (Survey)it.next();
            boolean surveyfitsblogger = true;

            SurveyCriteriaXML scXml = new SurveyCriteriaXML(survey.getCriteriaxml());

            if (surveyfitsblogger && !Util.arrayContains(scXml.getGender(), blogger.getGender())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getEthnicity(), blogger.getEthnicity())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getMaritalstatus(), blogger.getMaritalstatus())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getIncome(), blogger.getIncomerange())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getEducationlevel(), blogger.getEducationlevel())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getState(), blogger.getState())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getCity(), blogger.getCity())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getProfession(), blogger.getProfession())){
                surveyfitsblogger = false;
            }
            if (surveyfitsblogger && !Util.arrayContains(scXml.getPolitics(), blogger.getPolitics())){
                surveyfitsblogger = false;
            }

            //Now check the age requirements
            if (blogger.getBirthdate().before(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime()    )){
                surveyfitsblogger = false;
            }
            if (blogger.getBirthdate().after(   Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime()    )){
                surveyfitsblogger = false;
            }

            //Now check this blogger's blogs to see if they fulfill the criteria
            if (surveyfitsblogger){
                boolean atleastoneblogfulfills = false;
                for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
                    Blog blog = iterator.next();
                    if (Util.arrayContains(scXml.getBlogfocus(), blog.getBlogfocus())){
                        if (blog.getQuality()>=scXml.getBlogquality()){
                            if (blog.getQuality90days()>=scXml.getBlogquality90days()){
                                atleastoneblogfulfills = true;
                            }
                        }
                    }
                }
                if (!atleastoneblogfulfills){
                    surveyfitsblogger = false;
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
