package com.dneero.htmlui;

import com.dneero.util.Num;
import com.dneero.util.Str;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Dropdown {


    //convenience function to allow options to be submitted in a treemap
    public static String getHtml(String name, String value, TreeMap<String, String> options, String styleclass, String style){
        ArrayList<String> optionKeys = new ArrayList<String>();
        ArrayList<String> optionVals = new ArrayList<String>();
        //Put the options into two ArrayLists
        Iterator keyValuePairs = options.entrySet().iterator();
        for (int i = 0; i < options.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String val = (String)mapentry.getValue();
            optionKeys.add(key);
            optionVals.add(val);
        }
        //Now call the main function
        return getHtml(name, value, optionKeys, optionVals, 0, styleclass, style);
    }



    //In options<String, String> the first is the id value... second is displayed in the dropdown
    public static String getHtml(String name, String value, ArrayList<String> optionKeys, ArrayList<String> optionVals, int maxlength, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        if (maxlength<=0){
            maxlength = 35;    
        }

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }

        style="";
        styleclass="";

        out.append("<select name=\""+Str.cleanForHtml(name)+"\" id=\""+name+"\" "+styleclass+" "+style+">");
        out.append("<option value=\"\"></option>");
        for (int i = 0; i < optionKeys.size(); i++){
            String key = (String)optionKeys.get(i);
            String val = (String)optionVals.get(i);
            String selected = "";
            if (key.trim().equals(value.trim())){
                selected = " selected";
            }
            out.append("<option value=\""+Str.cleanForHtml(key.trim())+"\" "+selected+">" + Str.truncateString(val.trim(), maxlength) + "</option>");
        }
        out.append("</select>");

        return out.toString();

    }




    public static String getValueFromRequest(String name, String prettyName, boolean isrequired) throws ValidationException {
        if (Pagez.getRequest().getParameter(name)!=null && !Pagez.getRequest().getParameter(name).equals("")){
            return Pagez.getRequest().getParameter(name);
        }
        if (isrequired){
            throw new ValidationException(prettyName+" is required.");
        }
        return "";
    }

    public static int getIntFromRequest(String name, String prettyName, boolean isrequired) throws ValidationException {
        String str = getValueFromRequest(name, prettyName, isrequired);
        if (Num.isinteger(str)){
            return Integer.parseInt(str);
        } else {
            if (Num.isdouble(str)){
                Double dbl = Double.parseDouble(str);
                return dbl.intValue();
            }
        }
        return 0;
    }

    public static double getDblFromRequest(String name, String prettyName, boolean isrequired) throws ValidationException {
        String str = getValueFromRequest(name, prettyName, isrequired);
        if (Num.isdouble(str)){
            Double dbl = Double.parseDouble(str);
            return dbl;
        }
        return 0;
    }


}
