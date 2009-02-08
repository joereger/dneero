package com.dneero.cachedstuff;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.*;
import com.dneero.util.Time;
import com.dneero.util.Str;
import com.dneero.helpers.NicknameHelper;
import com.dneero.privatelabel.PlPeers;

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
public class TwitterQuestionAnswers implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "TwitterQuestionAnswers";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();
        int totalTwitasksAdded = 0;
        out.append("<table cellpadding='3' cellspacing='0' border='0'>");
        List<Twitanswer> twitanswers = HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("issysadminrejected", false))
                                           .addOrder(Order.desc("twittercreatedate"))
                                           .setCacheable(true)
                                           .setMaxResults(50)
                                           .list();
        for (Iterator<Twitanswer> iterator = twitanswers.iterator(); iterator.hasNext();) {
            Twitanswer twitanswer = iterator.next();
            if (totalTwitasksAdded<=10){
                Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                Pl plOfTwitask = Pl.get(twitask.getPlid());
                if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfTwitask)){
                    totalTwitasksAdded = totalTwitasksAdded + 1;
                    String ago = Time.agoText(Time.getCalFromDate(twitanswer.getResponsedate()));
                    out.append("<tr>");
                    out.append("<td valign=\"top\">");
                    out.append("<font class='tinyfont'>");
                    out.append("<a href=\"http://twitter.com/"+twitanswer.getTwitterusername()+"\">");
                    out.append("<img src=\""+twitanswer.getTwitterprofileimageurl()+"\" border=\"0\" width=\"48\" height=\"48\"/>");
                    out.append("</a>");
                    out.append("</td>");
                    out.append("<td valign=\"top\">");
                    out.append("<font class='tinyfont'>");
                    out.append("<a href=\"http://twitter.com/"+twitanswer.getTwitterusername()+"\">");
                    out.append(twitanswer.getTwitterusername());
                    out.append("</a>");
                    out.append(" answered ");
                    out.append("<a href=\"/twitask.jsp?twitaskid="+twitask.getTwitaskid()+"\">");
                    out.append("\""+twitask.getQuestion()+"\"");
                    out.append("</a>");
                    out.append(" "+ago);
                    out.append("</font>");
                    out.append("<br/>");
                    out.append("<font class='smallfont'>");
                    out.append(twitanswer.getTwitterusername()+" said: "+ Str.cleanForHtml(twitanswer.getAnswer()));
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
        return 15;
    }

    public String getHtml() {
        return html;
    }
}