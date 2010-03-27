package com.dneero.cachedstuff;

import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class NewestUsers implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "NewestUsers";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("isenabled", true))
                                           .addOrder(Order.desc("userid"))
                                           .setCacheable(true)
                                           .setMaxResults(20)
                                           .list();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            out.append("<tr>");
            out.append("<td>");
            out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
            out.append("<font class='tinyfont'>");
            out.append(user.getNickname());
            out.append("</font>");
            out.append("</a>");
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
        return 30;
    }

    public String getHtml() {
        return html;
    }
}
