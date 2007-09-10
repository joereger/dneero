package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CharityCalculateAmountDonated implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CharityCalculateAmountDonated called");
             List users = HibernateUtil.getSession().createQuery("from User").list();
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    try{
                        User user = (User)iterator.next();
                        double charityamtdonatedOriginal = user.getCharityamtdonated();
                        double sum = ((Double)HibernateUtil.getSession().createQuery("select sum(amt) from Charitydonation where userid='"+user.getUserid()+"'").uniqueResult()).doubleValue();
                        logger.debug("user name="+user.getFirstname()+" "+user.getLastname());
                        logger.debug("charityamtdonatedOriginal="+charityamtdonatedOriginal);
                        logger.debug("sum="+sum);
                        if (charityamtdonatedOriginal<sum || charityamtdonatedOriginal>sum){
                            logger.debug("user.setCharityamtdonated("+sum+")");
                            user.setCharityamtdonated(sum);
                            try{user.save();}catch(Exception ex){logger.error(ex);}
                        } else {
                            logger.debug("not updating because charityamtdonatedOriginal appears identical to sum");
                        }
                    } catch (Exception ex){
                        logger.debug(ex);
                    }
                }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
