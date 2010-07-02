package com.dneero.finders;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Jul 1, 2010
 * Time: 9:31:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DemographicsUtil {

    public static ArrayList<String> convert(String values){
        Logger logger = Logger.getLogger(DemographicsUtil.class);
        ArrayList<String> out = new ArrayList<String>();
        if (values!=null){
            String[] split = values.split("\\n");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                out.add(s);
            }
        }
        return out;
    }

    public static String convert(ArrayList<String> values){
        Logger logger = Logger.getLogger(DemographicsUtil.class);
        StringBuffer out = new StringBuffer();
        if (values!=null){
            for (Iterator it = values.iterator(); it.hasNext(); ) {
                String s = (String)it.next();
                out.append(s);
                if (it.hasNext()){
                   out.append("\n");
                }
            }
        }
        return out.toString();
    }

}
