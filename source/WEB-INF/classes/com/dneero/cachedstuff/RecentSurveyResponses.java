package com.dneero.cachedstuff;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Response;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Survey;
import com.dneero.util.Time;
import com.dneero.helpers.NicknameHelper;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

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

    public void refresh() {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .setMaxResults(20)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            Blogger blogger = Blogger.get(response.getBloggerid());
            User user = User.get(blogger.getUserid());
            Survey survey = Survey.get(response.getSurveyid());
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
        out.append("</table>");

        html = out.toString();
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 5;
    }

    public String getHtml() {
        return html;
    }
}
