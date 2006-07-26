package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.verisign.Verisign;
import com.dneero.verisign.VerisignException;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class PaybloggerViaCreditCard implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() PaybloggerViaCreditCard called");

        List<Payblogger> paybloggers = HibernateUtil.getSession().createCriteria(Payblogger.class)
                               .add( Restrictions.eq("status", Payblogger.STATUS_OWED))
                               .add( Restrictions.eq("status", Payblogger.STATUS_PAYERROR))
                               .list();

        for (Iterator<Payblogger> iterator = paybloggers.iterator(); iterator.hasNext();) {
            Payblogger payblogger = iterator.next();

            //Find any existing payments
            double paidtodate = 0;
            for (Iterator<Paybloggertransaction> iterator1 = payblogger.getPaybloggertransactions().iterator(); iterator1.hasNext();){
                Paybloggertransaction paybloggertransaction = iterator1.next();
                if (paybloggertransaction.getIssuccessful()){
                    paidtodate = paidtodate + paybloggertransaction.getAmt();
                }
            }

            //Calculate amount due now
            double amtdue = payblogger.getAmt() - paidtodate;

            //Get billing details
            Blogger blogger = Blogger.get(payblogger.getBloggerid());
            Bloggerbilling bloggerbilling = blogger.getBloggerbilling();
            if (bloggerbilling!=null){

                //Charge the card
                boolean successful = false;
                String notes = "";

                try{
                    Verisign vs = new Verisign();
                    notes = vs.chargeCard(amtdue, bloggerbilling.getCcnum(), bloggerbilling.getCcexpmonth(), bloggerbilling.getCcexpyear(), bloggerbilling.getBillingaddress1(), bloggerbilling.getBillingzip());
                    //@todo i don't believe successful is always set correctly.  a bad ccnum will not throw VerisignError, right?
                    successful = true;
                } catch (VerisignException vex){
                    logger.debug(vex.errorMessage);
                }

                //Record transaction
                Paybloggertransaction it = new Paybloggertransaction();
                it.setPaybloggerid(payblogger.getPaybloggerid());
                it.setAmt(amtdue);
                it.setIssuccessful(successful);
                it.setTransactiondate(new Date());
                it.setNotes(notes);

                payblogger.getPaybloggertransactions().add(it);

                try{
                    it.save();
                } catch (GeneralException gex){
                    logger.error(gex);
                }

            } else {
                //No billing info on file
                payblogger.setStatus(Payblogger.STATUS_PAYERROR);
                try{
                    payblogger.save();
                } catch (GeneralException gex){
                    logger.error(gex);
                }

                Paybloggertransaction it = new Paybloggertransaction();
                it.setPaybloggerid(payblogger.getPaybloggerid());
                it.setAmt(amtdue);
                it.setIssuccessful(false);
                it.setTransactiondate(new Date());
                it.setNotes("Blogger does not have billing information on file.");

                payblogger.getPaybloggertransactions().add(it);

                try{
                    it.save();
                } catch (GeneralException gex){
                    logger.error(gex);
                }
            }


        }

    }

}
