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

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SendEmailInvoicePastDue implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() SendEmailInvoicePastDue called");



        List<Invoice> invoices = HibernateUtil.getSession().createCriteria(Invoice.class)
                               .add( Restrictions.eq("status", Invoice.STATUS_PASTDUE))
                               .list();

        for (Iterator<Invoice> iterator = invoices.iterator(); iterator.hasNext();) {
            Invoice invoice = iterator.next();


            //Send email telling researcher they are past due
            Survey survey = Survey.get(invoice.getSurveyid());
            Researcher researcher = Researcher.get(survey.getResearcherid());
            User user = User.get(researcher.getUserid());
            user.getEmail();

            try{
                HtmlEmail email = new HtmlEmail();
                String message = "You have a dNeero invoice that is now past due.  Bloggers are waiting on your payment.  Please log in to your dNeero account and pay.  Thanks, The dNeero Team";
                email.addTo(user.getEmail());
                email.setFrom(EmailSendThread.DEFAULTFROM);
                email.setSubject("dNeero Invoice Past Due Notification");
                email.setHtmlMsg("<html><font face=arial size=+1 color=#00ff00>"+message+"</font></html>");
                email.setTextMsg(message);
                EmailSend.sendMail(email);
            } catch (Exception e){
                logger.error(e);
            }


        }

    }

}
