package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Politics {

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add("Apathetic");
        ts.add("Democrat");
        ts.add("Green");
        ts.add("Independent");
        ts.add("Libertarian");
        ts.add("Other");
        ts.add("Republican");
        return ts;
    }

}
