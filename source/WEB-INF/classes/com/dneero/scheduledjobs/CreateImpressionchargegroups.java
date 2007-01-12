package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.systemprops.InstanceProperties;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CreateImpressionchargegroups implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() CreateImpressionchargegroups called +++++++++++++++++++++");

            List<Researcher> researchers = HibernateUtil.getSession().createQuery("from Researcher").list();

            for (Iterator<Researcher> iterator = researchers.iterator(); iterator.hasNext();) {
                Researcher researcher = iterator.next();
                logger.debug("Begin researcherid="+researcher.getResearcherid());

                List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                       .add( Restrictions.eq("researcherid", researcher.getResearcherid()))
                                       .list();
                for (Iterator<Survey> iterator1 = surveys.iterator(); iterator1.hasNext();) {
                    Survey survey = iterator1.next();

                    List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                                   .list();
                    for (Iterator<Impression> iterator2 = impressions.iterator(); iterator2.hasNext();) {
                        Impression impression = iterator2.next();

                        //Impressiondetails
                        List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                                                   .add( Restrictions.eq("impressionid", impression.getImpressionid()))
                                                   .add( Restrictions.le("impressionchargegroupid", 0))
                                                   .list();
                        logger.debug(impressiondetails.size() + " Impressiondetails found.");

                        //If we've found any impressions
                        if (impressiondetails.size()>0){
                            //Calculate amt, but only for those that qualify for payment
                            double amt = 0;
                            for (Iterator<Impressiondetail> iterator3 = impressiondetails.iterator(); iterator3.hasNext();) {
                                Impressiondetail impressiondetail = iterator3.next();
                                if (impressiondetail.getQualifiesforpaymentstatus()==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                                    amt = amt + (survey.getWillingtopaypercpm()/1000);
                                }
                            }

                            //Create impressionchargegroup
                            Impressionchargegroup impressionchargegroup = new Impressionchargegroup();
                            impressionchargegroup.setResearcherid(researcher.getResearcherid());
                            impressionchargegroup.setDate(new Date());
                            impressionchargegroup.setAmt(amt);
                            try{ impressionchargegroup.save(); } catch (Exception ex){logger.error(ex);}

                            //Mark all elements as tied to this impressionchargegroup
                            for (Iterator<Impressiondetail> iterator4 = impressiondetails.iterator(); iterator4.hasNext();) {
                                Impressiondetail impressiondetail = iterator4.next();
                                impressiondetail.setImpressionchargegroupid(impressionchargegroup.getImpressionchargegroupid());
                                try{ impressiondetail.save(); } catch (Exception ex){logger.error(ex);}
                            }

                            //If there's any amt to charge, charge it
                            if (amt>0){
                                //Apply the dNeero markup
                                double amttocharge = amt + (amt*(SurveyMoneyStatus.DNEEROMARKUPPERCENT/100));

                                //Update the account balance for the researcher
                                MoveMoneyInAccountBalance.charge(User.get(researcher.getUserid()), amttocharge, "Charge for blog impressions on survey '"+survey.getTitle()+"'", 0, impressionchargegroup.getImpressionchargegroupid());
                            }
                        }


                    }

                }






            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}
