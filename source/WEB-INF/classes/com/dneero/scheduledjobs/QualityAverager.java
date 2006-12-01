package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.Blog;
import com.dneero.dao.Impression;
import com.dneero.dao.Blogger;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.util.Util;
import com.dneero.util.DateDiff;
import com.dneero.systemprops.InstanceProperties;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class QualityAverager implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() QualityAverager called");

            HashMap blogQuality = new HashMap();
            HashMap blogQuality90Days = new HashMap();
            HashMap bloggerQuality = new HashMap();
            HashMap bloggerQuality90Days = new HashMap();

            //Iterate impressions to collect averaging data
            List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression").list();
            for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
                Impression impression = iterator.next();
                Blog blog;
                Blogger blogger;
                logger.debug("impression.getImpressionid()="+impression.getImpressionid());
                if (impression.getBlog()!=null){
                    logger.debug("blog found with impression.getBlog() - impression.getBlog().getBlogid()="+impression.getBlog().getBlogid());
                    blog = impression.getBlog();
                    blogger = Blogger.get(blog.getBloggerid());
                } else {
                    logger.debug("blog not found with impression.getBlog()");
                    blog = new Blog();
                    blogger = new Blogger();
                }
                Calendar cal = Time.getCalFromDate(impression.getFirstseendate());
                int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), cal);
                //Delete or comment out next debug line before going live
                //logger.debug("daysold="+daysold+" cal=" + Time.dateformatfordb(cal) + " now="+Time.dateformatfordb(Calendar.getInstance()));

                //Blog quality
                if (1==1){
                    HashMap m = new HashMap();
                    if (blogQuality.containsKey(blog.getBlogid())){
                        m = (HashMap)blogQuality.get(blog.getBlogid());
                    } else {
                        m.put("impressionsqualifyingforpayment", 0);
                        m.put("totalquality", 0);
                    }
                    m.put("impressionsqualifyingforpayment", ((Integer)m.get("impressionsqualifyingforpayment"))+1);
                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
                    blogQuality.put(blog.getBlogid(), m);
                }

                //Blog quality 90 days
                if (daysold<=90){
                    HashMap m = new HashMap();
                    if (blogQuality90Days.containsKey(blog.getBlogid())){
                        m = (HashMap)blogQuality.get(blog.getBlogid());
                    } else {
                        m.put("impressionsqualifyingforpayment", 0);
                        m.put("totalquality", 0);
                    }
                    m.put("impressionsqualifyingforpayment", ((Integer)m.get("impressionsqualifyingforpayment"))+1);
                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
                    blogQuality90Days.put(blog.getBlogid(), m);
                }

                //Blogger quality
                if (1==1){
                    HashMap m = new HashMap();
                    if (bloggerQuality.containsKey(blogger.getBloggerid())){
                        m = (HashMap)bloggerQuality.get(blogger.getBloggerid());
                    } else {
                        m.put("impressionsqualifyingforpayment", 0);
                        m.put("totalquality", 0);
                    }
                    m.put("impressionsqualifyingforpayment", ((Integer)m.get("impressionsqualifyingforpayment"))+1);
                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
                    bloggerQuality.put(blog.getBloggerid(), m);
                }

                //Blogger quality 90 days
                if (daysold<=90){
                    HashMap m = new HashMap();
                    if (bloggerQuality90Days.containsKey(blogger.getBloggerid())){
                        m = (HashMap)bloggerQuality.get(blogger.getBloggerid());
                    } else {
                        m.put("impressionsqualifyingforpayment", 0);
                        m.put("totalquality", 0);
                    }
                    m.put("impressionsqualifyingforpayment", ((Integer)m.get("impressionsqualifyingforpayment"))+1);
                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
                    bloggerQuality90Days.put(blogger.getBloggerid(), m);
                }
            }

            //Debug
            logger.debug("blogQuality.size()="+blogQuality.size());
            logger.debug("blogQuality90Days.size()="+blogQuality90Days.size());
            logger.debug("bloggerQuality.size()="+bloggerQuality.size());
            logger.debug("bloggerQuality90Days.size()="+bloggerQuality90Days.size());

            //Compute blogQuality
            if (true){
                Iterator keyValuePairs = blogQuality.entrySet().iterator();
                for (int i = 0; i < blogQuality.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    int key = (Integer)mapentry.getKey();
                    if (key>0){
                        HashMap value = (HashMap)mapentry.getValue();
                        Blog blog = Blog.get(key);
                        logger.debug("blogid="+blog.getBlogid());
                        double avgquality = 0;
                        logger.debug("value.get(\"totalquality\")="+value.get("totalquality"));
                        logger.debug("(Integer)value.get(\"totalquality\")="+(Integer)value.get("totalquality"));
                        logger.debug("value.get(\"impressionsqualifyingforpayment\")="+value.get("impressionsqualifyingforpayment"));
                        logger.debug("(Integer)value.get(\"impressionsqualifyingforpayment\")="+(Integer)value.get("impressionsqualifyingforpayment"));
                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("impressionsqualifyingforpayment"); } catch (Exception ex){ logger.error(ex);}
                        logger.debug("blogid="+blog.getBlogid()+" avgquality="+avgquality);
                        blog.setQuality(avgquality);
                        try{ blog.save(); } catch (Exception ex){ logger.error(ex); }
                    }
                }
            }

            //Compute blogQuality90Days
            if (true){
                Iterator keyValuePairs = blogQuality90Days.entrySet().iterator();
                for (int i = 0; i < blogQuality90Days.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    int key = (Integer)mapentry.getKey();
                    if (key>0){
                        HashMap value = (HashMap)mapentry.getValue();
                        Blog blog = Blog.get(key);
                        double avgquality = 0;
                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("impressionsqualifyingforpayment"); } catch (Exception ex){ logger.error(ex);}
                        blog.setQuality90days(avgquality);
                        try{ blog.save(); } catch (Exception ex){ logger.error(ex); }
                    }
                }
            }

            //Compute bloggerQuality
            if (true){
                Iterator keyValuePairs = bloggerQuality.entrySet().iterator();
                for (int i = 0; i < bloggerQuality.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    int key = (Integer)mapentry.getKey();
                    if (key>0){
                        HashMap value = (HashMap)mapentry.getValue();
                        Blogger blogger = Blogger.get(key);
                        double avgquality = 0;
                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("impressionsqualifyingforpayment"); } catch (Exception ex){ logger.error(ex);}
                        blogger.setQuality(avgquality);
                        try{ blogger.save(); } catch (Exception ex){ logger.error(ex); }
                    }
                }
            }

            //Compute bloggerQuality90Days
            if (true){
                Iterator keyValuePairs = bloggerQuality90Days.entrySet().iterator();
                for (int i = 0; i < bloggerQuality90Days.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    int key = (Integer)mapentry.getKey();
                    if (key>0){
                        HashMap value = (HashMap)mapentry.getValue();
                        Blogger blogger = Blogger.get(key);
                        double avgquality = 0;
                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("impressionsqualifyingforpayment"); } catch (Exception ex){ logger.error(ex);}
                        blogger.setQuality90days(avgquality);
                        try{ blogger.save(); } catch (Exception ex){ logger.error(ex); }
                    }
                }
            }
        }

    }

}
