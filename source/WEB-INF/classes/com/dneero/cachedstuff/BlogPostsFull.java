package com.dneero.cachedstuff;

import com.dneero.dao.Blogpost;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class BlogPostsFull implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "BlogPostsFull";
    }

    public void refresh(Pl pl) {
        StringBuffer out = new StringBuffer();

        out.append("<table cellpadding='3' cellspacing='0' border='0' width='100%'>");
        List<Blogpost> blogposts = HibernateUtil.getSession().createQuery("from Blogpost where plid='"+pl.getPlid()+"' order by date DESC").setCacheable(true).setMaxResults(25).list();
        for (int i = 0; i < blogposts.size(); i++) {
            Blogpost blogpost = blogposts.get(i);
            //String bodyTmp = blogpost.getBody().replaceAll( PublicBlog.CARRIAGERETURN + PublicBlog.LINEBREAK, "<br>");
            //blogpost.setBody(bodyTmp);
            Calendar cal = Time.getCalFromDate(blogpost.getDate());
            cal = Time.convertFromOneTimeZoneToAnother(cal, cal.getTimeZone().getID(), "GMT");
            String ago = Time.agoText(cal);
            out.append("<tr>");
            out.append("<td>");
            out.append("<font class='tinyfont'>");
            out.append(blogpost.getAuthor());
            out.append(" - ");
            out.append(ago);
            out.append("</font>");
            out.append("<br/>");
            out.append("<font class='normalfont' style='font-weight:bold;'>");
            out.append("<a href=\"/blogpost.jsp?blogpostid="+blogpost.getBlogpostid()+"\">");
            out.append(blogpost.getTitle());
            out.append("</a>");
            out.append("</font>");
            out.append("<br/>");
            out.append("<font class='normalfont'>");
            out.append(blogpost.getBody());
            out.append("<br/><br/>");
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
        return 1440;
    }

    public String getHtml() {
        return html;
    }
}