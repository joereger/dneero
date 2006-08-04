package com.dneero.invoice;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.Util;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 20, 2006
 * Time: 12:41:39 PM
 */
public class InvoiceCostCalculator {

    public static double getAmtBase(Invoice invoice){
        return getAmtBase(invoice, null);
    }

    public static double getAmtBase(Invoice invoice, Blogger blogger){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        double amtbase = 0;
        int responsesInInvoicePeriod = getResponsesInInvoicePeriod(invoice, blogger).size();
        logger.debug("responsesInInvoicePeriod="+responsesInInvoicePeriod);
        int impressionsInInvoicePeriod = getImpressionDetailsInInvoiceThatWillBePaid(invoice, blogger).size();
        logger.debug("impressionsInInvoicePeriod="+impressionsInInvoicePeriod);
        Survey survey = Survey.get(invoice.getSurveyid());
        if (survey!=null && survey.getSurveyid()>0){
            amtbase = (responsesInInvoicePeriod*survey.getWillingtopayperrespondent()) + ((impressionsInInvoicePeriod/1000)*survey.getWillingtopaypercpm());
            logger.debug("amtbase="+amtbase);
        }
        return amtbase;
    }

    public static ArrayList<Response> getResponsesInInvoicePeriod(Invoice invoice){
        return getResponsesInInvoicePeriod(invoice, null);
    }

    public static ArrayList<Response> getResponsesInInvoicePeriod(Invoice invoice, Blogger blogger){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        ArrayList<Response> responses;
        if (blogger!=null){
            responses = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                       .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
                       .add( Restrictions.eq("invoiceid", invoice.getInvoiceid()))
                       .list();
        } else {
            responses = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                       .add( Restrictions.gt("invoiceid", invoice.getInvoiceid()))
                       .list();
        }
        return responses;
    }

    public static ArrayList<Impressiondetail> getImpressionDetailsInInvoice(Invoice invoice){
        return getImpressionDetailsInInvoiceThatWillBePaid(invoice, null, true);
    }

    public static ArrayList<Impressiondetail> getImpressionDetailsInInvoiceThatWillBePaid(Invoice invoice, Blogger blogger){
        ArrayList<Impressiondetail> impressiondetails = new ArrayList<Impressiondetail>();
        for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
            Blog blog = iterator.next();
            impressiondetails.addAll(getImpressionDetailsInInvoiceThatWillBePaid(invoice, blog, true));
        }
        return impressiondetails;
    }

    public static ArrayList<Impressiondetail> getImpressionDetailsInInvoiceThatWillBePaid(Invoice invoice, Blog blog, boolean dummy){
        Logger logger = Logger.getLogger(InvoiceCostCalculator.class);
        ArrayList<Impressiondetail> out = new ArrayList<Impressiondetail>();
        ArrayList<Impressiondetail> impressiondetails = (ArrayList)HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                       .add( Restrictions.eq("invoiceid", invoice.getInvoiceid()))
                       .add( Restrictions.eq("qualifiesforpaymentstatus", Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE))
                       .list();
        for (Iterator<Impressiondetail> iterator = impressiondetails.iterator(); iterator.hasNext();) {
            Impressiondetail impressiondetail = iterator.next();
            Impression impression = Impression.get(impressiondetail.getImpressionid());
            if (blog==null || (blog!=null && impression.getBlog()!=null && impression.getBlog().getBlogid()==blog.getBlogid())){
                out.add(impressiondetail);
            }
        }
        return out;
    }


}
