package com.dneero.formbeans;


import java.util.LinkedHashMap;


/**
 * User: Joe Reger Jr
 * Date: Mar 27, 2007
 * Time: 1:56:56 PM
 */
public class StaticVariables {


    public LinkedHashMap getPercentiles(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Top 1%", 1);
        out.put("Top 5%", 5);
        out.put("Top 10%", 10);
        out.put("Top 25%", 25);
        out.put("Top 50%", 50);
        out.put("All Bloggers", 100);
        return out;
    }

    public LinkedHashMap getBlogqualities(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("0", 0);
        out.put("1", 1);
        out.put("2", 2);
        out.put("3", 3);
        out.put("4", 4);
        out.put("5", 5);
        out.put("6", 6);
        out.put("7", 7);
        out.put("8", 8);
        out.put("9", 9);
        out.put("10", 10);
        return out;
    }


}
