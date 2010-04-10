package com.dneero.finders;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.privatelabel.PlPeers;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Pl plOfUser = Pl.get(user.getPlid());

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
            Pl plOfSurvey = Pl.get(survey.getPlid());
            logger.debug("Seeing if surveyid="+survey.getSurveyid()+" works for this blogger.");
            if (PlPeers.isThereATwoWayTrustRelationship(plOfSurvey, plOfUser)){
                if (survey.getIsopentoanybody()){
                    this.surveys.add(survey);
                } else {
                    SurveyCriteriaXML scXml = new SurveyCriteriaXML(survey.getCriteriaxml());
                    if (scXml.isUserQualified(User.get(blogger.getUserid()))){
                        this.surveys.add(survey);
                    }
                }
            }
        }
    }

    public static boolean isUserQualifiedToTakeSurvey(User user, Survey survey){
        if (survey.getIsopentoanybody()){
            return true;
        }
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
        if (survey.getIsopentoanybody()){
            return true;
        }
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
