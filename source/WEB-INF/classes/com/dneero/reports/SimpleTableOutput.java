package com.dneero.reports;

import com.dneero.util.Str;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Jan 28, 2008
 * Time: 2:27:59 PM
 */
public class SimpleTableOutput {

    private String html;
    private FieldAggregator fa;

    public SimpleTableOutput(FieldAggregator fa){
        this.fa = fa;
        process();
    }

    private void process(){
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");

        out.append(getHtmlForField(fa.getDneerousagemethods(), "dNeero Usage Method"));
        out.append(getHtmlForField(fa.getGender(), "Gender"));
        out.append(getHtmlForField(fa.getAge(), "Age"));
        out.append(getHtmlForField(fa.getEducationlevel(), "Education Level"));
        out.append(getHtmlForField(fa.getEthnicity(), "Ethnicity"));
        out.append(getHtmlForField(fa.getIncome(), "Income"));
        out.append(getHtmlForField(fa.getMaritalstatus(), "Marital Status"));
        out.append(getHtmlForField(fa.getPolitics(), "Politics"));
        out.append(getHtmlForField(fa.getBlogfocus(), "Blog Focus"));
        out.append(getHtmlForField(fa.getProfession(), "Profession"));
        out.append(getHtmlForField(fa.getCity(), "City"));
        out.append(getHtmlForField(fa.getState(), "State"));

        out.append("</table>");

        html = out.toString();
    }

    private String getHtmlForField(TreeMap<String, Integer> map, String fieldname){
        StringBuffer out = new StringBuffer();

        //Calculate total num
        int total = 0;
        Iterator keyValuePairs = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            int value = (Integer)mapentry.getValue();
            total = total + value;
        }

        out.append("<tr>");
        out.append("<td colspan='4'>");
        out.append("<b>"+fieldname+"</b>");
        out.append("</td>");
        out.append("</tr>");

        Iterator keyValuePairs2 = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs2.next();
            String key = (String)mapentry.getKey();
            int value = (Integer)mapentry.getValue();

            out.append("<tr>");
            out.append("<td>");
            out.append(key);
            out.append("</td>");
            out.append("<td>");
            out.append(value);
            out.append("</td>");
            out.append("<td>");
            double percent = 0;
            if (total>0){
                percent = (Double.parseDouble(String.valueOf(value))/Double.parseDouble(String.valueOf(total))) * 100;
                out.append(Str.formatForMoney(percent));
                out.append("%");
            }
            out.append("</td>");
            out.append("<td style=\"vertical-align: middle;\">");
            if (percent>0){
                int maxwidth = 300;
                double width = (percent/100) * maxwidth;
                out.append("<img src='../images/bar_green-blend.gif' width='"+width+"' height='5'>");
            }
            out.append("</td>");
            out.append("</tr>");
        }

        return out.toString();
    }


    public String getHtml() {
        return html;
    }
}
