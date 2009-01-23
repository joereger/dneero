package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.sir.SocialInfluenceRating;


import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SiRRankUpdate implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SiRRankUpdate called");

//            if (true && 1==2){
//                List users = HibernateUtil.getSession().createQuery("from User order by sirpoints desc").list();
//                int i = 0;
//                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
//                    User user = (User)iterator.next();
//                    i = i + 1;
//                    user.setSirrank(i);
//                    try{user.save();}catch(Exception ex){logger.error("",ex);}
//                }
//            }


            if (true){
                Transaction tx = HibernateUtil.getSession().beginTransaction();
                List users = HibernateUtil.getSession().createQuery("from User order by sirpoints desc").list();
                int i = 0;
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    User user = (User)iterator.next();
                    i = i + 1;
                    user.setSirrank(i);
                    try{
                        HibernateUtil.getSession().saveOrUpdate(user);
                        //logger.debug("saved i="+i+" userid="+user.getUserid());
                        if ( i % 200 == 0 ) { //same as the JDBC batch size
                            //logger.debug("flushing");
                            HibernateUtil.getSession().flush();
                            //logger.debug("clearing");
                            HibernateUtil.getSession().clear();
                        }
                    }catch(Exception ex){logger.error("",ex);}
                }
                tx.commit();
            }




        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

}
