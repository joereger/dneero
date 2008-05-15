package com.dneero.scheduledjobs;

import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.systemprops.InstanceProperties;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CurrentBalanceUpdater implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CurrentBalanceUpdater called");
             List users = HibernateUtil.getSession().createQuery("from User").list();
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    try{
                        User user = (User)iterator.next();
                        processUser(user);
                    } catch (Exception ex){
                        logger.debug(ex);
                    }
                }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

    public static void processUser(User user){
        Logger logger = Logger.getLogger(CurrentBalanceUpdater.class);
        try{
            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
            user.setCurrentbalance(cbc.getCurrentbalance());
            user.setCurrentbalanceblogger(cbc.getCurrentbalanceblogger());
            user.setCurrentbalanceresearcher(cbc.getCurrentbalanceresearcher());
            user.save();
        } catch (Exception ex){
            logger.debug(ex);
        }
    }

}