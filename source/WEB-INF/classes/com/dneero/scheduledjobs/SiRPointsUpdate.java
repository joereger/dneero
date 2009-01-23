package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.sir.SocialInfluenceRating;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailTemplateProcessor;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SiRPointsUpdate implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SiRPointsUpdate called");
            if (true){
                List users = HibernateUtil.getSession().createQuery("from User order by sirdate asc").setMaxResults(1000).list();
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    User user = (User)iterator.next();
                    try{
                        double points = SocialInfluenceRating.calculateSocialInfluenceRating(user);
                    } catch (Exception ex){
                        logger.error("", ex);
                    }
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }


    }

}