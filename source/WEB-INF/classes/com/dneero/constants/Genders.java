package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Genders {

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add("Female");
        ts.add("Male");
        return ts;
    }

}
