package com.dneero.cachedstuff;

import com.dneero.dao.Survey;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.helpers.SlotsRemainingInConvo;
import com.dneero.htmlui.PercentCompleteBar;
import com.dneero.incentive.IncentiveCash;
import com.dneero.incentive.IncentiveCoupon;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.privatelabel.PlPeers;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 AM
 */
public class RecentSurveys implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "RecentSurveys";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();
        int totalSurveysAdded = 0;
        out.append("<table cellpadding='2' cellspacing='1' border='0' width='100%'>");
        out.append("<tr>");
        out.append("<td><font class=\"mediumfont\" style=\"color: #999999;\">Conversations</font></td>");
        out.append("<td bgcolor='#cccccc' nowrap><font class='tinyfont' style='color: #666666;'>Ends In</font></td>");
        out.append("<td bgcolor='#cccccc' nowrap><font class='tinyfont' style='color: #666666;'>Earn</font></td>");
        out.append("<td bgcolor='#cccccc' nowrap><font class='tinyfont' style='color: #666666;'>Slots Available</font></td>");
        out.append("</tr>");
        //ArrayList<SurveyListItem> surveys = new ArrayList<SurveyListItem>();
        List openSurveys = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_OPEN+"' order by surveyid desc").list();
        for (Iterator iterator = openSurveys.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();
            Pl plOfSurvey = Pl.get(survey.getPlid());
            if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfSurvey)){
                out.append(formatSurvey(survey));
                totalSurveysAdded = totalSurveysAdded + 1;
            }
        }
        List closedSurveys = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_CLOSED+"' order by surveyid desc").setMaxResults(50).list();
        for (Iterator iterator = closedSurveys.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();
            if (totalSurveysAdded<13){
                Pl plOfSurvey = Pl.get(survey.getPlid());
                if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfSurvey)){
                    out.append(formatSurvey(survey));
                    totalSurveysAdded = totalSurveysAdded + 1;
                }
            }
        }
        out.append("</table>");

        html = out.toString();
        refreshedTimestamp = Calendar.getInstance();
    }

    private String formatSurvey(Survey survey){
        StringBuffer out = new StringBuffer();
        SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);

        String daysleftStr = "";
        int daysleft = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Calendar.getInstance());
        if (daysleft==0){
            daysleftStr = "Today";
        } else if (daysleft==1){
            daysleftStr = "Tomorrow";
        } else if (daysleft<0){
            daysleftStr = "";
        } else {
            daysleftStr = daysleft + " days";
        }

        if (survey.getStatus()==Survey.STATUS_CLOSED){
            daysleftStr = "Closed";
        }

        out.append("<tr>");
        out.append("<td>");
        if (survey.getStatus()==Survey.STATUS_OPEN){
            out.append("<a href=\"/survey.jsp?surveyid="+survey.getSurveyid()+"\">");
            out.append("<font class='tinyfont'>");
            out.append(survey.getTitle());
            out.append("</font>");
            out.append("</a>");
        } else {
            out.append("<a href=\"/surveyresults.jsp?surveyid="+survey.getSurveyid()+"\">");
            out.append("<font class='tinyfont'>");
            out.append(survey.getTitle());
            out.append("</font>");
            out.append("</a>");
        }
        out.append("</td>");
        out.append("<td nowrap>");
        out.append("<font class='tinyfont'>");
        out.append(daysleftStr);
        out.append("</font>");
        out.append("</td>");
        out.append("<td nowrap>");
        out.append("<font class='tinyfont'>");
        if(survey.getIncentive().getSurveyincentive().getType()==IncentiveCash.ID){
            out.append("<b>"+ survey.getIncentive().getShortSummary()+"</b>");
        } else if (survey.getIncentive().getSurveyincentive().getType()==IncentiveCoupon.ID){
            out.append("<b>Coupon</b>");
        } else {
            out.append("");
        }
        out.append("</font>");
        out.append("</td>");
        out.append("<td width='75' valign='middle'>");
        out.append("<img src=\"/images/clear.gif\" width=\"1\" height=\"3\"><br/>");
        out.append("<font class='tinyfont'>");
        int responsesrequested = survey.getNumberofrespondentsrequested();
        int remainingslots = SlotsRemainingInConvo.getSlotsRemaining(survey);
        if (remainingslots<0){
            remainingslots = 0;   
        }
        if (survey.getStatus()==Survey.STATUS_CLOSED){
            remainingslots = 0;  
        }
        String bar = PercentCompleteBar.get(String.valueOf(remainingslots), String.valueOf(responsesrequested), "", "", "75", 5, false);
        out.append(bar);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        return out.toString();
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
