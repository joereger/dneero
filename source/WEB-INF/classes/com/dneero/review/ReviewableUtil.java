package com.dneero.review;

import com.dneero.dao.Researcher;
import com.dneero.dao.Review;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmluibeans.ResearcherRankPeopleComparatorPoints;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Apr 29, 2008
 * Time: 11:13:51 AM
 */
public class ReviewableUtil {

    public static ArrayList<Reviewable> getPendingForSysadmin(){
        return getPendingForSysadmin(0);
    }

    public static ArrayList<Reviewable> getPendingForSysadmin(int type){
        Logger logger = Logger.getLogger(ReviewableUtil.class);
        //ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //CLEAN UP DELETES
        List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Review> reviewIterator=reviews.iterator(); reviewIterator.hasNext();) {
            Review review=reviewIterator.next();
            Reviewable reviewable = ReviewableFactory.get(review.getId(), review.getType());
            if (reviewable!=null && reviewable.getId()>0){
                //out.add(reviewable);
            } else {
                try{review.delete();}catch(Exception ex){logger.error("", ex);}
            }
        }
        //return out;


        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        for (Iterator<Reviewable> iterator=ReviewableFactory.getAllTypes().iterator(); iterator.hasNext();) {
            Reviewable reviewable=iterator.next();
            if (type==0 || type==reviewable.getType()){
                out.addAll(reviewable.getPendingForSysadmin());
            }
        }
        return out;

    }

    public static ArrayList<Reviewable> getPendingForResearcher(int researcherid){
        return getPendingForResearcher(researcherid, 0);
    }

    public static ArrayList<Reviewable> getPendingForResearcher(int researcherid, int type){
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        for (Iterator<Reviewable> iterator=ReviewableFactory.getAllTypes().iterator(); iterator.hasNext();) {
            Reviewable reviewable=iterator.next();
            if (type==0 || type==reviewable.getType()){
                out.addAll(reviewable.getPendingForResearcher(researcherid));
            }
        }
        return out;
    }

    public static ArrayList<Reviewable> getPendingForResearcherSorted(int researcherid){
        return getPendingForResearcherSorted(researcherid, 0);
    }

    public static ArrayList<Reviewable> getPendingForResearcherSorted(int researcherid, int type){
        ArrayList<Reviewable> out = getPendingForResearcher(researcherid, type);
        Collections.sort(out, new ReviewableDateComparator());
        return out;
    }

    public static ArrayList<Reviewable> getPendingForSysadminSorted(){
        return getPendingForSysadminSorted(0);
    }

    public static ArrayList<Reviewable> getPendingForSysadminSorted(int type){
        ArrayList<Reviewable> out = getPendingForSysadmin(type);
        Collections.sort(out, new ReviewableDateComparator());
        return out;
    }

    public static ArrayList<Reviewable> getRejectedByResearcher(int researcherid){
        Logger logger = Logger.getLogger(ReviewableUtil.class);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        Researcher researcher = Researcher.get(researcherid);
        List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("isresearcherreviewed", true))
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                            .add(Restrictions.eq("useridofresearcher", researcher.getUserid()))
                                            .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Review> reviewIterator=reviews.iterator(); reviewIterator.hasNext();) {
            Review review=reviewIterator.next();
            Reviewable reviewable = ReviewableFactory.get(review.getId(), review.getType());
            if (reviewable!=null && reviewable.getId()>0){
                out.add(reviewable);
            } else {
                try{review.delete();}catch(Exception ex){logger.error("", ex);}
            }
        }
        return out;
    }

    public static ArrayList<Reviewable> getRejectedBySysadmin(){
        Logger logger = Logger.getLogger(ReviewableUtil.class);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Review> reviewIterator=reviews.iterator(); reviewIterator.hasNext();) {
            Review review=reviewIterator.next();
            Reviewable reviewable = ReviewableFactory.get(review.getId(), review.getType());
            if (reviewable!=null && reviewable.getId()>0){
                out.add(reviewable);
            } else {
                try{review.delete();}catch(Exception ex){logger.error("", ex);}
            }
        }
        return out;
    }

}
