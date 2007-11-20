package com.dneero.htmluibeans;


import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.TreeMap;


/**
 * User: Joe Reger Jr
 * Date: Mar 27, 2007
 * Time: 1:56:56 PM
 */
public class StaticVariables {


    public TreeMap<String, String> getPercentiles(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("Top 1%", String.valueOf(1));
        out.put("Top 5%", String.valueOf(5));
        out.put("Top 10%", String.valueOf(10));
        out.put("Top 25%", String.valueOf(25));
        out.put("Top 50%", String.valueOf(50));
        out.put("Everybody", String.valueOf(100));
        return out;
    }

    public TreeMap<String, String> getBlogqualities(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("0", String.valueOf(0));
        out.put("1", String.valueOf(1));
        out.put("2", String.valueOf(2));
        out.put("3", String.valueOf(3));
        out.put("4", String.valueOf(4));
        out.put("5", String.valueOf(5));
        out.put("6", String.valueOf(6));
        out.put("7", String.valueOf(7));
        out.put("8", String.valueOf(8));
        out.put("9", String.valueOf(9));
        out.put("10", String.valueOf(10));
        return out;
    }


}
