package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.verisign.Verisign;
import com.dneero.verisign.VerisignException;
import com.dneero.util.GeneralException;
import com.dneero.paypal.Pay;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class PaybloggerSendMoney implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() PaybloggerSendMoney called");

        //Get a list of bloggers who need to be paid
//        List<Payblogger> allpaybloggers = HibernateUtil.getSession().createCriteria(Payblogger.class)
//                               .add( Restrictions.eq("status", Payblogger.STATUS_OWED))
//                               .add( Restrictions.eq("status", Payblogger.STATUS_PAYERROR))
//                               .list();
        List allpaybloggers = HibernateUtil.getSession().createQuery("from Payblogger where status='"+Payblogger.STATUS_OWED+"' OR status='"+Payblogger.STATUS_PAYERROR+"'").list();
        ArrayList<Integer> bloggerids = new ArrayList<Integer>();
        for (Iterator<Payblogger> iterator = allpaybloggers.iterator(); iterator.hasNext();) {
            Payblogger payblogger = iterator.next();
            bloggerids.add(payblogger.getBloggerid());
        }

        //Iterate the list of bloggers who need to be paid
        for (Iterator it = bloggerids.iterator(); it.hasNext(); ) {
            int bloggerid = (Integer)it.next();
            Blogger blogger = Blogger.get(bloggerid);
            double amtduetotal = 0;

            //List this blogger's payblogger records that are unpaid
//            List<Payblogger> paybloggers = HibernateUtil.getSession().createCriteria(Payblogger.class)
//                               .add( Restrictions.eq("bloggerid", blogger.getBloggerid()))
//                               .add( Restrictions.eq("status", Payblogger.STATUS_OWED))
//                               .add( Restrictions.eq("status", Payblogger.STATUS_PAYERROR))
//                               .list();
            List paybloggers = HibernateUtil.getSession().createQuery("from Payblogger where bloggerid='"+blogger.getBloggerid()+"' and (status='"+Payblogger.STATUS_OWED+"' OR status='"+Payblogger.STATUS_PAYERROR+"')").list();
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

                //Calculate amount due now for this one payment element
                double amtdue = payblogger.getAmt() - paidtodate;
                amtduetotal = amtduetotal + amtdue;
                //@todo if we have a paypal address
                if (!User.get(blogger.getUserid()).getPaypaladdress().equals("")){

                    //Charge the card
                    boolean successful = false;
                    String notes = "";

                    try{
                        Pay.pay(User.get(Blogger.get(payblogger.getBloggerid()).getUserid()).getPaypaladdress(), amtduetotal);
                        //@todo i don't believe successful is always set correctly.  a bad ccnum will not throw VerisignError, right?
                        successful = true;
                    } catch (Exception vex){
                        logger.error(vex);
                    }

                    //Record transaction
                    Paybloggertransaction pbt = new Paybloggertransaction();
                    pbt.setPaybloggerid(payblogger.getPaybloggerid());
                    pbt.setAmt(amtdue);
                    //@todo with paypal need to call later on for success
                    pbt.setIssuccessful(successful);
                    pbt.setTransactiondate(new Date());
                    pbt.setNotes(notes);

                    payblogger.getPaybloggertransactions().add(pbt);

                    try{
                        pbt.save();
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

                    Paybloggertransaction pbt = new Paybloggertransaction();
                    pbt.setPaybloggerid(payblogger.getPaybloggerid());
                    pbt.setAmt(amtdue);
                    pbt.setIssuccessful(false);
                    pbt.setTransactiondate(new Date());
                    pbt.setNotes("Blogger does not have billing information on file.");

                    payblogger.getPaybloggertransactions().add(pbt);

                    try{
                        pbt.save();
                    } catch (GeneralException gex){
                        logger.error(gex);
                    }
                }
            }




        }







    }

}
