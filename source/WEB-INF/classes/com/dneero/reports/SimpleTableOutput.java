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
        process("");
    }

    public SimpleTableOutput(FieldAggregator fa, String onlyshow){
        this.fa = fa;
        process(onlyshow);
    }

    private void process(String onlyshow){
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        if (onlyshow.equals("")||onlyshow.equals("dneerousagemethod")){
            out.append(getHtmlForField(fa.getDneerousagemethods(), "dNeero Usage Method"));
        }
        if (onlyshow.equals("")||onlyshow.equals("gender")){
            out.append(getHtmlForField(fa.getGender(), "Gender"));
        }
        if (onlyshow.equals("")||onlyshow.equals("age")){
            out.append(getHtmlForField(fa.getAge(), "Age"));
        }
        if (onlyshow.equals("")||onlyshow.equals("educationlevel")){
            out.append(getHtmlForField(fa.getEducationlevel(), "Education Level"));
        }
        if (onlyshow.equals("")||onlyshow.equals("ethnicity")){
            out.append(getHtmlForField(fa.getEthnicity(), "Ethnicity"));
        }
        if (onlyshow.equals("")||onlyshow.equals("income")){
            out.append(getHtmlForField(fa.getIncome(), "Income"));
        }
        if (onlyshow.equals("")||onlyshow.equals("maritalstatus")){
            out.append(getHtmlForField(fa.getMaritalstatus(), "Marital Status"));
        }
        if (onlyshow.equals("")||onlyshow.equals("politics")){
            out.append(getHtmlForField(fa.getPolitics(), "Politics"));
        }
        if (onlyshow.equals("")||onlyshow.equals("blogfocus")){
            out.append(getHtmlForField(fa.getBlogfocus(), "Blog Focus"));
        }
        if (onlyshow.equals("")||onlyshow.equals("profession")){
            out.append(getHtmlForField(fa.getProfession(), "Profession"));
        }
        if (onlyshow.equals("")||onlyshow.equals("city")){
            out.append(getHtmlForField(fa.getCity(), "City"));
        }
        if (onlyshow.equals("")||onlyshow.equals("state")){
            out.append(getHtmlForField(fa.getState(), "State"));
        }
        if (onlyshow.equals("")||onlyshow.equals("country")){
            out.append(getHtmlForField(fa.getState(), "Country"));
        }

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
