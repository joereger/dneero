package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Educationlevels {

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add("None");
        ts.add("High School");
        ts.add("Some College");
        ts.add("College Degree");
        ts.add("Advanced Degree");
        return ts;
    }

}
