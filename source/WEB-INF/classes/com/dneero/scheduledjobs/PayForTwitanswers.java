package com.dneero.scheduledjobs;

import com.dneero.systemprops.InstanceProperties;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class PayForTwitanswers implements Job {



    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() PayForTwitanswers called");


            //NOW HANDLED BY A USER CLICK ON bloggercompletedtwitasks.jsp


//            List<Twitanswer> twitanswers = HibernateUtil.getSession().createCriteria(Twitanswer.class)
//                                   .add(Restrictions.eq("status", Twitanswer.STATUS_APPROVED))
//                                   .add(Restrictions.eq("issysadminrejected", false))
//                                   .add(Restrictions.eq("iscriteriaxmlqualified", true))
//                                   .add(Restrictions.eq("ispaid", false))
//                                   .setCacheable(false)
//                                   .list();
//            logger.debug("twitanswers.size()="+ twitanswers.size());
//            for (Iterator<Twitanswer> iterator = twitanswers.iterator(); iterator.hasNext();) {
//                Twitanswer twitanswer= iterator.next();
//                logger.debug("twitanswer.getTwitanswerid()="+ twitanswer.getTwitanswerid());
//                try{
//                    //Survey survey = Survey.get(twitanswer.getSurveyid());
//                    User user = User.get(twitanswer.getUserid());
//                    try{
//                        //One extra check to make sure this isn't rejected
//                        if (!twitanswer.getIssysadminrejected()){
//                            //Award the incentive
//                            //Award the incentive
//                            //Award the incentive
//                            //Award the incentive
//                            logger.debug("awarding the incentive");
//                            twitanswer.getIncentive().doAwardIncentive(twitanswer);
//                            //Update paid status
//                            twitanswer.setIspaid(true);
//                            try{twitanswer.save();}catch(Exception ex){logger.error("",ex);}
//                            //@todo Notify user via email that they've been paid... thank them for their effort
//                            if (user.getEmail()!=null && !user.getEmail().equals("")){
//
//                            }
//                        } else {
//                            logger.debug("twitanswer.getIssysadminrejected()=true, not awarding");
//                        }
//                    } catch (Exception ex){
//                        logger.error("",ex);
//                        ex.printStackTrace();
//                        //Notify sales group
//                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "BIG ERROR IN PayForSurveyResponsesOncePosted:  "+ex.getMessage());
//                        xmpp.send();
//                        //Send error via email
//                        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Error paying "+user.getNickname()+" userid="+user.getUserid(), ErrorDissect.dissect(ex));
//                    }
//                } catch (Exception ex){
//                    logger.error("",ex);
//                    ex.printStackTrace();
//                }
//            }

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}