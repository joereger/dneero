package com.dneero.util;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import javax.servlet.http.Cookie;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 7:50:21 AM
 */
public class Util {

    public static List setToArrayList(Set set){
        List out = new ArrayList();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            out.add(o);
        }
        return out;

    }



    public static void logStackTrace(int levelsdeep){
        Logger logger = Logger.getLogger(Util.class.getName());
        logger.debug("logStackTrace("+levelsdeep+") begin");
        StackTraceElement[] elements = new Exception().getStackTrace();
        for (int i=0; i<elements.length; i++) {
            StackTraceElement element=elements[i];
            if (i<levelsdeep){
                logger.debug("class="+element.getClassName()+"."+element.getMethodName()+":"+element.getLineNumber());
            }
        }
        logger.debug("logStackTrace("+levelsdeep+") end");
    }

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

    public static ArrayList stringArrayToArrayList(String[] src){
        ArrayList<String> out = new ArrayList<String>();
        if (src!=null){
            for(int i=0; i < src.length; i++) {
                out.add(src[i]);
            }
        }
        return out;
    }

    public static String stringArrayAsString(String[] array){
        StringBuffer out = new StringBuffer();
        if (array!=null){
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                out.append(s);
                if (i<array.length){
                    out.append(", ");
                }
            }
        }
        return out.toString();
    }

    public static boolean stringArrayContainsInteger(String[] array){
        if (array!=null){
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                if (Num.isinteger(s)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean arrayContains(String[] array, String value){
        if (array!=null){
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                if (s.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] appendToEndOfStringArray(String[] in, String[] append){
        int outlength = 0;
        if (in!=null){
            outlength = outlength + in.length;
        }
        if (append!=null){
            outlength = outlength + append.length;
        }
        String[] out = new String[outlength];
        int outindex = 0;
        if (in!=null){
            for (int i = 0; i < in.length; i++) {
                out[outindex]=in[i];
                outindex = outindex + 1;
            }
        }
        if (append!=null){
            for (int i = 0; i < append.length; i++) {
                out[outindex]=append[i];
                outindex = outindex + 1;
            }
        }
        return out;
    }

    public static String[] appendToEndOfStringArray(String[] in, ArrayList<String> append){
        Logger logger = Logger.getLogger(Util.class);
        int outlength = 0;
        if (in!=null){
            outlength = outlength + in.length;
        }
        if (append!=null){
            outlength = outlength + append.size();
        }
        String[] out = new String[outlength];
        int outindex = 0;
        if (in!=null){
            for (int i = 0; i < in.length; i++) {
                out[outindex]=in[i];
                outindex = outindex + 1;
            }
        }
        if (append!=null){
            for (Iterator<String> iterator=append.iterator(); iterator.hasNext();) {
                try{
                    String s=iterator.next();
                    out[outindex]=s;
                    outindex = outindex + 1;
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
        return out;
    }


    public static String truncateString(String instring, int maxlength){
        String outstring="";
        try{
            outstring = instring.substring(0,maxlength-1);
        } catch (Exception e) {
            outstring = instring;
        }
        return outstring;
    }

    public static Cookie[] addToCookieArray(Cookie[] src, Cookie str){
        if (src==null){
            src=new Cookie[0];
        }

        Cookie[] outArr = new Cookie[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }

    public static TreeMap treeSetToTreeMap(TreeSet ts){
        TreeMap tm = new TreeMap();
        for (Iterator iterator=ts.iterator(); iterator.hasNext();) {
            Object o=iterator.next();
            tm.put(o,o);
        }
        return tm;   
    }

    public static TreeMap<String, String> stringArrayToTreeMap(ArrayList<String> in){
        TreeMap<String, String> tm = new TreeMap<String, String>();
        if (in!=null){
            for (Iterator it = in.iterator(); it.hasNext(); ) {
                String s = (String)it.next();
                tm.put(s, s);
            }
        }
        return tm;
    }

    public static ArrayList treeSetToArrayList(TreeSet ts){
        ArrayList al = new ArrayList();
        for (Iterator iterator=ts.iterator(); iterator.hasNext();) {
            Object o=iterator.next();
            al.add(o);
        }
        return al;   
    }

    public static String[] arrayListToStringArray(ArrayList<String> in){
        String[] out = new String[0];
        if (in!=null){
            out = new String[in.size()];
            int i = 0;
            for (Iterator it = in.iterator(); it.hasNext(); ) {
                String var = (String)it.next();
                out[i] = var;
                i = i + 1;
            }
        }
        return out;
    }

    public static String jdomXmlDocAsString(Document doc){
        Logger logger = Logger.getLogger(Util.class);
        try {
            XMLOutputter serializer = new XMLOutputter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.output(doc, out);
            return out.toString();
        } catch (Exception ex) {
            logger.debug("",ex);
        }
        return "";
    }


    public static String[] convertToArray(TreeMap tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            Iterator keyValuePairs = tmap.entrySet().iterator();
            for (int i = 0; i < tmap.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                Object key = mapentry.getKey();
                String value = (String)mapentry.getValue();
                out[i] = value;
            }
        }
        return out;
    }

    public static String[] convertToArray(TreeSet<String> tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            int i = 0;
            for (Iterator<String> iterator=tmap.iterator(); iterator.hasNext();) {
                String s=iterator.next();
                out[i] = s;
                i = i + 1;
            }

        }
        return out;
    }

    public static String[] convertToArray(LinkedHashMap tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            Iterator keyValuePairs = tmap.entrySet().iterator();
            for (int i = 0; i < tmap.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                Object key = mapentry.getKey();
                String value = (String)mapentry.getValue();
                out[i] = value;
            }
        }
        return out;
    }
}
