package com.dneero.rank;

import com.dneero.dao.Rankquestion;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 2:03:08 PM
 */
public class NormalizedpointsUtil {

    public static int determineMaxPoints(int questionid, int rankid){
        int maxpoints = 0;
        //Find rankings for this question
        List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                               .add(Restrictions.eq("questionid", questionid))
                               .add(Restrictions.eq("rankid", rankid))
                               .setCacheable(true)
                               .list();
        //Iterate to get max/min point range
        for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
            Rankquestion rankquestion = rankquestionIterator.next();
            if (rankquestion.getPoints()>maxpoints){
                maxpoints = rankquestion.getPoints();
            }
        }
        return maxpoints;
    }

    public static double calculateNormalizedPoints(int points, int maxpoints){
        double normalizedpoints = 0.0;
        if (maxpoints>0){
            double pointsDbl = Double.parseDouble(String.valueOf(points));
            double maxpointsDbl = Double.parseDouble(String.valueOf(maxpoints));
            normalizedpoints = pointsDbl/maxpointsDbl;
        }
        return normalizedpoints;
    }

}
