package com.dneero.cachedstuff;

import com.dneero.dao.Impression;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.util.Str;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class MostActiveImpressionLocations implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "MostActiveImpressionLocations";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                           .add(Restrictions.gt("impressionstotal", 25))
                                           .add(Restrictions.ne("referer", "Facebook"))
                                           .add(Restrictions.ne("referer", ""))
                                           .addOrder(Order.desc("impressionid"))
                                           .setCacheable(true)
                                           .setMaxResults(200)
                                           .list();
        int impressionsfound = 0;
        ArrayList<String> previouslyaddedreferers = new ArrayList<String>();
        for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            if (impressionsfound<=25 && !previouslyaddedreferers.contains(Str.truncateString(impression.getReferer(), 15))){
                previouslyaddedreferers.add(Str.truncateString(impression.getReferer(), 15));
                impressionsfound = impressionsfound + 1;
                User user = User.get(impression.getUserid());
                out.append("<tr>");
                out.append("<td>");
                out.append("<a href=\""+impression.getReferer()+"\">");
                out.append("<font class='tinyfont'>");
                out.append(Str.truncateString(impression.getReferer(), 15)+"...");
                out.append("</font>");
                out.append("</a>");
                out.append("</td>");
                out.append("<td>");
                out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
                out.append("<font class='tinyfont'>");
                out.append(user.getNickname());
                out.append("</font>");
                out.append("</a>");
                out.append("</td>");
                out.append("</tr>");
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
        return 1440;
    }

    public String getHtml() {
        return html;
    }
}
