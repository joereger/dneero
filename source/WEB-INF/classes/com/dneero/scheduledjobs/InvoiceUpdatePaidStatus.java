package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.dao.Invoice;
import com.dneero.money.TotalSpentCalculator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */

public class InvoiceUpdatePaidStatus implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() InvoiceUpdatePaidStatus called");
        try{
            List users = HibernateUtil.getSession().createQuery("from User").list();
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                User user = (User) iterator.next();
                if (user.getResearcher()!=null){
                    double totalspent = TotalSpentCalculator.getTotalSpent(user);
                    double tmpbal = totalspent;
                    List invoices = HibernateUtil.getSession().createQuery("from Invoice where researcherid='"+user.getResearcher().getResearcherid()+"' order by startdate asc").list();
                    for (Iterator iterator1 = invoices.iterator(); iterator1.hasNext();) {
                        Invoice invoice = (Invoice) iterator1.next();
                        if(tmpbal>invoice.getAmttotal()){
                            //This invoice has been paid
                            if (invoice.getStatus()!=Invoice.STATUS_PAID){
                                invoice.setStatus(Invoice.STATUS_PAID);
                                invoice.setAmtpaidtodate(invoice.getAmttotal());
                                try{invoice.save();}catch(Exception ex){logger.error(ex);}
                                //Update tmpbal
                                tmpbal = tmpbal - invoice.getAmttotal();
                            }
                        } else {
                            //The invoice has not been fully paid but it may be partially paid
                            if (tmpbal>0){
                                //It has been partially paid
                                if (invoice.getStatus()!=Invoice.STATUS_PARTIALLYPAID){
                                    invoice.setStatus(Invoice.STATUS_PARTIALLYPAID);
                                    invoice.setAmtpaidtodate(tmpbal);
                                    try{invoice.save();}catch(Exception ex){logger.error(ex);}
                                    //Update tmpbal
                                    tmpbal=0;
                                }
                            } else {
                                //It has not been paid
                                if (invoice.getStatus()!=Invoice.STATUS_NOTPAID){
                                    invoice.setStatus(Invoice.STATUS_NOTPAID);
                                    invoice.setAmtpaidtodate(0);
                                    try{invoice.save();}catch(Exception ex){logger.error(ex);}
                                    //Update tmpbal
                                    tmpbal=0;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex){
            logger.debug("Error in InvoiceUpdatePaidStatus.");
            logger.error(ex);
            ex.printStackTrace();
        }

    }

}
