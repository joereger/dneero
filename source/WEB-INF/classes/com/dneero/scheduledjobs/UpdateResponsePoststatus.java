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



    public static int MAXPOSTINGPERIODINDAYS = 10;
    public static int DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD = 5;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
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
                //Process this response
                processSingleResponse(response);
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void processSingleResponse(Response response){
        Logger logger = Logger.getLogger(UpdateResponsePoststatus.class);
        try{
            //I need to find out if there are at least 20 days with impressions.
            //I'll use a treemap to add a bunch of dates as keys.  Values don't matter.
            //Duplicate (multiple) impressions on a particular day mean nothing... they just overwrite the key.
            //At the end (or during) I can do a treemap.size() to see if it's at least 20
            TreeMap dayswithimpressions = new TreeMap();
            TreeMap daybyday = new TreeMap();

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
                    //Remove time, compress the impression date to something unique for the day
                    String impressiondateAsStringNoTime = Time.dateformatdateMmddyyyy(impressiondate);
                    //What the heck, create an incrementing count
                    if (dayswithimpressions.containsKey(impressiondateAsStringNoTime)){
                        dayswithimpressions.put(impressiondateAsStringNoTime, (Integer)dayswithimpressions.get(impressiondateAsStringNoTime)+1);
                    } else {
                        dayswithimpressions.put(impressiondateAsStringNoTime, 1);
                    }
                    //Also need to store a day by day report
                    int dayssinceresponse = DateDiff.dateDiff("day", impressiondate, Time.getCalFromDate(response.getResponsedate()));
                    logger.debug("dayssinceresponse="+dayssinceresponse+" impressiondate="+Time.dateformatfordb(impressiondate)+" responsedate="+Time.dateformatfordb(Time.getCalFromDate(response.getResponsedate())));
                    if (daybyday.containsKey(dayssinceresponse)){
                        daybyday.put(dayssinceresponse, (Integer)daybyday.get(dayssinceresponse)+1);
                    } else {
                        daybyday.put(dayssinceresponse, 1);
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

            //Create response payment status html and store it in the db
            StringBuffer statusHtml = new StringBuffer();
            int dayssinceresponsefortoday = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(response.getResponsedate()));
            logger.debug("dayssinceresponsefortoday="+dayssinceresponsefortoday);
            statusHtml.append(  "<table cellpadding=\"0\" cellspacing=\"1\" border=\"0\">\n" +
                                "\t<tr>\n");
            int daysthatqualify = 0;
            for (int i = 0; i < MAXPOSTINGPERIODINDAYS; i++){
                String boxColor = "#cccccc";
                //If this box represents a day in the future
                if(i<=dayssinceresponsefortoday){
                    //If there are impressions on this day
                    if(daybyday.containsKey(i)){
                        boxColor = "#00ff00";
                        daysthatqualify = daysthatqualify + 1;
                    } else {
                        boxColor = "#ff0000";
                    }
                }
                statusHtml.append("\t\t<td width=\"10\" bgcolor=\""+boxColor+"\" class=\"surveystatusbar\"><img src=\"/images/clear.gif\" width=\"1\" height=\"15\" border=\"0\"></td>\n");
            }
            statusHtml.append(  "\t</tr>\n" +
                                "\t<tr>\n" +
                                "\t\t<td width=\"10\" bgcolor=\"#ffffff\" colspan=\"11\" nowrap><font class=\"tinyfont\">"+daysthatqualify+" Days Qualify; "+(DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD-daysthatqualify)+" More Needed</font></td>\n" +
                                "\t</tr>\n" +
                                "</table>");
            //Store it in the database for the response
            try{
                response.setResponsestatushtml(statusHtml.toString());
                response.save();
            }catch(Exception ex){logger.error(ex);}


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

        } catch (Exception ex){
            logger.error(ex);
        }
    }
}
