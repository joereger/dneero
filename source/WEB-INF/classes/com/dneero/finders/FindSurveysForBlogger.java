package com.dneero.finders;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.constants.Dneerousagemethods;

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
        User user = User.get(blogger.getUserid());


        //The idea here is that I'm not doing an actual XML query.
        //I'm just making sure that all of the terms are in the XML record itself.
        //This reduces the set to a manageable number which I can then parse
        //with jdom and do more granular and absolute checking.  It's
        //not ideal and once MySQL's Xpath support goes live I should consider it.

        //Create the criteria
        Criteria crit = HibernateUtil.getSession().createCriteria(Survey.class);
        //Status
        crit.add(Restrictions.eq("status", Survey.STATUS_OPEN));

        //The checks below were an attempt to force less inspection at the app server level.
        //I was mostly worried about 1000's of surveys being live and was looking for a way to manually go through less of them.
        //But the reality is that many fewer are live at any point in time.
        //And this is already checked manually in scXml.isUserQualified() below
//        //Gender
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getGender()+"%"));
//        //Ethnicity
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getEthnicity()+"%"));
//        //Marital Status
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getMaritalstatus()+"%"));
//        //Income Range
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getIncomerange()+"%"));
//        //Education Level
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getEducationlevel()+"%"));
//        //State
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getState()+"%"));
//        //City
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getCity()+"%"));
//        //Country
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getCountry()+"%"));
//        //Profession
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getProfession()+"%"));
//        //Politics
//        crit.add(Restrictions.like("criteriaxml", "%"+blogger.getPolitics()+"%"));

        //Run the query and get the preliminary results
        List<Survey> surveys = crit.setCacheable(true).list();
        logger.debug("initial list from db: surveys.size()=" + surveys.size());
        for (Iterator it = surveys.iterator(); it.hasNext(); ) {
            Survey survey = (Survey)it.next();
            logger.debug("Seeing if surveyid="+survey.getSurveyid()+" works for this blogger.");
            SurveyCriteriaXML scXml = new SurveyCriteriaXML(survey.getCriteriaxml());
            if (scXml.isUserQualified(User.get(blogger.getUserid()))){
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

    public static boolean isUserQualifiedToTakeSurvey(User user, Survey survey){
        if (user==null){
            return false;
        }
        if (user.getBloggerid()==0){
            return false;
        }
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger==null || blogger.getBloggerid()==0){
            return false;
        }
        return isBloggerQualifiedToTakeSurvey(blogger, survey);
    }

    public static boolean isBloggerQualifiedToTakeSurvey(Blogger blogger, Survey survey){
        if (blogger==null){
            return false;
        }
        //Find current surveys
        FindSurveysForBlogger fsfb = new FindSurveysForBlogger(blogger);
        for (Iterator iterator = fsfb.getSurveys().iterator(); iterator.hasNext();) {
            Survey tmpSurvey = (Survey) iterator.next();
            if (tmpSurvey.getSurveyid()==survey.getSurveyid()){
                return true;
            }
        }
        //See if they've already taken it
        for (Iterator<Response> iterator=blogger.getResponses().iterator(); iterator.hasNext();) {
            Response response=iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                return true;
            }
        }
        //Otherwise, return false... sorry pal, not qualified
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
