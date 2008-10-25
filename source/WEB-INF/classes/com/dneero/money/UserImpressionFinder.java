package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.dao.hibernate.NumFromUniqueResultImpressions;

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
        if (survey==null){
            logger.debug("survey is null");
        } else {
            logger.debug("survey is not null... surveyid="+survey.getSurveyid());
        }
        //@todo is this saveOrUpdate() really necessary?
        HibernateUtil.getSession().saveOrUpdate(blogger);
        ArrayList<Impression> out = new ArrayList();
        if (survey!=null && survey.getSurveyid()>0){
            List<Impression> impressionsfromdb = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("userid", blogger.getUserid()))
                                               .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                               .setCacheable(true)
                                               .list();
            if (impressionsfromdb!=null){
                logger.debug("impressionsfromdb.size()="+impressionsfromdb.size());
            } else {
                logger.debug("impressionsfromdb is null");
            }
            out.addAll((ArrayList)impressionsfromdb);
        } else {
            List<Impression> impressionsfromdb = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("userid", blogger.getUserid()))
                                               .setCacheable(true)
                                               .list();
            if (impressionsfromdb!=null){
                logger.debug("impressionsfromdb.size()="+impressionsfromdb.size());
            } else {
                logger.debug("impressionsfromdb is null");
            }
            out.addAll((ArrayList)impressionsfromdb);
        }
        return out;
    }

    public static int getTotalImpressionsForSurvey(Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(UserImpressionFinder.class);
        logger.debug("getTotalImpressionsForSurvey called for bloggerid="+blogger.getBloggerid());
        //@todo is this saveOrUpdate() really necessary?
        //HibernateUtil.getSession().saveOrUpdate(blogger);
        if (survey!=null && survey.getSurveyid()>0){
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionstotal) from Impression where userid='"+blogger.getUserid()+"' and surveyid='"+survey.getSurveyid()+"'");
            return totalimpressions;
        } else {
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionstotal) from Impression where userid='"+blogger.getUserid()+"'");
            return totalimpressions;
        }
    }

    public static int getTotalImpressionsPaidForSurvey(Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(UserImpressionFinder.class);
        logger.debug("getTotalImpressionsForSurvey called for bloggerid="+blogger.getBloggerid());
        //@todo is this saveOrUpdate() really necessary?
        //HibernateUtil.getSession().saveOrUpdate(blogger);
        if (survey!=null && survey.getSurveyid()>0){
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionspaid) from Impression where userid='"+blogger.getUserid()+"' and surveyid='"+survey.getSurveyid()+"'");
            return totalimpressions;
        } else {
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionspaid) from Impression where userid='"+blogger.getUserid()+"'");
            return totalimpressions;
        }
    }

    public static int getTotalImpressionsToBePaidForSurvey(Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(UserImpressionFinder.class);
        logger.debug("getTotalImpressionsForSurvey called for bloggerid="+blogger.getBloggerid());
        //@todo is this saveOrUpdate() really necessary?
        //HibernateUtil.getSession().saveOrUpdate(blogger);
        if (survey!=null && survey.getSurveyid()>0){
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionstobepaid) from Impression where userid='"+blogger.getUserid()+"' and surveyid='"+survey.getSurveyid()+"'");
            return totalimpressions;
        } else {
            int totalimpressions = NumFromUniqueResultImpressions.getInt("select sum(impressionstobepaid) from Impression where userid='"+blogger.getUserid()+"'");
            return totalimpressions;
        }
    }


    public static int getTotalImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getTotalImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getTotalImpressions(Blogger blogger, Survey survey){
        return getTotalImpressionsForSurvey(blogger, survey);
    }

    public static int getPaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getPaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getPaidImpressions(Blogger blogger, Survey survey){
        return getTotalImpressionsPaidForSurvey(blogger, survey);
    }

    public static int getToBePaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getToBePaidImpressions(Blogger blogger, Survey survey){
        return getTotalImpressionsToBePaidForSurvey(blogger, survey);
    }

    public static int getPaidAndToBePaidImpressions(User user, Survey survey){
        if (user!=null && user.getBloggerid()>0){
            return getPaidAndToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
        }
        return 0;
    }

    public static int getPaidAndToBePaidImpressions(Blogger blogger, Survey survey){
        int paid = getTotalImpressionsPaidForSurvey(blogger, survey);
        int tobepaid = getTotalImpressionsToBePaidForSurvey(blogger, survey);
        return paid + tobepaid;
    }






}
