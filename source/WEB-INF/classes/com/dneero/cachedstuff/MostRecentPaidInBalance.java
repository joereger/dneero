package com.dneero.cachedstuff;

import com.dneero.dao.Balance;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.privatelabel.PlPeers;
import com.dneero.util.Time;
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
public class MostRecentPaidInBalance implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "MostRecentPaidInbalance";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();
        int totalDisplayed = 0;
        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Balance> balances = HibernateUtil.getSession().createCriteria(Balance.class)
                                           .add(Restrictions.eq("isbloggermoney", true))
                                           .add(Restrictions.gt("amt", 0.0))
                                           .addOrder(Order.desc("balanceid"))
                                           .setCacheable(true)
                                           .setMaxResults(50)
                                           .list();
        for (Iterator<Balance> iterator = balances.iterator(); iterator.hasNext();) {
            Balance balance = iterator.next();
            User user = User.get(balance.getUserid());
            if (totalDisplayed<=20){
                Pl plOfUser = Pl.get(user.getPlid());
                if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfUser)){
                    totalDisplayed = totalDisplayed + 1;
                    Calendar cal = Time.getCalFromDate(balance.getDate());
                    cal = Time.convertFromOneTimeZoneToAnother(cal, cal.getTimeZone().getID(), "GMT");
                    String ago = Time.agoText(cal);
                    out.append("<tr>");
                    out.append("<td>");
                    out.append("<font class='tinyfont'>");
                    out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
                    out.append(user.getNickname());
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
        return 360;
    }

    public String getHtml() {
        return html;
    }
}
