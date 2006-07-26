package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Payblogger;
import com.dneero.dao.Invoice;
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

public class PaybloggerPendingToOwed implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() PaybloggerPendingToOwed called");

        List<Payblogger> paybloggers = HibernateUtil.getSession().createCriteria(Payblogger.class)
                               .add( Restrictions.eq("status", Payblogger.STATUS_PENDINGRESEARCHER))
                               .list();

        for (Iterator<Payblogger> iterator = paybloggers.iterator(); iterator.hasNext();) {
            Payblogger payblogger = iterator.next();

            Invoice invoice = Invoice.get(payblogger.getInvoiceid());
            if (invoice.getStatus()==Invoice.STATUS_PAID || invoice.getStatus()==Invoice.STATUS_WAIVED){
                payblogger.setStatus(Payblogger.STATUS_OWED);
                try{
                    payblogger.save();
                } catch (GeneralException ex){
                    logger.error(ex);
                }
            }


        }

    }

}
