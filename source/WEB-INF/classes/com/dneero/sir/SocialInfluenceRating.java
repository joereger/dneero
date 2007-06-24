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
    At the blogger level we simply sum the SIR of all of their blogs
     */
    public static double calculateSocialInfluenceRating(Blogger blogger){
        double out = 0;
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            out = out + calculateSocialInfluenceRating(blog);
        }
        return out;
    }

    public static double calculateSocialInfluenceRating90days(Blogger blogger){
        double out = 0;
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            out = out + calculateSocialInfluenceRating90days(blog);
        }
        return out;
    }


    /*
    At the blog level we look at impressions, referrals and eventually impact of a person's answers to a survey
     */
    public static double calculateSocialInfluenceRating(Blog blog){
        int impressionscore = getImpressions(blog);
        int referralsscore = getSurveyReferralsFromBlog(blog);
        //@todo Incorporate survey scoring influence
        double finalscore = impressionscore + (referralsscore*referralsscoremultiplier);
        return finalscore;
    }

    public static double calculateSocialInfluenceRating90days(Blog blog){
        int impressionscore = getImpressions90days(blog);
        int referralsscore = getSurveyReferralsFromBlog90days(blog);
        //@todo Incorporate survey scoring influence
        double finalscore = impressionscore + (referralsscore*referralsscoremultiplier);
        return finalscore;
    }

    /*
    Impressions are a simple one-point-per
     */
    private static int getImpressions(Blog blog){
        int out = 0;
        for (Iterator<Impression> iterator = blog.getImpressions().iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            int impressiondetailscount = ((Long)HibernateUtil.getSession().createQuery("count(*) from Impressiondetail where impressionid='"+impression.getImpressionid()+"'").uniqueResult()).intValue();
            out = out + impressiondetailscount;
        }
        return out;
    }

    private static int getImpressions90days(Blog blog){
        int out = 0;
        Calendar date90daysago = Time.xDaysAgoStart(Calendar.getInstance(), 90);
        for (Iterator<Impression> iterator = blog.getImpressions().iterator(); iterator.hasNext();) {
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
    private static int getSurveyReferralsFromBlog(Blog blog){
        int out = 0;
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("referredbyblogid", blog.getBlogid()))
                                           .setCacheable(true)
                                           .list();
        if (responses!=null){
            out = responses.size();
        }
        return out;
    }

    private static int getSurveyReferralsFromBlog90days(Blog blog){
        int out = 0;
        Calendar date90daysago = Time.xDaysAgoStart(Calendar.getInstance(), 90);
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("referredbyblogid", blog.getBlogid()))
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
