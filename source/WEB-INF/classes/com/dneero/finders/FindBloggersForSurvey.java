package com.dneero.finders;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Disjunction;

import java.util.*;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.Util;
import com.dneero.util.DateDiff;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.constants.Dneerousagemethods;
import com.dneero.privatelabel.PlPeers;

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
        Pl plOfSurvey = Pl.get(survey.getPlid());
        //Get the surveycriteria in xml form
        //Goal here is to get a subset of all users, not to be hyper-specific
        //If I can do more with a single HQL query, I should... but I need to be careful to not knock out any possible qualifying bloggers
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
        //Country
        crit.add( Property.forName("country").in( scXml.getCountry() ) );
        //Profession
        crit.add( Property.forName("profession").in( scXml.getProfession() ) );
        //Politics
        crit.add( Property.forName("politics").in( scXml.getPolitics() ) );
        //Birthdate
        crit.add(Restrictions.ge("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime() ));
        crit.add(Restrictions.le("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime() ));
        //Run the query to get the initial list of bloggers
        bloggers = crit.list();
        //Iterate all bloggers, removing those that specifically don't qualify
        for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = (Blogger) iterator.next();
            User user = User.get(blogger.getUserid());
            Pl plOfUser = Pl.get(user.getPlid());
            if (PlPeers.isThereATwoWayTrustRelationship(plOfSurvey, plOfUser)){
                if (!scXml.isUserQualified(user)){
                    iterator.remove();
                    continue;
                }
            }
        }
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
