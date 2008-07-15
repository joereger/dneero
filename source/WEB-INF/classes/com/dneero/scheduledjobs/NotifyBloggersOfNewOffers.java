package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.systemprops.BaseUrl;
import com.dneero.systemprops.SystemProperty;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class NotifyBloggersOfNewOffers implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() NotifyBloggersOfNewOffers called");
            StringBuffer debug = new StringBuffer();

            List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

            for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
                Blogger blogger = iterator.next();
                User user = User.get(blogger.getUserid());
                debug.append("<br/><br/>userid="+user.getUserid()+" bloggerid="+blogger.getBloggerid()+" name="+user.getFirstname()+" "+user.getLastname());
                logger.debug("userid="+user.getUserid()+" bloggerid="+blogger.getBloggerid()+" name="+user.getFirstname()+" "+user.getLastname());

                if (user.getIsenabled()){
                    boolean shouldSendUserNotifications = false;
                    if (user.getNotifyofnewsurveysbyemaileveryexdays()>0){
                        Calendar lastsenton = Time.getCalFromDate(user.getNotifyofnewsurveyslastsent());
                        int dayssincelastsend = DateDiff.dateDiff("day", Calendar.getInstance(), lastsenton);
                        debug.append("<br/>dayssincelastsend="+dayssincelastsend+" user.getNotifyofnewsurveysbyemaileveryexdays()="+user.getNotifyofnewsurveysbyemaileveryexdays());
                        logger.debug("dayssincelastsend="+dayssincelastsend+" user.getNotifyofnewsurveysbyemaileveryexdays()="+user.getNotifyofnewsurveysbyemaileveryexdays());
                        if (dayssincelastsend>=user.getNotifyofnewsurveysbyemaileveryexdays()){
                            shouldSendUserNotifications = true;
                        }
                    } else {
                        debug.append("<br/>user.getNotifyofnewsurveysbyemaileveryexdays() less than or equal to zero");
                        logger.debug("user.getNotifyofnewsurveysbyemaileveryexdays() less than or equal to zero");
                    }
                    debug.append("<br/>shouldSendUserNotifications="+shouldSendUserNotifications+" after date check");
                    logger.debug("shouldSendUserNotifications="+shouldSendUserNotifications+" after date check");
                    if (shouldSendUserNotifications){
                        FindSurveysForBlogger finder = new FindSurveysForBlogger(blogger);
                        List allSurveys = finder.getSurveys();
                        ArrayList<Survey> newSurveys = new ArrayList<Survey>();
                        //See if any of the surveys are new since the last time the blogger was sent email
                        for (Iterator iterator1 = allSurveys.iterator(); iterator1.hasNext();) {
                            Survey survey = (Survey) iterator1.next();
                            if (survey.getStartdate().after(user.getNotifyofnewsurveyslastsent())){
                                if (survey.getStartdate().before(new Date())){
                                    if (survey.getStatus()==Survey.STATUS_OPEN){
                                        if (!survey.getIsaccesscodeonly()){
                                            newSurveys.add(survey);
                                        } else {
                                            debug.append("<br/>!survey.getIsaccesscodeonly()");
                                            logger.debug("!survey.getIsaccesscodeonly()");
                                        }
                                    } else {
                                        debug.append("<br/>!survey.getStatus()==Survey.STATUS_OPEN");
                                        logger.debug("!survey.getStatus()==Survey.STATUS_OPEN");
                                    }
                                } else {
                                    debug.append("<br/>!survey.getStartdate().before(new Date())");
                                    logger.debug("!survey.getStartdate().before(new Date())");
                                }
                            } else {
                                debug.append("<br/>!survey.getStartdate().after(user.getNotifyofnewsurveyslastsent())");
                                logger.debug("!survey.getStartdate().after(user.getNotifyofnewsurveyslastsent())");
                            }
                        }
                        //If we have any new ones
                        if (newSurveys.size()>0){
                            //Vars to hold our strings in the email
                            boolean atleastonenewsurveyforblogger = false;
                            double possibleearnings = 0;
                            StringBuffer listofsurveysHtml = new StringBuffer();
                            StringBuffer listofsurveysTxt = new StringBuffer();
                            //Iterate surveys
                            for (Iterator<Survey> iterator1 = newSurveys.iterator(); iterator1.hasNext();) {
                                Survey survey = iterator1.next();
                                boolean bloggerhastakensurvey = false;
                                for (Iterator<Response> iterator2 = blogger.getResponses().iterator(); iterator2.hasNext();) {
                                    Response response = iterator2.next();
                                    if (response.getSurveyid()==survey.getSurveyid()){
                                        bloggerhastakensurvey = true;
                                    }
                                }
                                debug.append("<br/>bloggerhastakensurvey="+bloggerhastakensurvey);
                                logger.debug("bloggerhastakensurvey="+bloggerhastakensurvey);
                                if (!bloggerhastakensurvey){
                                    atleastonenewsurveyforblogger = true;
                                    SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                                    possibleearnings = possibleearnings + surveyEnhancer.getMaxearningDbl();
                                    String url = BaseUrl.get(false, survey.getPlid()) + "survey.jsp?surveyid="+survey.getSurveyid();
                                    if (user.getFacebookuserid()>0){
                                        url = "http://apps.facebook.com/"+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/";
                                    }
                                    //Html
                                    listofsurveysHtml.append("<br><br><a href=\""+url+"\"><font style=\"font-size: 14px;\"><b>" + survey.getTitle() + "</b> (Earn up to: " + surveyEnhancer.getMaxearning() + ")</font></a>");
                                    if (!survey.getDescription().equals("")){
                                        listofsurveysHtml.append("<br><font style=\"font-size: 9px;\">"+survey.getDescription()+"</font>");
                                    }
                                    //Txt
                                    listofsurveysTxt.append("\n\n" + survey.getTitle() + " (Earn up to: " + surveyEnhancer.getMaxearning()+")");
                                    if (!survey.getDescription().equals("")){
                                        listofsurveysTxt.append("\n" + survey.getDescription());
                                    }
                                    listofsurveysTxt.append("\n" + url);
                                }
                            }
                            //Collect number of people who've answered this person's last user question
                            StringBuffer userquestionsansweredHtml = new StringBuffer();
                            StringBuffer userquestionsansweredTxt = new StringBuffer();
                            if (atleastonenewsurveyforblogger){
                                try{
                                    int questionsAnswered = NumFromUniqueResult.getInt("select count(*) from Question as question left join question.questionresponses as questionresponses where question.userid='"+user.getUserid()+"' and questionresponses.response.responsedate>'"+Time.dateformatfordb(Time.getCalFromDate(user.getNotifyofnewsurveyslastsent()))+"'");
                                    if (questionsAnswered>0){
                                        userquestionsansweredHtml.append("<br/><br/>"+questionsAnswered+" people answered your questions ");   
                                        userquestionsansweredTxt.append(questionsAnswered + " people answered your questions ");
                                        int dayssincelastsend = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(user.getNotifyofnewsurveyslastsent()));
                                        if (dayssincelastsend<=0){

                                        } else if (dayssincelastsend==1){
                                            userquestionsansweredHtml.append("since yesterday");
                                            userquestionsansweredTxt.append("since yesterday");
                                        } else if (dayssincelastsend>1){
                                            userquestionsansweredHtml.append("in the last "+dayssincelastsend+" days");
                                            userquestionsansweredTxt.append("in the last "+dayssincelastsend+" days");
                                        }
                                    }
                                } catch (Exception ex){
                                    logger.error("", ex);
                                }
                            }
                            //Do the sending
                            debug.append("<br/>atleastonenewsurveyforblogger="+atleastonenewsurveyforblogger);
                            logger.debug("atleastonenewsurveyforblogger="+atleastonenewsurveyforblogger);
                            if (atleastonenewsurveyforblogger){
                                //Create the args array to hold the dynamic stuff
                                String[] args = new String[10];
                                args[0] = "$"+Str.formatForMoney(possibleearnings);
                                args[1] = listofsurveysHtml.toString();
                                args[2] = listofsurveysTxt.toString();
                                args[3] = userquestionsansweredHtml.toString();
                                args[4] = userquestionsansweredTxt.toString();
                                //Send the email
                                EmailTemplateProcessor.sendMail("New dNeero Conversations for "+user.getFirstname(), "bloggernotifyofnewsurveys", user, args);
                                //Update blogger last sent date
                                user.setNotifyofnewsurveyslastsent(new Date());
                                try{user.save();}catch(Exception ex){logger.error("",ex);}
                            }
                        } else {
                            debug.append("<br/>No new surveys for this user");
                            logger.debug("No new surveys for this user");
                        }
                    }
                }
            }

            EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "NotifyBloggersOfNewOffers Daily Report", debug.toString());
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
