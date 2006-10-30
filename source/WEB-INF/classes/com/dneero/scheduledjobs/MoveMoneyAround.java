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
        logger.debug("execute() MoveMoneyAround called");
        try{
            List users = HibernateUtil.getSession().createQuery("from User").list();
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                User user = (User) iterator.next();
                double currentbalance = CurrentBalanceCalculator.getCurrentBalance(user);
                if (currentbalance>0){
                    //Need to pay somebody as long as they don't have any open or pending surveys
                    if (user.getResearcher()!=null){
//                        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
//                               .add( Restrictions.eq("researcherid", user.getResearcher().getResearcherid()))
//                               .add( Restrictions.eq("status", Survey.STATUS_OPEN))
//                               .add( Restrictions.eq("status", Survey.STATUS_WAITINGFORFUNDS))
//                               .add( Restrictions.eq("status", Survey.STATUS_WAITINGFORSTARTDATE))
//                               .list();

                       List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+user.getResearcher().getResearcherid()+"' "+
                                                                              " and ("+
                                                                              "status='"+Survey.STATUS_OPEN+"'"+
                                                                              "or status='"+Survey.STATUS_WAITINGFORFUNDS+"'"+
                                                                              "or status='"+Survey.STATUS_WAITINGFORSTARTDATE+"'"+
                                                                              ")").list();
                       if (surveys.size()==0){
                            //Need to pay somebody
                            MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, currentbalance);
                            mmirw.move();
                       }
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
