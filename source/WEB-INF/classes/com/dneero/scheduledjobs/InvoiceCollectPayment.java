package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.verisign.Verisign;
import com.dneero.verisign.VerisignException;
import com.dneero.paypal.Charge;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class InvoiceCollectPayment implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() InvoiceCollectPayment called");

        List<Invoice> invoices = HibernateUtil.getSession().createCriteria(Invoice.class)
                               .add( Restrictions.ne("status", Invoice.STATUS_PAID))
                               .list();

        for (Iterator<Invoice> iterator = invoices.iterator(); iterator.hasNext();) {
            Invoice invoice = iterator.next();

            //Find any existing payments
            double paidtodate = 0;
            for (Iterator<Invoicetransaction> iterator1 = invoice.getInvoicetransactions().iterator(); iterator1.hasNext();){
                Invoicetransaction invoicetransaction = iterator1.next();
                if (invoicetransaction.getIssuccessful()){
                    paidtodate = paidtodate + invoicetransaction.getAmt();
                }
            }

            //Calculate amount due now
            double amtdue = invoice.getAmttotal() - paidtodate;
            logger.debug("paidtodate="+paidtodate+" invoice.getAmttotal()="+invoice.getAmttotal()+" amtdue="+amtdue);

            //Get billing details
            Researcher researcher = Researcher.get(invoice.getResearcherid());


            //Charge the card
            boolean successful = false;
            String notes = "";

            if (amtdue>0){
                try{
                    Charge.charge(User.get(researcher.getUserid()).getPaypaladdress(), amtdue);
                    //@todo i don't believe successful is always set correctly.  a bad ccnum will not throw VerisignError, right?
                    successful = true;
                } catch (Exception vex){
                    logger.error(vex);
                    vex.printStackTrace();
                }

                //Record transaction
                Invoicetransaction it = new Invoicetransaction();
                it.setInvoiceid(invoice.getInvoiceid());
                it.setAmt(amtdue);
                //@todo with paypal need to check back for payment status
                it.setIssuccessful(successful);
                it.setTransactiondate(new Date());
                it.setNotes(notes);

                invoice.getInvoicetransactions().add(it);

                try{
                    it.save();
                } catch (GeneralException gex){
                    logger.error(gex);
                }

                if (successful){
                    invoice.setStatus(Invoice.STATUS_PAID);
                    try{invoice.save();} catch (GeneralException gex){logger.error(gex);}
                }
            } else {
                invoice.setStatus(Invoice.STATUS_PAID);
                try{invoice.save();} catch (GeneralException gex){logger.error(gex);}
            }

        }

    }

}