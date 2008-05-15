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
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Time;
import com.dneero.util.ErrorDissect;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.email.EmailTemplateProcessor;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;

/**
 * Reconciles discrepancies between in-system balance and the real world.
 */

public class MoveMoneyAround implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() MoveMoneyAround called");
            StringBuffer debug = new StringBuffer();
            try{
                List users = HibernateUtil.getSession().createQuery("from User").list();
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    User user = (User) iterator.next();
                    logger.debug("===");
                    logger.debug("Start User "+user.getUserid()+" "+user.getFirstname()+" "+user.getLastname());

                    //Handle Blogger Balances, only check User obj for performance
                    if (user.getCurrentbalanceblogger()>0){
                        if (user.getCurrentbalanceblogger()>=20){
                            //Now the expensive realtime calculation, just to be sure
                            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
                            if (cbc.getCurrentbalanceblogger()>0){
                                if (cbc.getCurrentbalanceblogger()>=20){
                                    logger.debug("dopay=true so calling MoveMoneyInRealWorld for user");
                                    debug.append("<br/>$"+cbc.getCurrentbalanceblogger()+" to "+user.getFirstname()+" "+user.getLastname()+" ("+user.getEmail()+")"+"\n\n");
                                    MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, cbc.getCurrentbalanceblogger(), false, true, false, false);
                                    mmirw.move();
                                }
                            }
                        }
                    } else if (user.getCurrentbalanceblogger()<0){
                        //How does a blogger balance go less than zero?
                        logger.error("user has negative balanceblogger... how does that happen? userid="+user.getUserid()+" user.getCurrentbalanceblogger()="+user.getCurrentbalanceblogger());
                        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Currentbalanceblogger<0", "user has negative balanceblogger... how does that happen? userid="+user.getUserid()+" user.getCurrentbalanceblogger()="+user.getCurrentbalanceblogger());
                    }

                    //Handle Researcher Balances, only looking at User first for performance
                    if (user.getCurrentbalanceresearcher()>0){
                        //Don't pay researchers if they have any open surveys or closed surveys where we can still collect impression revenue
                        boolean dopayresearcher = true;
                        if (user.getResearcherid()>0){
                            //Now the expensive realtime calculation, just to be sure
                            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
                            if (cbc.getCurrentbalanceresearcher()>0){
                                Calendar closeDateForSurveysNoLongerCollectingImpressionRevenue = Time.xDaysAgoEnd(Calendar.getInstance(), SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS);
                                logger.debug("researcherid="+user.getResearcherid());
                                logger.debug("closeDateForSurveysNoLongerCollectingImpressionRevenue="+Time.dateformatfordb(closeDateForSurveysNoLongerCollectingImpressionRevenue));
                                List surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+user.getResearcherid()+"' "+
                                                                                      " and ("+
                                                                                      "status='"+Survey.STATUS_OPEN+"'"+
                                                                                      "or status='"+Survey.STATUS_WAITINGFORFUNDS+"'"+
                                                                                      "or status='"+Survey.STATUS_WAITINGFORSTARTDATE+"'"+
                                                                                      "or status='"+Survey.STATUS_DRAFT+"'"+
                                                                                      "or (status='"+Survey.STATUS_CLOSED+"' and enddate>'"+Time.dateformatfordb(closeDateForSurveysNoLongerCollectingImpressionRevenue)+"')" +
                                                                                      ")").list();
                                if (surveys.size()>0){
                                    logger.debug("setting dopay=false because surveys were found to be active or closed but not ending before "+Time.dateformatfordb(closeDateForSurveysNoLongerCollectingImpressionRevenue));
                                    dopayresearcher=false;
                                }
                                if (dopayresearcher){
                                    logger.debug("dopayresearcher=true so calling MoveMoneyInRealWorld for user");
                                    debug.append("<br/>$"+cbc.getCurrentbalanceresearcher()+" to "+user.getFirstname()+" "+user.getLastname()+" ("+user.getEmail()+")"+"\n\n");
                                    MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, cbc.getCurrentbalanceresearcher(), true, false, false, false);
                                    mmirw.move();
                                }
                            }
                        }
                    } else if (user.getCurrentbalanceresearcher()<0){
                        //Now the expensive realtime calculation, just to be sure
                            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
                            if (cbc.getCurrentbalanceresearcher()<0){
                                //Need to collect from a researcher
                                logger.debug("currentbalance<0 so calling MoveMoneyInRealWorld for user");
                                debug.append("<br/>$"+cbc.getCurrentbalanceresearcher()+" from "+user.getFirstname()+" "+user.getLastname()+" ("+user.getEmail()+")"+"\n\n");
                                MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(user, cbc.getCurrentbalanceresearcher(), true, false, false, false);
                                mmirw.move();
                            }
                    }

                    logger.debug("End User "+user.getUserid()+" "+user.getFirstname()+" "+user.getLastname());
                    logger.debug("===");
                }
            } catch (Exception ex){
                logger.debug("Error in top block.");
                logger.error("",ex);
                SendXMPPMessage xmpp2 = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "Error in MoveMoneyAround.java: "+ex.getMessage());
                xmpp2.send();
                EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "Error in MoveMoneyAround", ErrorDissect.dissect(ex));
            }
            EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "MoveMoneyAround Scheduled Task Report", Time.dateformatcompactwithtime(Time.nowInUserTimezone("EST"))+"<br/><br>\n\n"+debug.toString());
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }

    }

}
