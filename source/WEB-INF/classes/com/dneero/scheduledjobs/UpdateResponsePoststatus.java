package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Response;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.survey.servlet.ImpressionsByDayUtil;

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
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                   .add(Restrictions.ne("poststatus", Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED))
                                   .add(Restrictions.eq("ispaid", false))
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

    public static void processAllResponses(){
        Logger logger = Logger.getLogger(UpdateResponsePoststatus.class);
        logger.debug("execute() UpdateResponsePoststatus called");
        //Note that I'm saying NOT timepasses AND NOT moddeclined AND NOT posted
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
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
    }

    public static void processSingleResponse(Response response){
        Logger logger = Logger.getLogger(UpdateResponsePoststatus.class);
        try{

            //Get a single ImpressionsByDayUtil that holds all impression by day data
            ImpressionsByDayUtil ibdus = new ImpressionsByDayUtil("");
            List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("responseid", response.getResponseid()))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Impression> iterator2 = impressions.iterator(); iterator2.hasNext();) {
                Impression impression = iterator2.next();
                ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil(impression.getImpressionsbyday());
                ibdus.add(ibdu);
            }

            //If the time period for evaluating impressions has passed, set that status too
            Calendar responseDateAsCal = Time.getCalFromDate(response.getResponsedate());
            int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), responseDateAsCal);
            boolean toolate = false;
            if (daysold>MAXPOSTINGPERIODINDAYS ){
                toolate = true;
            }

            //Create response payment status html and store it in the db
            StringBuffer statusHtml = new StringBuffer();
            int dayssinceresponsefortoday = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(response.getResponsedate()));
            logger.debug("dayssinceresponsefortoday="+dayssinceresponsefortoday);
            statusHtml.append(  "<table cellpadding=\"0\" cellspacing=\"1\" border=\"0\">\n" +
                                "\t<tr>\n");
            //Calculate number of days that qualify
            int daysthatqualify = 0;
            for (int i = 0; i < MAXPOSTINGPERIODINDAYS; i++){
                //If this box represents a day in the future
                if(i<=dayssinceresponsefortoday){
                    //If there are impressions on this day
                    if(ibdus.getImpressionsForParticularDay(i)>0){
                        daysthatqualify = daysthatqualify + 1;
                    }
                }
            }
            //Output the html
            for (int i = 0; i < MAXPOSTINGPERIODINDAYS; i++){
                String boxColor = "#cccccc";
                //If this box represents a day in the future
                if(i<=dayssinceresponsefortoday){
                    //If there are impressions on this day
                    if(ibdus.getImpressionsForParticularDay(i)>0){
                        boxColor = "#00ff00";
                    } else {
                        boxColor = "#ff0000";
                    }
                }
                //Override color and set to grey when posting limit has passed
                if (toolate && !response.getIspaid()){
                    boxColor = "#cccccc";
                }
                statusHtml.append("\t\t<td width=\"10\" bgcolor=\""+boxColor+"\" class=\"surveystatusbar\"><img src=\"/images/clear.gif\" width=\"1\" height=\"15\" border=\"0\"></td>\n");
            }
            statusHtml.append("\t\t<td width=\"10\" rowspan=\"2\" nowrap>");
            statusHtml.append("<center>");
            if (toolate && !response.getIspaid()){
                statusHtml.append("<img src=\"/images/delete-alt-16.png\" width=\"16\" height=\"16\" border=\"0\">");
                statusHtml.append("<br/>");
                statusHtml.append("<font class=\"smallfont\" style=\"color: #999999; font-weight: bold;\">Too Late</font>");
            } else {
                if (response.getIspaid()){
                    statusHtml.append("<img src=\"/images/ok-16.png\" width=\"16\" height=\"16\" border=\"0\">");
                    statusHtml.append("<br/>");
                    statusHtml.append("<font class=\"smallfont\" style=\"color: #999999; font-weight: bold;\">Paid</font>");
                } else {
                    statusHtml.append("<img src=\"/images/clock-16.png\" width=\"16\" height=\"16\" border=\"0\">");
                    statusHtml.append("<br/>");
                    statusHtml.append("<font class=\"smallfont\" style=\"color: #999999; font-weight: bold;\">Pending</font>");
                }
            }
            statusHtml.append("</center>");
            int moreneeded =  DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD-daysthatqualify;
            if (moreneeded<0){
                moreneeded = 0;
            }
            String daysqualify = "Days Qualify";
            if (daysthatqualify==1){
                daysqualify = "Day Qualifies";    
            }
            statusHtml.append("</td>\n");
            statusHtml.append(  "\t</tr>\n" +
                                "\t<tr>\n" +
                                "\t\t<td width=\"10\" colspan=\""+(MAXPOSTINGPERIODINDAYS)+"\" nowrap><font class=\"tinyfont\">"+daysthatqualify+" "+daysqualify+"; "+moreneeded+" More Needed</font></td>\n" +
                                "\t</tr>\n" +
                                "</table>");

                 
            //Store it in the database for the response
            try{
                if (daysthatqualify>=1){
                    response.setPoststatus(Response.POSTATUS_POSTEDATLEASTONCE);
                }
                if (daysthatqualify>=DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD){
                    response.setPoststatus(Response.POSTATUS_POSTED);
                }
                if (toolate && daysthatqualify<DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD){
                    response.setPoststatus(Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED);
                }
                if (response.getIspaid()){
                    response.setPoststatus(Response.POSTATUS_POSTED);
                }
                response.setResponsestatushtml(statusHtml.toString());
                response.save();
            }catch(Exception ex){logger.error("",ex);}

        } catch (Exception ex){
            logger.error("",ex);
        }
    }
}
