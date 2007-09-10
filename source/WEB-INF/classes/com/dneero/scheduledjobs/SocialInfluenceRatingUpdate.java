package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Userpersistentlogin;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.session.PersistentLogin;
import com.dneero.sir.SocialInfluenceRating;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SocialInfluenceRatingUpdate implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SocialInfluenceRatingUpdate called");


            
            //Update all blogger ratings
            if (true){
                List bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();
                for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
                    Blogger blogger = (Blogger)iterator.next();
                    User user = User.get(blogger.getUserid());
                    blogger.setSocialinfluencerating(SocialInfluenceRating.calculateSocialInfluenceRating(user));
                    blogger.setSocialinfluencerating90days(SocialInfluenceRating.calculateSocialInfluenceRating90days(user));
                    try{blogger.save();}catch(Exception ex){logger.error(ex);}
                }
            }

            //Update blogger ranking numbers
            if (true){
                List bloggers = HibernateUtil.getSession().createQuery("from Blogger order by socialinfluencerating desc").list();
                int i = 0;
                for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
                    Blogger blogger = (Blogger)iterator.next();
                    i = i + 1;
                    blogger.setSocialinfluenceratingranking(i);
                    try{blogger.save();}catch(Exception ex){logger.error(ex);}
                }
            }

            //Update blogger ranking numbers 90 days
            if (true){
                List bloggers = HibernateUtil.getSession().createQuery("from Blogger order by socialinfluencerating90days desc").list();
                int i = 0;
                for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
                    Blogger blogger = (Blogger)iterator.next();
                    i = i + 1;
                    blogger.setSocialinfluenceratingranking90days(i);
                    try{blogger.save();}catch(Exception ex){logger.error(ex);}
                }
            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }


    }

}
