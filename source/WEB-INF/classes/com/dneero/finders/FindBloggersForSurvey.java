package com.dneero.finders;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Property;

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
        crit.add( Property.forName("gender").in( scXml.getGender() ) );
        //Ethnicity
        crit.add( Property.forName("ethnicity").in( scXml.getEthnicity() ) );
        //Marital Status
        crit.add( Property.forName("maritalstatus").in( scXml.getMaritalstatus() ) );
        //Income Range
        crit.add( Property.forName("incomerange").in( scXml.getIncome() ) );
        //Education Level
        crit.add( Property.forName("educationlevel").in( scXml.getEducationlevel() ) );
        //State
        crit.add( Property.forName("state").in( scXml.getState() ) );
        //City
        crit.add( Property.forName("city").in( scXml.getCity() ) );
        //Profession
        crit.add( Property.forName("profession").in( scXml.getProfession() ) );
        //Politics
        crit.add( Property.forName("politics").in( scXml.getPolitics() ) );

        //Quality
        crit.add(Restrictions.ge("quality", Double.parseDouble(String.valueOf(scXml.getBlogquality()))));
        //Quality90days
        crit.add(Restrictions.ge("quality90days", Double.parseDouble(String.valueOf(scXml.getBlogquality90days()))));

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
