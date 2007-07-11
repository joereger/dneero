package com.dneero.sir;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

import java.util.Iterator;
import java.util.Calendar;
import java.util.List;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Feb 19, 2007
 * Time: 10:25:37 AM
 */
public class SocialInfluenceRating {

    public static int referralsscoremultiplier = 100;

    /*
    At the blog level we look at impressions, referrals and eventually impact of a person's answers to a survey
     */
    public static double calculateSocialInfluenceRating(User user){
        int impressionscore = getImpressions(user);
        int referralsscore = getSurveyReferralsFromUser(user);
        //@todo Incorporate survey scoring influence
        double finalscore = impressionscore + (referralsscore*referralsscoremultiplier);
        return finalscore;
    }

    public static double calculateSocialInfluenceRating90days(User user){
        int impressionscore = getImpressions90days(user);
        int referralsscore = getSurveyReferralsFromUser90days(user);
        //@todo Incorporate survey scoring influence
        double finalscore = impressionscore + (referralsscore*referralsscoremultiplier);
        return finalscore;
    }

    /*
    Impressions are a simple one-point-per
     */
    private static int getImpressions(User user){
        int out = 0;
        List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            int impressiondetailscount = ((Long)HibernateUtil.getSession().createQuery("select count(*) from Impressiondetail where impressionid='"+impression.getImpressionid()+"'").setCacheable(true).uniqueResult()).intValue();
            out = out + impressiondetailscount;
        }
        return out;
    }

    private static int getImpressions90days(User user){
        int out = 0;
        Calendar date90daysago = Time.xDaysAgoStart(Calendar.getInstance(), 90);
        List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            List impressiondetails = HibernateUtil.getSession().createQuery("from Impressiondetail where impressionid='"+impression.getImpressionid()+"'").setCacheable(true).list();
            for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();){
                Impressiondetail impressiondetail = iterator1.next();
                if (impressiondetail.getImpressiondate().after(date90daysago.getTime())){
                    out = out + 1;
                }
            }
        }
        return out;
    }


    /*
    Referrals are a many-point-per
     */
    private static int getSurveyReferralsFromUser(User user){
        int out = 0;
        //@todo use a unique result select count(*) to prevent big datasets from crossing the wire
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("referredbyuserid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        if (responses!=null){
            out = responses.size();
        }
        return out;
    }

    private static int getSurveyReferralsFromUser90days(User user){
        int out = 0;
        Calendar date90daysago = Time.xDaysAgoStart(Calendar.getInstance(), 90);
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("referredbyuserid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        if (responses!=null){
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                if (response.getResponsedate().after(date90daysago.getTime())){
                    out = out + 1;
                }
            }
        }
        return out;
    }

}
