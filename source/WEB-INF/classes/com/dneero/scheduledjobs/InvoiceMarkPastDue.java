package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.verisign.Verisign;
import com.dneero.verisign.VerisignException;
import com.dneero.util.GeneralException;
import com.dneero.util.Time;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailSendThread;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class InvoiceMarkPastDue implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int DAYSALLOWEDTOPAY = 10;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() InvoiceMarkPastDue called");



        Calendar cal = Time.xDaysAgoStart(Calendar.getInstance(), DAYSALLOWEDTOPAY);

        List<Invoice> invoices = HibernateUtil.getSession().createCriteria(Invoice.class)
                               .add( Restrictions.eq("status", Invoice.STATUS_NOTPAID))
                               .add( Restrictions.eq("status", Invoice.STATUS_PARTIALLYPAID))
                               .add( Restrictions.lt("enddate", cal.getTime()))
                               .list();

        for (Iterator<Invoice> iterator = invoices.iterator(); iterator.hasNext();) {
            Invoice invoice = iterator.next();

            invoice.setStatus(Invoice.STATUS_PASTDUE);

            try{
                invoice.save();
            } catch (GeneralException gex){
                logger.error(gex);
            }

        }

    }

}
