package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Userpersistentlogin;
import com.dneero.dao.Blog;
import com.dneero.dao.Blogger;
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

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SocialInfluenceRatingUpdate called");

            //Update all blog ratings
            if (true){
                List blogs = HibernateUtil.getSession().createQuery("from Blog").list();
                for (Iterator iterator = blogs.iterator(); iterator.hasNext();) {
                    Blog blog = (Blog)iterator.next();
                    blog.setSocialinfluencerating(SocialInfluenceRating.calculateSocialInfluenceRating(blog));
                    blog.setSocialinfluencerating90days(SocialInfluenceRating.calculateSocialInfluenceRating90days(blog));
                    try{blog.save();}catch(Exception ex){logger.error(ex);}
                }
            }

            //Update blog ranking numbers
            if (true){
                List blogs = HibernateUtil.getSession().createQuery("from Blog order by socialinfluencerating desc").list();
                int i = 0;
                for (Iterator iterator = blogs.iterator(); iterator.hasNext();) {
                    Blog blog = (Blog)iterator.next();
                    i = i + 1;
                    blog.setSocialinfluenceratingranking(i);
                    try{blog.save();}catch(Exception ex){logger.error(ex);}
                }
            }

            //Update blog ranking numbers 90 days
            if (true){
                List blogs = HibernateUtil.getSession().createQuery("from Blog order by socialinfluencerating90days desc").list();
                int i = 0;
                for (Iterator iterator = blogs.iterator(); iterator.hasNext();) {
                    Blog blog = (Blog)iterator.next();
                    i = i + 1;
                    blog.setSocialinfluenceratingranking90days(i);
                    try{blog.save();}catch(Exception ex){logger.error(ex);}
                }
            }
            
            //Update all blogger ratings
            if (true){
                List bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();
                for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
                    Blogger blogger = (Blogger)iterator.next();
                    blogger.setSocialinfluencerating(SocialInfluenceRating.calculateSocialInfluenceRating(blogger));
                    blogger.setSocialinfluencerating90days(SocialInfluenceRating.calculateSocialInfluenceRating90days(blogger));
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
