package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Impression;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.util.DateDiff;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class UpdateResponsePoststatus implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static int MAXPOSTINGPERIODINDAYS = 10;
    public static int DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD = 5;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() UpdateResponsePoststatus called");
            //Note that I'm saying NOT timepasses AND NOT moddeclined AND NOT posted
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                   .add( Restrictions.ne("poststatus", Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED))
                                   .add( Restrictions.ne("poststatus", Response.POSTATUS_POSTED))
                                   .setCacheable(false)
                                   .list();
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                logger.debug("response.getResponseid()="+response.getResponseid());
                logger.debug("response.getResponsedate()="+ Time.dateformatcompactwithtime(Time.getCalFromDate(response.getResponsedate())));
                logger.debug("new Date()="+Time.dateformatcompactwithtime(Time.getCalFromDate(new Date())));

                //I need to find out if there are at least 20 days with impressions.
                //I'll use a treemap to add a bunch of dates as keys.  Values don't matter.
                //Duplicate (multiple) impressions on a particular day mean nothing... they just overwrite the key.
                //At the end (or during) I can do a treemap.size() to see if it's at least 20
                TreeMap dayswithimpressions = new TreeMap();

                //Start by pulling up impressions for this responseid
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                                   .add(Restrictions.eq("responseid", response.getResponseid()))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Impression> iterator2 = impressions.iterator(); iterator2.hasNext();) {
                    Impression impression = iterator2.next();

                    //Next, pull up impressiondetails for this impressionid
                    List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                                   .add(Restrictions.eq("impressionid", impression.getImpressionid()))
                                                   .setCacheable(true)
                                                   .list();
                    for (Iterator<Impressiondetail> iterator3 = impressiondetails.iterator(); iterator3.hasNext();) {
                        Impressiondetail impressiondetail = iterator3.next();
                        Calendar impressiondate = Time.getCalFromDate(impressiondetail.getImpressiondate());
                        String impressiondateAsStringNoTime = Time.dateformatdateMmddyyyy(impressiondate);
                        //What the heck, create an incrementing count
                        if (dayswithimpressions.containsKey(impressiondateAsStringNoTime)){
                            dayswithimpressions.put(impressiondateAsStringNoTime, (Integer)dayswithimpressions.get(impressiondateAsStringNoTime)+1);
                        } else {
                            dayswithimpressions.put(impressiondateAsStringNoTime, 1);
                        }
                    }
                }

                //Debug, print out treemap
                Iterator keyValuePairs = dayswithimpressions.entrySet().iterator();
                logger.debug("dayswithimpressions results... size()="+dayswithimpressions.size());
                for (int i = 0; i < dayswithimpressions.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    String key = (String)mapentry.getKey();
                    int value = (Integer)mapentry.getValue();
                    logger.debug("date: "+key+" impressions: "+value);
                }

                //Save updated response status, if necessary
                if (dayswithimpressions.size()>=1){
                    response.setPoststatus(Response.POSTATUS_POSTEDATLEASTONCE);
                    if (dayswithimpressions.size()>=DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD){
                        response.setPoststatus(Response.POSTATUS_POSTED);
                    }
                    try{response.save();}catch(Exception ex){logger.error(ex);}
                }

                //If the time period for evaluating impressions has passed, set that status too
                Calendar responseDateAsCal = Time.getCalFromDate(response.getResponsedate());
                int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), responseDateAsCal);
                if (daysold>MAXPOSTINGPERIODINDAYS){
                    response.setPoststatus(Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED);
                    try{response.save();}catch(Exception ex){logger.error(ex);}
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
