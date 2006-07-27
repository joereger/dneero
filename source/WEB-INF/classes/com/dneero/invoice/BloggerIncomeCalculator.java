package com.dneero.invoice;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;

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

    public static double getBloggerTotalPossibleIncomeForSurvey(Blogger blogger, Survey survey){
        double out = 0;
        HibernateUtil.getSession().saveOrUpdate(blogger);
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            List<Impression> impressions = new ArrayList();
            if (survey!=null){
                impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                           .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                           .add( Restrictions.eq("blogid", blog.getBlogid()))
                           .list();
            } else {
                impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                           .add( Restrictions.eq("blogid", blog.getBlogid()))
                           .list();
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

    public static double getBloggerTotalPossibleEarningsAllTime(Blogger blogger){
        return getBloggerTotalPossibleIncomeForSurvey(blogger, null);
    }

}
