package com.dneero.cachedstuff;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class MostRecentPaidInRealWorld implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "MostRecentPaidInRealWorld";
    }

    public void refresh() {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Balancetransaction> balancetransactions = HibernateUtil.getSession().createCriteria(Balancetransaction.class)
                                           .add(Restrictions.eq("issuccessful", true))
                                           .add(Restrictions.gt("amt", 0.0))
                                           .addOrder(Order.desc("balancetransactionid"))
                                           .setCacheable(true)
                                           .setMaxResults(20)
                                           .list();
        for (Iterator<Balancetransaction> iterator = balancetransactions.iterator(); iterator.hasNext();) {
            Balancetransaction balancetransaction = iterator.next();
            User user = User.get(balancetransaction.getUserid());
            out.append("<tr>");
            out.append("<td>");
            out.append("<a href='/profile.jsp?userid="+user.getUserid()+"'>");
            out.append("<font class='tinyfont'>");
            out.append(user.getFirstname()+" "+user.getLastname());
            out.append("</font>");
            out.append("</a>");
            out.append("</td>");
            out.append("<td>");
            out.append("<font class='tinyfont'>");
            out.append("$"+Str.formatForMoney(balancetransaction.getAmt()));
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
