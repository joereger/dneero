package com.dneero.review;

import com.dneero.htmluibeans.ResearcherRankPeopleListitem;

import java.util.Comparator;

/**
 * User: Joe Reger Jr
 * Date: Jul 7, 2008
 * Time: 7:14:18 AM
 */
public class ReviewableDateComparator implements Comparator {

    public int compare(Object obj1, Object obj2) throws ClassCastException {
        if (!(obj1 instanceof Reviewable) || !(obj2 instanceof Reviewable)){
            throw new ClassCastException("A Reviewable object expected.");
        }
        Reviewable rrpli1 = (Reviewable)obj1;
        Reviewable rrpli2 = (Reviewable)obj2;
        Long t1 = rrpli1.getDate().getTime();
        Long t2 = rrpli2.getDate().getTime();
        return t2.intValue() - t1.intValue();
    }

}
