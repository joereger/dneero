package com.dneero.cachedstuff;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.helpers.NicknameHelper;
import com.dneero.privatelabel.PlPeers;
import com.dneero.util.Time;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class RecentSurveyResponses implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "RecentSurveyResponses";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();
        int totalDisplayed = 0;
        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .setMaxResults(50)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (totalDisplayed<=20){
                Blogger blogger = Blogger.get(response.getBloggerid());
                User user = User.get(blogger.getUserid());
                Survey survey = Survey.get(response.getSurveyid());
                Pl plOfSurvey = Pl.get(survey.getPlid());
                if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfSurvey)){
                    totalDisplayed = totalDisplayed + 1;
                    Calendar cal = Time.getCalFromDate(response.getResponsedate());
                    cal = Time.convertFromOneTimeZoneToAnother(cal, cal.getTimeZone().getID(), "GMT");
                    String ago = Time.agoText(cal);
                    out.append("<tr>");
                    out.append("<td>");
                    out.append("<font class='tinyfont'>");
                    out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
                    out.append(NicknameHelper.getNameOrNickname(user));
                    out.append("</a>");
                    out.append(" responded to ");
                    out.append("<a href=\"/survey.jsp?surveyid="+survey.getSurveyid()+"&userid="+user.getUserid()+"\">");
                    out.append(survey.getTitle());
                    out.append("</a>");
                    out.append(" "+ago);
                    out.append("</font>");
                    out.append("</td>");
                    out.append("</tr>");
                }
            }
        }
        out.append("</table>");

        html = out.toString();
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 90;
    }

    public String getHtml() {
        return html;
    }
}
