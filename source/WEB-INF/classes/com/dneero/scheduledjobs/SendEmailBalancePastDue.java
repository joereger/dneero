package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import com.dneero.util.Time;
import com.dneero.dao.Invoice;
import com.dneero.dao.Researcher;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailTemplateProcessor;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SendEmailBalancePastDue implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private int DAYSALLOWEDTOPAY = 10;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() SendEmailBalancePastDue called");


        Calendar cal = Time.xDaysAgoStart(Calendar.getInstance(), DAYSALLOWEDTOPAY);

        //@todo Implement SendEmailBalancePastDue

        //EmailTemplateProcessor.sendMail("dNeero Invoice Past Due Notification", "invoicepastdue", user);








    }

}
