package com.dneero.util;

/**
 * User: Joe Reger Jr
 * Date: Apr 14, 2006
 * Time: 3:20:35 PM
 */
public class Str {

    public static String[] addToStringArray(String[] src, String str){
        if (src==null){
            src=new String[0];
        }

        String[] outArr = new String[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }


}
