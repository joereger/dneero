package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.Impression;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.systemprops.InstanceProperties;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class QualityAverager implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() QualityAverager called");

//            HashMap bloggerQuality = new HashMap();
//            HashMap bloggerQuality90Days = new HashMap();

            //Iterate bloggers to collect averaging data
            List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();
            for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
                Blogger blogger = iterator.next();
                //int uniquequalityratingsincludedincalculation = NumFromUniqueResult.getInt("select count(*) from Impression where quality>'0' and userid='"+blogger.getUserid()+"'");
                double avgquality =  NumFromUniqueResult.getDouble("select avg(quality) from Impression where quality>'0' and userid='"+blogger.getUserid()+"'");
                blogger.setQuality(avgquality);
                blogger.setQuality90days(avgquality);
                try{ blogger.save(); } catch (Exception ex){ logger.error(ex); }
            }

//            //Iterate impressions to collect averaging data
//            List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression").list();
//            for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
//                Impression impression = iterator.next();
//                Blogger blogger;
//                User user = User.get(impression.getUserid());
//                if (user!=null && user.getUserid()>0 && user.getBloggerid()>0){
//                    blogger = Blogger.get(user.getBloggerid());
//                } else {
//                    blogger = new Blogger();
//                }
//                Calendar cal = Time.getCalFromDate(impression.getFirstseen());
//                int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), cal);
//                //Delete or comment out next debug line before going live
//                //logger.debug("daysold="+daysold+" cal=" + Time.dateformatfordb(cal) + " now="+Time.dateformatfordb(Calendar.getInstance()));
//
//
//
//                //Blogger quality
//                if (1==1){
//                    HashMap m = new HashMap();
//                    if (bloggerQuality.containsKey(blogger.getBloggerid())){
//                        m = (HashMap)bloggerQuality.get(blogger.getBloggerid());
//                    } else {
//                        m.put("uniquequalityratingsincludedincalculation", 0);
//                        m.put("totalquality", 0);
//                    }
//                    m.put("uniquequalityratingsincludedincalculation", ((Integer)m.get("uniquequalityratingsincludedincalculation"))+1);
//                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
//                    bloggerQuality.put(blogger.getBloggerid(), m);
//                }
//
//                //Blogger quality 90 days
//                if (daysold<=90){
//                    HashMap m = new HashMap();
//                    if (bloggerQuality90Days.containsKey(blogger.getBloggerid())){
//                        m = (HashMap)bloggerQuality.get(blogger.getBloggerid());
//                    } else {
//                        m.put("uniquequalityratingsincludedincalculation", 0);
//                        m.put("totalquality", 0);
//                    }
//                    m.put("uniquequalityratingsincludedincalculation", ((Integer)m.get("uniquequalityratingsincludedincalculation"))+1);
//                    m.put("totalquality", ((Integer)m.get("totalquality"))+impression.getQuality());
//                    bloggerQuality90Days.put(blogger.getBloggerid(), m);
//                }
//            }
//
//            //Debug
//            logger.debug("bloggerQuality.size()="+bloggerQuality.size());
//            logger.debug("bloggerQuality90Days.size()="+bloggerQuality90Days.size());
//
//
//
//            //Compute bloggerQuality
//            if (true){
//                Iterator keyValuePairs = bloggerQuality.entrySet().iterator();
//                for (int i = 0; i < bloggerQuality.size(); i++){
//                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//                    int key = (Integer)mapentry.getKey();
//                    if (key>0){
//                        HashMap value = (HashMap)mapentry.getValue();
//                        Blogger blogger = Blogger.get(key);
//                        double avgquality = 0;
//                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("uniquequalityratingsincludedincalculation"); } catch (Exception ex){ logger.error(ex);}
//                        blogger.setQuality(avgquality);
//                        try{ blogger.save(); } catch (Exception ex){ logger.error(ex); }
//                    }
//                }
//            }
//
//            //Compute bloggerQuality90Days
//            if (true){
//                Iterator keyValuePairs = bloggerQuality90Days.entrySet().iterator();
//                for (int i = 0; i < bloggerQuality90Days.size(); i++){
//                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//                    int key = (Integer)mapentry.getKey();
//                    if (key>0){
//                        HashMap value = (HashMap)mapentry.getValue();
//                        Blogger blogger = Blogger.get(key);
//                        double avgquality = 0;
//                        try{avgquality = (Integer)value.get("totalquality") / (Integer)value.get("uniquequalityratingsincludedincalculation"); } catch (Exception ex){ logger.error(ex);}
//                        blogger.setQuality90days(avgquality);
//                        try{ blogger.save(); } catch (Exception ex){ logger.error(ex); }
//                    }
//                }
//            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

}
