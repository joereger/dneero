package com.dneero.invoice;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;

import java.util.*;

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

    public static ArrayList<Impressiondetail> getImpressionDetailsThatQualifyForPay(Blogger blogger, Survey survey){
        HibernateUtil.getSession().saveOrUpdate(blogger);
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            out.addAll(getImpressionDetailsThatQualifyForPay(blog, survey));
        }
        return out;
    }


    public static ArrayList<Impressiondetail> getImpressionDetailsThatQualifyForPay(Blog blog, Survey survey){
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
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
            int numberofimpressionsforthisblog = 0;
            for (Iterator<Impressiondetail> iterator2 = impression.getImpressiondetails().iterator(); iterator2.hasNext();){
                Impressiondetail impressiondetail = iterator2.next();
                numberofimpressionsforthisblog = numberofimpressionsforthisblog + 1;
                //@todo how am i limiting the total overall responses per survey that the client sets?
                if (numberofimpressionsforthisblog <= sv.getMaxdisplaysperblog()){
                    out.add(impressiondetail);
                }
            }
        }
        return out;
    }

    public static ArrayList<Impression> getAllImpressionsForSurvey(Blogger blogger, Survey survey){
        HibernateUtil.getSession().saveOrUpdate(blogger);
        ArrayList<Impression> out = new ArrayList();
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
            out.addAll(impressions);
        }
        return out;
    }

    public static ArrayList<Impressiondetail> getAllImpressiondetailsForSurvey(Blogger blogger, Survey survey){
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            out.addAll(impression.getImpressiondetails());
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



    public static ArrayList<Payblogger> getPaybloggersForASurvey(Blogger blogger, Survey survey){
        //Hashmap used so I can simply put a bunch of duplicate Paybloggers in and not worry about dupes
        HashMap<Integer, Payblogger> allpaybloggers = new HashMap<Integer, Payblogger>();
        for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                //See if the response itself has been paid
                if (response.getPaybloggerid()>0){
                    allpaybloggers.put(response.getPaybloggerid(), Payblogger.get(response.getPaybloggerid()));
                }
                //Now find impressions for this response/survey
                ArrayList<Impressiondetail> impressiondetails = getAllImpressiondetailsForSurvey(blogger, survey);
                for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
                    Impressiondetail impressiondetail = iterator1.next();
                    if (impressiondetail.getPaybloggerid()>0){
                        allpaybloggers.put(impressiondetail.getPaybloggerid(), Payblogger.get(impressiondetail.getPaybloggerid()));
                    }
                }
            }
        }
        //Convert HashMap to ArrayList
        ArrayList<Payblogger> out = new ArrayList<Payblogger>();
        Iterator keyValuePairs = allpaybloggers.entrySet().iterator();
        for (int i = 0; i < allpaybloggers.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            Integer key = (Integer)mapentry.getKey();
            Payblogger value = (Payblogger)mapentry.getValue();
            out.add(value);
        }
        return out;
    }

    public static ArrayList<Response> getResponsesForAPayblogger(Payblogger payblogger){
        List responses = HibernateUtil.getSession().createQuery("FROM Response WHERE paybloggerid='"+payblogger.getPaybloggerid()+"'").list();
        return (ArrayList)responses;
    }

    public static ArrayList<Impressiondetail> getImpressiondetailsForAPayblogger(Payblogger payblogger){
        List impressiondetails = HibernateUtil.getSession().createQuery("FROM Impressiondetail WHERE paybloggerid='"+payblogger.getPaybloggerid()+"'").list();
        return (ArrayList)impressiondetails;
    }

    public static ArrayList<Revshare> getRevsharesForAPayblogger(Payblogger payblogger){
        List revshares = HibernateUtil.getSession().createQuery("FROM Revshare WHERE paybloggerid='"+payblogger.getPaybloggerid()+"'").list();
        return (ArrayList)revshares;
    }



}
