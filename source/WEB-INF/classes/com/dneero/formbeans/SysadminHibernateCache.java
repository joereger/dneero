package com.dneero.formbeans;

import com.dneero.dao.hibernate.HibernateCacheStats;
import com.dneero.util.Jsf;
import com.dneero.cache.providers.CacheProvider;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.scheduledjobs.ImpressionActivityObjectQueue;

import javax.faces.context.FacesContext;
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

    public String beginView(){
        load();
        return "sysadminhibernatecache";
    }

    private void load(){
        cacheashtml = HibernateCacheStats.getCacheDump();
        misccacheashtml = CacheFactory.getCacheProvider().getCacheStatsAsHtml();

        StringBuffer iaosqueueSb = new StringBuffer();
        ArrayList<ImpressionActivityObject> iaos = ImpressionActivityObjectQueue.getIaos();
        if (iaos!=null){
            iaosqueueSb.append("<font class=\"mediumfont\">iaos in queue: "+iaos.size()+"</font>");
            iaosqueueSb.append("<br>");
            iaosqueueSb.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"0\">");
            for (Iterator it = iaos.iterator(); it.hasNext(); ) {
                ImpressionActivityObject iao = (ImpressionActivityObject)it.next();
                iaosqueueSb.append("<tr>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iao.getDate());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iao.getIp());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iao.getReferer());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iao.getSurveyid());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("<td valign=\"top\">");
                iaosqueueSb.append(iao.getUserid());
                iaosqueueSb.append("</td>");
                iaosqueueSb.append("</tr>");
            }
            iaosqueueSb.append("</table>");
        }

        iaosqueue = iaosqueueSb.toString();

    }

    public String runImpressionActivityObjectQueue(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.ImpressionActivityObjectQueue task = new com.dneero.scheduledjobs.ImpressionActivityObjectQueue();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminhibernatecache";
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
