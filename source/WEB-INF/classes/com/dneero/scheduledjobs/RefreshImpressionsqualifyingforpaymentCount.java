package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Impression;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class RefreshImpressionsqualifyingforpaymentCount implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() RefreshImpressionsqualifyingforpaymentCount called");

        //This is NOT the primary mechanism for the accuracy of impression.impressionsqualifyingforpayment
        //The primary mechanism is at the time of impression storage (ImpressionActivityObjectStorage.java)

        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression").list();
        for (Iterator iterator = impressions.iterator(); iterator.hasNext();) {
            Impression impression = (Impression) iterator.next();
            //Count impressiondetails where the payment status is true
            int impressionsqualifyingforpayment = (Integer)HibernateUtil.getSession().createQuery("select count(*) from Impressiondetail where impressionid='"+impression.getImpressionid()+"' and qualifiesforpaymentstatus='"+ Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE+"'").uniqueResult();
            //Update if the number is off
            if (impressionsqualifyingforpayment!=impression.getImpressionsqualifyingforpayment()){
                impression.setImpressionsqualifyingforpayment(impressionsqualifyingforpayment);
                try{impression.save();} catch (GeneralException gex){logger.error(gex);}
            }
        }

    }

}
