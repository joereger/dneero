package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:08:53 AM
 */
public class BloggerIncomeCalculator {

    

//    public static ArrayList<Impressiondetail> getImpressionDetailsThatQualifyForPay(Blogger blogger, Survey survey){
//        HibernateUtil.getSession().saveOrUpdate(blogger);
//        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
//        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
//            Blog blog = iterator.next();
//            out.addAll(getImpressionDetailsThatQualifyForPay(blog, survey));
//        }
//        return out;
//    }
//
//
//    public static ArrayList<Impressiondetail> getImpressionDetailsThatQualifyForPay(Blog blog, Survey survey){
//        Logger logger = Logger.getLogger(BloggerIncomeCalculator.class);
//        logger.debug("getImpressionDetailsThatQualifyForPay called for blogid="+blog.getBlogid());
//        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
//        List<Impression> impressions = new ArrayList();
//        if (survey!=null){
//            logger.debug("survey!=null surveyid="+survey.getSurveyid());
//            //@todo optimize this using a single hql query... iterating all impressions for a blogger is wasteful
//            List<Impression> imp = new ArrayList<Impression>();
//            for (Iterator<Impression> iterator1 = blog.getImpressions().iterator(); iterator1.hasNext();) {
//                Impression impression = iterator1.next();
//                logger.debug("found an impressionid="+impression.getImpressionid());
//                if (impression.getSurveyid()==survey.getSurveyid()){
//                    imp.add(impression);
//                }
//            }
//            impressions = imp;
//        } else {
//            logger.debug("survey is null");
//            impressions = Util.setToArrayList(blog.getImpressions());
//        }
//        logger.debug("found "+impressions.size()+" impressions");
//
//        for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
//            Impression impression = iterator1.next();
//            logger.debug("processing impressionid="+impression.getImpressionid());
//            Survey sv = Survey.get(impression.getSurveyid());
//            int numberofimpressionsforthisblog = 0;
//            for (Iterator<Impressiondetail> iterator2 = impression.getImpressiondetails().iterator(); iterator2.hasNext();){
//                Impressiondetail impressiondetail = iterator2.next();
//                logger.debug("found an impressiondetailid="+impressiondetail.getImpressiondetailid());
//                if (impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE || impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_PENDING){
//                    numberofimpressionsforthisblog = numberofimpressionsforthisblog + 1;
//                    //@todo how am i limiting the total overall responses per survey that the researcher sets?
//                    if (numberofimpressionsforthisblog <= sv.getMaxdisplaysperblog()){
//                        out.add(impressiondetail);
//                    }
//                }
//            }
//        }
//        return out;
//    }

    public static ArrayList<Impression> getAllImpressionsForSurvey(Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(BloggerIncomeCalculator.class);
        logger.debug("getAllImpressionsForSurvey called for bloggerid="+blogger.getBloggerid());
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
            logger.debug("found "+impressions.size()+" impressions");
            out.addAll(impressions);
        }
        return out;
    }

    public static ArrayList<Impressiondetail> getAllImpressiondetailsForSurveyThatQualifyForPay(Blogger blogger, Survey survey){
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            List impressiondetails = HibernateUtil.getSession().createQuery("from Impressiondetail where impressionid='"+impression.getImpressionid()+"' and qualifiesforpaymentstatus='"+Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE+"'").setCacheable(true).list();
            for (Iterator<Impressiondetail> iterator2 = impressiondetails.iterator(); iterator2.hasNext();) {
                Impressiondetail impressiondetail = iterator2.next();
                if (impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                    out.add(impressiondetail);
                }
            }
        }
        return out;
    }

    public static ArrayList<Impressiondetail> getAllImpressiondetailsForSurvey(Blogger blogger, Survey survey){
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
        ArrayList<Impression> impressions = getAllImpressionsForSurvey(blogger, survey);
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            List impressiondetails = HibernateUtil.getSession().createQuery("from Impressiondetail where impressionid='"+impression.getImpressionid()+"'").setCacheable(true).list();
            out.addAll(impressiondetails);
        }
        return out;
    }


    public static ArrayList<Impressionpaymentgroup> getImpressionpaymentgroupsForASurvey(Blogger blogger, Survey survey){
        //Hashmap used so I can simply put a bunch of duplicate Impressionpaymentgroups in and not worry about dupes
        HashMap<Integer, Impressionpaymentgroup> allimpressionpaymentgroups = new HashMap<Integer, Impressionpaymentgroup>();

        //Get impressiondetails for this survey
        ArrayList<Impressiondetail> impressiondetails = getAllImpressiondetailsForSurvey(blogger, survey);
        for (Iterator<Impressiondetail> iterator1 = impressiondetails.iterator(); iterator1.hasNext();) {
            Impressiondetail impressiondetail = iterator1.next();
            if (impressiondetail.getImpressionpaymentgroupid()>0){
                allimpressionpaymentgroups.put(impressiondetail.getImpressionpaymentgroupid(), Impressionpaymentgroup.get(impressiondetail.getImpressionpaymentgroupid()));
            }
        }

        //Convert HashMap to ArrayList
        ArrayList<Impressionpaymentgroup> out = new ArrayList<Impressionpaymentgroup>();
        Iterator keyValuePairs = allimpressionpaymentgroups.entrySet().iterator();
        for (int i = 0; i < allimpressionpaymentgroups.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            Integer key = (Integer)mapentry.getKey();
            Impressionpaymentgroup value = (Impressionpaymentgroup)mapentry.getValue();
            out.add(value);
        }
        return out;
    }

    public static ArrayList<Impressiondetail> getImpressiondetailsForAnImpressionpaymentgroupid(Impressionpaymentgroup impressionpaymentgroup){
        List impressiondetails = HibernateUtil.getSession().createQuery("FROM Impressiondetail WHERE impressionpaymentgroupid='"+ impressionpaymentgroup.getImpressionpaymentgroupid()+"'").list();
        return (ArrayList)impressiondetails;
    }




}
