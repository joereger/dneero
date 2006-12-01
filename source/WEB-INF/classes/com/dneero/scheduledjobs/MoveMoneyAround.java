package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.MoveMoneyInRealWorld;
import com.dneero.util.Jsf;
import com.dneero.systemprops.InstanceProperties;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class MoveMoneyAround implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() MoveMoneyAround called");
            try{
                List users = HibernateUtil.getSession().createQuery("from User").list();
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    User user = (User) iterator.next();
                    double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);
                    if (currentbalance>0){
                        boolean dopay = true;
                        //Don't pay researchers if they have any open surveys
                        if (user.getResearcherid()>0){
                           List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+user.getResearcherid()+"' "+
                                                                                  " and ("+
                                                                                  "status='"+Survey.STATUS_OPEN+"'"+
                                                                                  "or status='"+Survey.STATUS_WAITINGFORFUNDS+"'"+
                                                                                  "or status='"+Survey.STATUS_WAITINGFORSTARTDATE+"'"+
                                                                                  "or status='"+Survey.STATUS_DRAFT+"'"+
                                                                                  ")").list();
                            if (surveys.size()==0){
                                dopay=false;
                            }
                        }
                        //Don't pay anything less than $25
                        if (currentbalance<25){
                            dopay=false;
                        }

                        //Go pay
                        if (dopay){
                            MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, currentbalance);
                            mmirw.move();
                        }

                    } else if (currentbalance<0){
                        //Need to collect from somebody
                        MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, currentbalance);
                        mmirw.move();
                    }
                }
            } catch (Exception ex){
                logger.debug("Error in top block.");
                logger.error(ex);
            }
        }

    }

}
