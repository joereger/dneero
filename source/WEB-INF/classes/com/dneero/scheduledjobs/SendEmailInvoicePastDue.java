package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.dao.Invoice;
import com.dneero.dao.Survey;
import com.dneero.dao.Researcher;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailTemplateProcessor;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SendEmailInvoicePastDue implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int DAYSALLOWEDTOPAY = 10;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() SendEmailInvoicePastDue called");


        Calendar cal = Time.xDaysAgoStart(Calendar.getInstance(), DAYSALLOWEDTOPAY);

        List<Invoice> invoices = HibernateUtil.getSession().createCriteria(Invoice.class)
                               .add( Restrictions.eq("status", Invoice.STATUS_NOTPAID))
                               .add( Restrictions.eq("status", Invoice.STATUS_PARTIALLYPAID))
                               .add( Restrictions.lt("enddate", cal.getTime()))
                               .list();

        for (Iterator<Invoice> iterator = invoices.iterator(); iterator.hasNext();) {
            Invoice invoice = iterator.next();

            //Send email telling researcher they are past due
            Researcher researcher = Researcher.get(invoice.getResearcherid());
            User user = User.get(researcher.getUserid());
            EmailTemplateProcessor.sendMail("dNeero Invoice Past Due Notification", "invoicepastdue", user);



        }








    }

}
