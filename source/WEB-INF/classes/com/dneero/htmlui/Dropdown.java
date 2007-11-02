package com.dneero.htmlui;

import com.dneero.util.Str;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Dropdown {

    public static String getHtml(String name, String value, ArrayList<String> options, String styleclass, String style){
        StringBuffer out = new StringBuffer();

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }

        out.append("<select name=\""+Str.cleanForHtml(name)+"\">");
        out.append("<option value=\"\"></option>");
        for (Iterator iterator = options.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            String selected = "";
            if (s.equals(value)){
                selected = " selected";
            }
            out.append("<option value=\""+Str.cleanForHtml(s.trim())+"\" "+selected+">" + Str.truncateString(s.trim(), 30) + "</option>");
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
