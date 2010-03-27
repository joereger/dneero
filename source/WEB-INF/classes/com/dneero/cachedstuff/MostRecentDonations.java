package com.dneero.cachedstuff;

import com.dneero.dao.Charitydonation;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class MostRecentDonations implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "MostRecentDonations";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='1' cellspacing='0' border='0'>");
        List<Charitydonation> charitydonations = HibernateUtil.getSession().createQuery("from Charitydonation where balanceid>0 order by charitydonationid desc").setMaxResults(25).setCacheable(true).list();
        for (Iterator<Charitydonation> iterator = charitydonations.iterator(); iterator.hasNext();) {
            Charitydonation charitydonation = iterator.next();
            User user = User.get(charitydonation.getUserid());

            out.append("<tr>");
            out.append("<td>");
            out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
            out.append("<font class='tinyfont'>");
            out.append(user.getNickname());
            out.append("</font>");
            out.append("</a>");
            //out.append("</td>");
            //out.append("<td>");
            out.append("<font class='tinyfont'> </font>");
            out.append("<font class='tinyfont'>");
            out.append(charitydonation.getCharityname());
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
        return 600;
    }

    public String getHtml() {
        return html;
    }
}
