package com.dneero.constants;

/**
 * User: Joe Reger Jr
 * Date: Jul 31, 2006
 * Time: 4:22:34 PM
 */
public class MaritalstatusesGet extends GetterBase {

    private static String beanname = "maritalstatuses";

    public static String get(String key){
       return (String)get(key, beanname);
    }

    public static String getKeyByVal(String val){
        return (String)getKeyByVal(val, beanname);
    }

}
