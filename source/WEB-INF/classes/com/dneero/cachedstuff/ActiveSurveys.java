package com.dneero.cachedstuff;

import com.dneero.htmluibeans.SurveyListItem;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class ActiveSurveys implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "ActiveSurveys";
    }

    public void refresh() {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        ArrayList<SurveyListItem> surveys = new ArrayList<SurveyListItem>();
        List results = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_OPEN+"' order by surveyid desc").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();

            double maxearningNum = survey.getWillingtopayperrespondent()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );

            String daysleftStr = "";
            int daysleft = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Calendar.getInstance());
            if (daysleft==0){
                daysleftStr = "Ends today!";
            } else if (daysleft==1){
                daysleftStr = "One day left!";
            } else {
                daysleftStr = daysleft + " days left!";
            }

            out.append("<tr>");
            out.append("<td>");
            out.append("<a href='/survey.jsp?userid="+survey.getSurveyid()+"'>");
            out.append("<font class='tinyfont'>");
            out.append(survey.getTitle());
            out.append("</font>");
            out.append("</a>");
            out.append("</td>");
            out.append("<td>");
            out.append("<font class='tinyfont'>");
            out.append(daysleftStr);
            out.append("</font>");
            out.append("</td>");
            out.append("<td>");
            out.append("<font class='tinyfont'>");
            out.append("$"+Str.formatForMoney(maxearningNum));
            out.append("</font>");
            out.append("</td>");
            out.append("</tr>");
        }
        out.append("</table>");

        html = out.toString();
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 20;
    }

    public String getHtml() {
        return html;
    }
}
