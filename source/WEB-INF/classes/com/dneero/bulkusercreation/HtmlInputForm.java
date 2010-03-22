package com.dneero.bulkusercreation;

import com.dneero.util.Str;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 21, 2010
 * Time: 9:08:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlInputForm {


    public static String getForm(ArrayList<Bulkuser> bulkusers){
        StringBuffer out = new StringBuffer();
        int counter = 0;
        for (Iterator<Bulkuser> bulkuserIterator = bulkusers.iterator(); bulkuserIterator.hasNext();) {
            Bulkuser bulkuser = bulkuserIterator.next();
            counter++;
            String color = "#e6e6e6";
            if (!bulkuser.getIsvalid()){ color = "#00ff00"; }
            out.append("<tr>");
            out.append("<td valign='top'>");
            out.append("<input type=\"text\" name=\"first"+counter+"\" value=\""+ Str.cleanForHtml(bulkuser.getFirst())+"\" size=\"15\" maxlength=\"255\">");
            out.append("</td>");
            out.append("<td valign='top'>");
            out.append("<input type=\"text\" name=\"last"+counter+"\" value=\""+ Str.cleanForHtml(bulkuser.getLast())+"\" size=\"15\" maxlength=\"255\">");
            out.append("</td>");
            out.append("<td valign='top'>");
            out.append("<input type=\"text\" name=\"nickname"+counter+"\" value=\""+ Str.cleanForHtml(bulkuser.getNickname())+"\" size=\"15\" maxlength=\"255\">");
            out.append("</td>");
            out.append("<td valign='top'>");
            out.append("<input type=\"text\" name=\"email"+counter+"\" value=\""+ Str.cleanForHtml(bulkuser.getEmail())+"\" size=\"30\" maxlength=\"255\">");
            out.append("</td>");
            out.append("<td valign='top'>");
            out.append("<input type=\"text\" name=\"password"+counter+"\" value=\""+ Str.cleanForHtml(bulkuser.getPassword())+"\" size=\"15\" maxlength=\"255\">");
            out.append("</td>");
            out.append("<td valign='top'  style=\"background: "+color+";\">");
            out.append(bulkuser.getErrors());
            out.append("</td>");
            out.append("</tr>");
        }
        return out.toString();
    }


}
