package com.dneero.cachedstuff;

import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.helpers.NicknameHelper;
import com.dneero.privatelabel.PlPeers;

import java.io.Serializable;
import java.util.*;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.Query;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class MostActiveUsersByTotalSurveysTaken implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "MostActiveUsersByTotalSurveysTaken";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();

        //Put userid, surveystaken into a treemap
        TreeMap tm = new TreeMap();
        String emptyStr = "";
        List objectlist = HibernateUtil.getSession().createQuery("select bloggerid, sum(1) from Response group by bloggerid"+emptyStr).list();
        for (Iterator iterator = objectlist.iterator(); iterator.hasNext();) {
            Object[] row = (Object[]) iterator.next();
            Blogger blogger = Blogger.get((Integer)row[0]);
            long surveystaken = (Long)row[1];
            System.out.println("bloggerid="+blogger.getBloggerid()+"surveystaken="+surveystaken);
            //Put into the TreeMap
            //if (surveystaken>=75){
                tm.put(blogger.getBloggerid(), surveystaken);
            //}
        }

        //Create a sorting TreeSet and add everything from the TreeMap to it
        TreeSet set = new TreeSet(new Comparator() {
            public int compare(Object obj, Object obj1) {
                return ((Comparable) ((Map.Entry) obj1).getValue()).compareTo(((Map.Entry) obj).getValue());
            }
        });
        set.addAll(tm.entrySet());

        //Output the TreeSet as html
        int numberadded = 0;
        out.append("<table cellpadding='3' cellspacing='0' border='0' width='100%'>");
        for (Iterator i = set.iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            int bloggerid = (Integer)entry.getKey();
            long surveystaken = (Long)entry.getValue();
            if (numberadded<=20){
                Blogger blogger = Blogger.get(bloggerid);
                User user = User.get(blogger.getUserid());
                if (user!=null && user.getIsenabled()){
                    Pl plOfUser = Pl.get(user.getPlid());
                    if (PlPeers.isThereATwoWayTrustRelationship(pl, plOfUser)){
                        numberadded = numberadded + 1;
                        out.append("<tr>");
                        out.append("<td>");
                        out.append("<a href=\"/profile.jsp?userid="+user.getUserid()+"\">");
                        out.append("<font class='tinyfont'>");
                        out.append(NicknameHelper.getNameOrNickname(user));
                        out.append("</font>");
                        out.append("</a>");
                        out.append("</td>");
                        out.append("<td width='35%'>");
                        out.append("<font class='tinyfont'>");
                        out.append(surveystaken + " convos");
                        out.append("</font>");
                        out.append("</td>");
                        out.append("</tr>");
                    }
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
        return 10080;
    }

    public String getHtml() {
        return html;
    }
}
