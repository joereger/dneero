package com.dneero.reports;

import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Jan 28, 2008
 * Time: 2:27:59 PM
 */
public class SimpleTableOutput {

    private String html;
    private FieldAggregator fa;
    private Pl pl;

    public SimpleTableOutput(FieldAggregator fa, Pl pl){
        this.fa = fa;
        this.pl = pl;
        process("");
    }

    public SimpleTableOutput(FieldAggregator fa, Pl pl, String onlyshow){
        this.fa = fa;
        this.pl = pl;
        process(onlyshow);
    }

    private void process(String onlyshow){
        Logger logger = Logger.getLogger(this.getClass().getName());
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");

        if (onlyshow.equals("")||onlyshow.equals("age")){
            out.append(getHtmlForField(fa.getAge(), "Age"));
        }
        try{
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", pl.getPlid()))
                                           .setCacheable(true)
                                           .list();
            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                Demographic demographic = demographicIterator.next();
                if (onlyshow.equals("")||onlyshow.equals(String.valueOf(demographic.getDemographicid()))){
                    out.append(getHtmlForField(fa.getDemographicResults(demographic.getDemographicid()), demographic.getName()));
                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        if (onlyshow.equals("")||onlyshow.equals("dneerousagemethod")){
            out.append(getHtmlForField(fa.getDneerousagemethods(), "Usage Method"));
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
