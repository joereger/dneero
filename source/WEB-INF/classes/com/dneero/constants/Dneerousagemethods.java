package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Dneerousagemethods {

    public static String DNEERODOTCOMUSERS = "dNeero.com Users";
    public static String FACEBOOKAPPUSERS = "Facebook App Users";

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add(DNEERODOTCOMUSERS);
        ts.add(FACEBOOKAPPUSERS);
        return ts;
    }

}
