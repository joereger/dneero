package com.dneero.htmlui;

import com.dneero.util.Str;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Dropdown {

    //In options<String, String> the first is the id value... second is displayed in the dropdown
    public static String getHtml(String name, String value, TreeMap<String, String> options, String styleclass, String style){
        StringBuffer out = new StringBuffer();

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }

        out.append("<select name=\""+Str.cleanForHtml(name)+"\">");
        out.append("<option value=\"\"></option>");
        Iterator keyValuePairs = options.entrySet().iterator();
        for (int i = 0; i < options.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String val = (String)mapentry.getValue();
            String selected = "";
            if (key.equals(value)){
                selected = " selected";
            }
            out.append("<option value=\""+Str.cleanForHtml(key.trim())+"\" "+selected+">" + Str.truncateString(val.trim(), 30) + "</option>");
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


}
