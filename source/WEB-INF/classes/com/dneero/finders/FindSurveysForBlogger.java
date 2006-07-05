package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Surveycriteria;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

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
        this.blogger = blogger;

        //Create the criteria
        Criteria crit = HibernateUtil.getSession().createCriteria(Surveycriteria.class);

        //Birthdate
        //@todo convert birthdate to agemin and agemax
        crit.add(Restrictions.between("agemin", 0, 100));
        crit.add(Restrictions.between("agemax", 0, 100));
        //Gender
        crit.createCriteria("surveycriteriagender")
            .add(Restrictions.eq("gender", blogger.getGender()));
        //Ethnicity
        crit.createCriteria("surveycriteriaethnicity")
            .add(Restrictions.eq("ethnicity", blogger.getEthnicity()));
        //Marital Status
        crit.createCriteria("surveycriteriamaritalstatus")
            .add(Restrictions.eq("maritalstatus", blogger.getMaritalstatus()));
        //Income Range
        crit.createCriteria("surveycriteriaincomerange")
            .add(Restrictions.eq("incomerange", blogger.getIncomerange()));
        //Education Level
        crit.createCriteria("surveycriteriaeducationlevel")
            .add(Restrictions.eq("educationlevel", blogger.getEducationlevel()));
        //State
        crit.createCriteria("surveycriteriastate")
            .add(Restrictions.eq("state", blogger.getState()));
        //City
        crit.createCriteria("surveycriteriacity")
            .add(Restrictions.eq("city", blogger.getCity()));
        //Profession
        crit.createCriteria("surveycriteriaprofession")
            .add(Restrictions.eq("profession", blogger.getProfession()));
        //Politics
        crit.createCriteria("surveycriteriapolitics")
            .add(Restrictions.eq("politics", blogger.getPolitics()));
        //@todo add blog.blogfocus criteria


        //Run the query
        List<Surveycriteria> surveycriteria = crit.list();
        logger.debug("surveycriteria.size()=" + surveycriteria.size());
        //Iterate results
        for (Iterator it = surveycriteria.iterator(); it.hasNext(); ) {
            Surveycriteria oc = (Surveycriteria)it.next();
            surveys.add(Survey.get(oc.getSurveyid()));
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
