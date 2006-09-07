package com.dneero.finders;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

/**
 * User: Joe Reger Jr
 * Date: Sep 7, 2006
 * Time: 10:02:28 AM
 */
public class FindBloggersForSurvey {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private List bloggers;
    private Survey survey;

    public FindBloggersForSurvey(Survey survey){
        this.bloggers = new ArrayList();
        this.survey = survey;

        //Get the surveycriteria in xml form
        SurveyCriteriaXML scXml = new SurveyCriteriaXML(survey.getCriteriaxml());

        //Create the criteria
        Criteria crit = HibernateUtil.getSession().createCriteria(Blogger.class);

        //Gender
        crit.add(Restrictions.eq("gender", scXml.getGender()));
        //Ethnicity
        crit.add(Restrictions.eq("ethnicity", scXml.getEthnicity()));
        //Marital Status
        crit.add(Restrictions.eq("maritalstatus", scXml.getMaritalstatus()));
        //Income Range
        crit.add(Restrictions.eq("incomerange", scXml.getIncome()));
        //Education Level
        crit.add(Restrictions.eq("educationlevel", scXml.getEducationlevel()));
        //State
        crit.add(Restrictions.eq("state", scXml.getState()));
        //City
        crit.add(Restrictions.eq("city", scXml.getCity()));
        //Profession
        crit.add(Restrictions.eq("profession", scXml.getProfession()));
        //Politics
        crit.add(Restrictions.eq("politics", scXml.getPolitics()));

        //Quality
        crit.add(Restrictions.ge("quality", scXml.getBlogquality()));
        //Quality90days
        crit.add(Restrictions.ge("quality90days", scXml.getBlogquality90days()));

        //Birthdate
        crit.add(Restrictions.ge("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime() ));
        crit.add(Restrictions.le("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime() ));


        //Run the query and get the preliminary results
        bloggers = crit.list();
        logger.debug("bloggers.size()=" + bloggers.size());
    }

    public List getBloggers() {
        return bloggers;
    }

    public void setBloggers(List bloggers) {
        this.bloggers = bloggers;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
