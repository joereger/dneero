package com.dneero.invoice;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:08:53 AM
 */
public class BloggerIncomeCalculator {

    public static double getBloggerTotalPossibleEarningsAllTime(Blogger blogger){
        return getBloggerTotalPossibleIncomeForSurvey(blogger, null);
    }

    public static double getBloggerTotalPossibleIncomeForSurvey(Blogger blogger, Survey survey){
        double out = 0;
        HibernateUtil.getSession().saveOrUpdate(blogger);
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            List<Impression> impressions = new ArrayList();
            if (survey!=null){
                //@todo optimize this using a single hql query... iterating all impressions for a blogger is wasteful
                List<Impression> imp = new ArrayList<Impression>();
                for (Iterator<Impression> iterator1 = blog.getImpressions().iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    if (impression.getSurveyid()==survey.getSurveyid()){
                        imp.add(impression);
                    }
                }
                impressions = imp;
            } else {
                impressions = Util.setToArrayList(blog.getImpressions());
            }

            for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                Impression impression = iterator1.next();
                Survey sv = Survey.get(impression.getSurveyid());
                int impressionsthatqualify = impression.getTotalimpressions();
                if (impressionsthatqualify > sv.getMaxdisplaysperblog()){
                    impressionsthatqualify = sv.getMaxdisplaysperblog();
                }
                double earnedonthisimpression = sv.getWillingtopayperrespondent()+(sv.getWillingtopaypercpm()/1000);
                out = out + earnedonthisimpression;
            }
        }

        return out;
    }

    public static double getAllEarningsPaidToBlogger(){
        double amt = 0;
        List<Payblogger> paybloggers = HibernateUtil.getSession().createCriteria(Payblogger.class)
                                       .add( Restrictions.eq("status", Payblogger.STATUS_PAID))
                                       .list();

        for (Iterator<Payblogger> iterator = paybloggers.iterator(); iterator.hasNext();) {
            Payblogger payblogger = iterator.next();
            amt = amt + payblogger.getAmt();
        }
        return amt;
    }



    public static ArrayList<Payblogger> getPaybloggersForResponse(Response response){
        ArrayList<Payblogger> out = new ArrayList<Payblogger>();
        if(response.getPaybloggerid()>0){
            Payblogger payblogger = Payblogger.get(response.getPaybloggerid());
            out.add(payblogger);
        }
        return out;
    }

    public static int getQualifyingImpressionsByABlogger(Blogger blogger, Survey survey){
        int out = 0;
        HibernateUtil.getSession().saveOrUpdate(blogger);
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            List<Impression> impressions = new ArrayList<Impression>();
            //@todo optimize this using a single hql query... iterating all impressions for a blogger is wasteful
            List<Impression> imp = new ArrayList<Impression>();
            for (Iterator<Impression> iterator1 = blog.getImpressions().iterator(); iterator1.hasNext();) {
                Impression impression = iterator1.next();
                if (impression.getSurveyid()==survey.getSurveyid()){
                    imp.add(impression);
                }
            }
            impressions = imp;

            for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                Impression impression = iterator1.next();
                Survey sv = Survey.get(impression.getSurveyid());
                int impressionsthatqualify = impression.getTotalimpressions();
                if (impressionsthatqualify > sv.getMaxdisplaysperblog()){
                    impressionsthatqualify = sv.getMaxdisplaysperblog();
                }
                out = out + impressionsthatqualify;
            }
        }

        return out;
    }

}
