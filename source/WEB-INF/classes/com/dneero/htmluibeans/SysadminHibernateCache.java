package com.dneero.htmluibeans;

import com.dneero.dao.hibernate.HibernateCacheStats;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.survey.servlet.ImpressionActivityObjectCollated;
import com.dneero.scheduledjobs.ImpressionActivityObjectQueue;
import com.dneero.htmlui.ValidationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 28, 2006
 * Time: 12:30:25 PM
 */
public class SysadminHibernateCache implements Serializable {

    private String cacheashtml;
    private String misccacheashtml;
    private String iaosqueue;

    public SysadminHibernateCache(){

    }



    public void initBean(){
        cacheashtml = HibernateCacheStats.getCacheDump();
        misccacheashtml = CacheFactory.getCacheProvider().getCacheStatsAsHtml();

        StringBuffer iaosqueueSb = new StringBuffer();
        ArrayList<ImpressionActivityObjectCollated> iaocs= ImpressionActivityObjectQueue.getIaocs();
        if (iaocs !=null){
            iaosqueueSb.append("<font class=\"mediumfont\">iaocs in queue: "+ iaocs.size()+"</font>");
            iaosqueueSb.append("<br>");
            iaosqueueSb.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"0\">");
            iaosqueueSb.append("<tr>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append("Imp");
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append("Referer");
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append("surveyid");
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append("userid");
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append("responseid");
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("</tr>");
            for (Iterator it = iaocs.iterator(); it.hasNext(); ) {
                ImpressionActivityObjectCollated iaoc = (ImpressionActivityObjectCollated)it.next();
                iaosqueueSb.append("<tr>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iaoc.getImpressions());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iaoc.getReferer());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iaoc.getSurveyid());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iaoc.getUserid());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iaoc.getResponseid());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("</tr>");
            }
            iaosqueueSb.append("</table>");
        }

        iaosqueue = iaosqueueSb.toString();

    }

    public void runImpressionActivityObjectQueue() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.ImpressionActivityObjectQueue task = new com.dneero.scheduledjobs.ImpressionActivityObjectQueue();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        initBean();
    }


    public String getCacheashtml() {
        return cacheashtml;
    }

    public void setCacheashtml(String cacheashtml) {
        this.cacheashtml = cacheashtml;
    }

    public String getMisccacheashtml() {
        return misccacheashtml;
    }

    public void setMisccacheashtml(String misccacheashtml) {
        this.misccacheashtml = misccacheashtml;
    }

    public String getIaosqueue() {
        return iaosqueue;
    }

    public void setIaosqueue(String iaosqueue) {
        this.iaosqueue = iaosqueue;
    }
}
