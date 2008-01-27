package com.dneero.finders;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Property;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.Util;
import com.dneero.util.DateDiff;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.constants.Dneerousagemethods;

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
        bloggers = getBloggersForParticularCriteria(scXml);
        logger.debug("bloggers.size() before panels=" + bloggers.size());
        //Incorporate panels
        for (Iterator<Surveypanel> iterator = survey.getSurveypanels().iterator(); iterator.hasNext();) {
            Surveypanel surveypanel = iterator.next();
            Panel panel = Panel.get(surveypanel.getPanelid());
            for (Iterator<Panelmembership> iterator1 = panel.getPanelmemberships().iterator(); iterator1.hasNext();) {
                Panelmembership panelmembership = iterator1.next();
                Blogger blogger = Blogger.get(panelmembership.getBloggerid());
                boolean alreadyInList = false;
                for (Iterator<Blogger> iterator2 = bloggers.iterator(); iterator2.hasNext();) {
                    Blogger blogger1 = iterator2.next();
                    if (blogger.getBloggerid()==blogger1.getBloggerid()){
                        alreadyInList = true;
                    }
                }
                if (!alreadyInList){
                    this.bloggers.add(blogger);
                }
            }
        }
        logger.debug("bloggers.size() after panels=" + bloggers.size());
        //Iterate all bloggers, removing some
        for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = (Blogger) iterator.next();
            User user = User.get(blogger.getUserid());
            //dneerousagemethod qualification
            if (user.getFacebookuserid()>0){
                //This is a facebook user
                if(!Util.arrayContains(scXml.getDneerousagemethods(), Dneerousagemethods.FACEBOOKAPPUSERS)){
                    iterator.remove();
                    continue;
                }
            } else {
                //This is a dNeero.com user
                if(!Util.arrayContains(scXml.getDneerousagemethods(), Dneerousagemethods.DNEERODOTCOMUSERS)){
                    iterator.remove();
                    continue;
                }
            }
            //Iterate all responses to collect some data for next few qualifications
            Response mostrecentresponse = null;
            int surveystaken = 0;
            for (Iterator<Response> iterator1 = blogger.getResponses().iterator(); iterator1.hasNext();) {
                Response response = iterator1.next();
                //Fill most recent response
                if (mostrecentresponse==null || mostrecentresponse.getResponsedate().before(response.getResponsedate())){
                    mostrecentresponse = response;
                }
                //Count surveys taken
                surveystaken = surveystaken + 1;
            }
            //Calculate dayssincelastsurvey
            int dayssincelastsurvey = Integer.MAX_VALUE;
            if (mostrecentresponse!=null){
                dayssincelastsurvey = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(mostrecentresponse.getResponsedate()));
                logger.debug("bloggerid="+blogger.getBloggerid()+" dayssincelastsurvey="+dayssincelastsurvey);
            }
            //DaysSinceLastSurvey
            if (scXml.getDayssincelastsurvey()>0 && dayssincelastsurvey<scXml.getDayssincelastsurvey()){
                iterator.remove();
                continue;
            }
            //Total surveys taken of at least
            if (scXml.getTotalsurveystakenatleast()>0 && surveystaken<scXml.getTotalsurveystakenatleast()){
                iterator.remove();
                continue;
            }
            //Total surveys taken of at most
            if (surveystaken>scXml.getTotalsurveystakenatmost()){
                iterator.remove();
                continue;
            }
        }
    }

    public FindBloggersForSurvey(SurveyCriteriaXML scXml){
        bloggers = getBloggersForParticularCriteria(scXml);
        logger.debug("bloggers.size()=" + bloggers.size()+" (this constructor does not incorporate panel membership)");
    }

    private List getBloggersForParticularCriteria(SurveyCriteriaXML scXml){
        List out;
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
        //Social Influence Rating
        int maxranking = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), scXml.getMinsocialinfluencepercentile());
        logger.debug("maxranking="+maxranking+" Double.parseDouble(String.valueOf(maxranking)))="+Double.parseDouble(String.valueOf(maxranking)));
        crit.add(Restrictions.le("socialinfluenceratingranking", maxranking));
        //Social Influence Rating 90 days
        int maxranking90days = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), scXml.getMinsocialinfluencepercentile90days());
        logger.debug("maxranking90days="+maxranking90days+" Double.parseDouble(String.valueOf(maxranking90days)))="+Double.parseDouble(String.valueOf(maxranking90days)));
        crit.add(Restrictions.le("socialinfluenceratingranking90days", maxranking90days));
        //Birthdate
        crit.add(Restrictions.ge("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemax()).getTime() ));
        crit.add(Restrictions.le("birthdate", Time.subtractYear(Calendar.getInstance(), scXml.getAgemin()).getTime() ));

        //Run the query and get the preliminary results
        out = crit.list();

        return out;
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
