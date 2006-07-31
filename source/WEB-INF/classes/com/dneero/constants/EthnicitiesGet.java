package com.dneero.constants;

import com.dneero.util.Jsf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Jul 31, 2006
 * Time: 4:22:34 PM
 */
public class EthnicitiesGet extends GetterBase {

    private static String beanname = "ethnicities";

    public static String get(String key){
       return (String)get(key, beanname);
    }

    public static String getKeyByVal(String val){
        return (String)getKeyByVal(val, beanname);
    }

}
