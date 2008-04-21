package com.dneero.htmluibeans;

import java.util.Comparator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2008
 * Time: 11:40:45 AM
 */
public class ResearcherRankPeopleComparatorPoints implements Comparator {

    public int compare(Object obj1, Object obj2) throws ClassCastException {
        if (!(obj1 instanceof ResearcherRankPeopleListitem) || !(obj2 instanceof ResearcherRankPeopleListitem)){
            throw new ClassCastException("A ResearcherRankPeopleListitem object expected.");
        }
        ResearcherRankPeopleListitem rrpli1 = (ResearcherRankPeopleListitem)obj1;
        ResearcherRankPeopleListitem rrpli2 = (ResearcherRankPeopleListitem)obj2;
        return rrpli2.getPoints() - rrpli1.getPoints();
    }
}
