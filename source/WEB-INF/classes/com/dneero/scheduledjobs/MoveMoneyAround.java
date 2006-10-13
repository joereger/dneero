package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.MoveMoneyInRealWorld;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class MoveMoneyAround implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() MoveMoneyAround called");

        List users = HibernateUtil.getSession().createQuery("from User").list();
        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);
            if (currentbalance>0){
                //Need to pay somebody
                MoveMoneyInRealWorld.pay(user, currentbalance);
            } else if (currentbalance<0){
                //Need to collect from somebody
                MoveMoneyInRealWorld.charge(user, (-1)*currentbalance);
                //@todo Invoice paid status needs to be updated here
            }
        }


    }

}
