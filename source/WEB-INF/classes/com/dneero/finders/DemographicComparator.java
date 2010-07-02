package com.dneero.finders;

import com.dneero.dao.Demographic;

import java.util.Comparator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2008
 * Time: 11:40:45 AM
 */
public class DemographicComparator implements Comparator {

    public int compare(Object obj1, Object obj2) throws ClassCastException {
        if (!(obj1 instanceof Demographic) || !(obj2 instanceof Demographic)){
            throw new ClassCastException("A Demographic object expected.");
        }
        Demographic d1 = (Demographic)obj1;
        Demographic d2 = (Demographic)obj2;

        return d2.getOrdernum() - d1.getOrdernum();
    }
}