package com.dneero.review;

import com.dneero.dao.Researcher;
import com.dneero.dao.Review;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmluibeans.ResearcherRankPeopleComparatorPoints;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Review> reviewIterator=reviews.iterator(); reviewIterator.hasNext();) {
            Review review=reviewIterator.next();
            Reviewable reviewable = ReviewableFactory.get(review.getId(), review.getType());
            out.add(reviewable);
        }
        return out;
    }

    public static ArrayList<Reviewable> getPendingForResearcher(int researcherid){
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        for (Iterator<Reviewable> iterator=ReviewableFactory.getAllTypes().iterator(); iterator.hasNext();) {
            Reviewable reviewable=iterator.next();
            out.addAll(reviewable.getPendingForResearcher(researcherid));
        }
        return out;
    }

    public static ArrayList<Reviewable> getPendingForResearcherSorted(int researcherid){
        ArrayList<Reviewable> out = getPendingForResearcher(researcherid);
        Collections.sort(out, new ReviewableDateComparator());
        return out;
    }

    public static ArrayList<Reviewable> getRejectedByResearcher(int researcherid){
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
            out.add(reviewable);
        }
        return out;
    }

    public static ArrayList<Reviewable> getRejectedBySysadmin(){
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Review> reviewIterator=reviews.iterator(); reviewIterator.hasNext();) {
            Review review=reviewIterator.next();
            Reviewable reviewable = ReviewableFactory.get(review.getId(), review.getType());
            out.add(reviewable);
        }
        return out;
    }

}
