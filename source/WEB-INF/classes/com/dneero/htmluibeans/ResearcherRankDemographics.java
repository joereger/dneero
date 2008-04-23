package com.dneero.htmluibeans;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.reports.FieldAggregator;
import com.dneero.reports.SimpleTableOutput;
import com.dneero.util.DateDiff;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:40 AM
 */
public class ResearcherRankDemographics implements Serializable {


    private Rank rank;
    private String html;
    private String whengenerated="";


    public ResearcherRankDemographics(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rank = Rank.get((Integer.parseInt(Pagez.getRequest().getParameter("rankid"))));
        }
        String key = String.valueOf(rank.getRankid());
        String group = "RankDemographicReport";
        Object obj = CacheFactory.getCacheProvider().get(key, group);
        if (obj!=null && (obj instanceof ResearcherResultsDemographicsCacheitem)){
            logger.debug("found a report in the cache");
            ResearcherResultsDemographicsCacheitem ci = (ResearcherResultsDemographicsCacheitem)obj;
            int minago = DateDiff.dateDiff("minute", Calendar.getInstance(), ci.getLastupdated());
            whengenerated = minago + " minutes ago";
            if (minago==1){
                whengenerated = minago + " minute ago";
            }
            logger.debug("report is "+minago+" minutes old");
            if (minago>60){
                logger.debug("refreshing report");
                refreshReport(key, group);
                whengenerated = "0 minutes ago";
            } else {
                logger.debug("using cached report");
                html = ci.getHtml();
            }
        } else {
            logger.debug("no report in cache");
            refreshReport(key, group);
            whengenerated = "0 minutes ago";
        }
    }

    public void refreshReport(String key, String group){
        ArrayList<Blogger> bloggers = new ArrayList<Blogger>();
        if (rank!=null && rank.getRankid()>0){
//            ArrayList<Object> stats = (ArrayList<Object>) HibernateUtil.getSession().createQuery("select distinct userid, from Rankuser rankuser where rankuser.rankid="+rank.getRankid()).list();
//            for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
//                Object res = (Object)iterator.next();
//                Integer userid = (Integer)res;
//                User user = User.get(userid);
//                Blogger blogger = Blogger.get(user.getBloggerid());
//                bloggers.add(blogger);
//            }
            ArrayList<Object[]> stats = (ArrayList<Object[]>) HibernateUtil.getSession().createQuery("select userid, sum(points) from Rankuser rankuser where rankuser.rankid="+rank.getRankid()+" group by rankuser.userid").list();
            for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
                Object[] res = (Object[])iterator.next();
                Integer userid = (Integer)res[0];
                long points = (Long)res[1];
                //Only include people with positive ranking
                if (points>0){
                    User user = User.get(userid);
                    Blogger blogger = Blogger.get(user.getBloggerid());
                    bloggers.add(blogger);
                }
            }
            if (bloggers.size()>=10){
                FieldAggregator fa = new FieldAggregator((ArrayList)bloggers);
                SimpleTableOutput sto = new SimpleTableOutput(fa);
                html = sto.getHtml();
                ResearcherResultsDemographicsCacheitem ci = new ResearcherResultsDemographicsCacheitem(sto.getHtml(), Calendar.getInstance());
                CacheFactory.getCacheProvider().put(key, group, ci);
            } else {
                html = "Only "+bloggers.size()+" people are in this set.  We don't display demographics until at least 10 are included.  This is done to protect the demographic information of individuals.";
            }
        }
    }


    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getHtml() {
        return html;
    }

    public String getWhengenerated() {
        return whengenerated;
    }
}