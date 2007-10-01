package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:08:53 AM
 */
public class UserImpressionFinder {



    public static ArrayList<Impression> getAllImpressionsForSurvey(Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(UserImpressionFinder.class);
        logger.debug("getAllImpressionsForSurvey called for bloggerid="+blogger.getBloggerid());
        HibernateUtil.getSession().saveOrUpdate(blogger);
        ArrayList<Impression> out = new ArrayList();
        if (survey!=null && survey.getSurveyid()>0){
            List<Impression> impressionsfromdb = HibernateUtil.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("userid", blogger.getUserid()))
                                               .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                               .setCacheable(true)
                                               .list();
            out.addAll((ArrayList)impressionsfromdb);
        } else {
            List<Impression> impressionsfromdb = HibernateUtil.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("userid", blogger.getUserid()))
                                               .setCacheable(true)
                                               .list();
            out.addAll((ArrayList)impressionsfromdb);
        }
        return out;
    }


    //@todo can these be sped up with sum() queries like sum(impressionstotal) from impression where userid='X' and surveyid='X'?

    public static int getTotalImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getTotalImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getTotalImpressions(Blogger blogger, Survey survey){
        int out = 0;
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            out = out + impression.getImpressionstotal();
        }
        return out;
    }

    public static int getPaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getPaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getPaidImpressions(Blogger blogger, Survey survey){
        int out = 0;
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            out = out + impression.getImpressionspaid();
        }
        return out;
    }

    public static int getToBePaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getToBePaidImpressions(Blogger blogger, Survey survey){
        int out = 0;
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            out = out + impression.getImpressionstobepaid();
        }
        return out;
    }

    public static int getPaidAndToBePaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getPaidAndToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getPaidAndToBePaidImpressions(Blogger blogger, Survey survey){
        int out = 0;
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            out = out + impression.getImpressionspaid() + impression.getImpressionstobepaid();
        }
        return out;
    }






}
