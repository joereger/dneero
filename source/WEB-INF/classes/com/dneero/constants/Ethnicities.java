package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Ethnicities {

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add("Not Specified");
        ts.add("White, Not Hispanic");
        ts.add("Black, Not Hispanic");
        ts.add("Hispanic");
        ts.add("Asian");
        ts.add("American Indian");
        return ts;
    }

}
