package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.systemprops.BaseUrl;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Str;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class NotifyBloggersOfNewOffers implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() NotifyBloggersOfNewOffers called");

            List<Blogger> bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();

            for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
                Blogger blogger = iterator.next();
                blogger.refresh();
                if (blogger.getNotifyofnewsurveysbyemail()){
                    FindSurveysForBlogger finder = new FindSurveysForBlogger(blogger);
                    List allSurveys = finder.getSurveys();
                    ArrayList<Survey> newSurveys = new ArrayList<Survey>();
                    //See if any of the surveys are new since the last time the blogger was sent email
                    for (Iterator iterator1 = allSurveys.iterator(); iterator1.hasNext();) {
                        Survey survey = (Survey) iterator1.next();
                        if (survey.getStartdate().after(blogger.getLastnewsurveynotificationsenton())){
                            if (survey.getStartdate().before(new Date())){
                                if (survey.getStatus()==Survey.STATUS_OPEN){
                                    newSurveys.add(survey);
                                }
                            }
                        }
                    }
                    //If we have any new ones
                    if (newSurveys.size()>0){
                        //Vars to hold our strings in the email
                        double possibleearnings = 0;
                        StringBuffer listofsurveysHtml = new StringBuffer();
                        StringBuffer listofsurveysTxt = new StringBuffer();
                        //Iterate surveys
                        for (Iterator<Survey> iterator1 = newSurveys.iterator(); iterator1.hasNext();) {
                            Survey survey = iterator1.next();
                            SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                            possibleearnings = possibleearnings + surveyEnhancer.getMaxearningDbl();
                            String url = BaseUrl.get(false) + "publicsurveydetail.jsf?surveyid="+survey.getSurveyid();
                            listofsurveysHtml.append("<br><br><a href='"+url+"'>" + survey.getTitle() + " (Earn up to: " + surveyEnhancer.getMaxearning() + ")</a>");
                            listofsurveysTxt.append("\n\n" + survey.getTitle() + " (Earn up to: " + surveyEnhancer.getMaxearning()+")");
                            listofsurveysTxt.append("\n" + url);
                        }
                        //Create the args array to hold the dynamic stuff
                        String[] args = new String[10];
                        args[0] = "$"+Str.formatForMoney(possibleearnings);
                        args[1] = listofsurveysHtml.toString();
                        args[2] = listofsurveysTxt.toString();
                        //Send the email
                        User user = User.get(blogger.getUserid());
                        EmailTemplateProcessor.sendMail("New dNeero Surveys for "+user.getFirstname(), "bloggernotifyofnewsurveys", user, args);
                        //Update blogger last sent date
                        blogger.setLastnewsurveynotificationsenton(new Date());
                        try{blogger.save();}catch(Exception ex){logger.error(ex);}
                    }
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
