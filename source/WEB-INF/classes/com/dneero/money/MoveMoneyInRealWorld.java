package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import com.dneero.threadpool.ThreadPool;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.util.Time;
import com.dneero.util.ErrorDissect;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:41:48 PM
 */
public class MoveMoneyInRealWorld implements Runnable {

    public static double GLOBALMAXCHARGEPERTRANSACTION = 10000;

    private static ThreadPool tp;

    private User user;
    private double amttogiveuser;

    public MoveMoneyInRealWorld(User user, double amttogiveuser){
        this.user = user;
        this.amttogiveuser = amttogiveuser;
    }

    public void run(){
        giveUserThisAmt();
    }

    public void move(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }

    private void giveUserThisAmt(){
        Logger logger = Logger.getLogger(MoveMoneyInRealWorld.class);

        //See if this needs to be broken into multiple transactions
        double amtremainder = 0;
        if (amttogiveuser>0){
            if (amttogiveuser>GLOBALMAXCHARGEPERTRANSACTION){
                amtremainder = amttogiveuser - GLOBALMAXCHARGEPERTRANSACTION;
                amttogiveuser = GLOBALMAXCHARGEPERTRANSACTION;
            }
        } else if (amttogiveuser<0){
            if (((-1)* amttogiveuser)>GLOBALMAXCHARGEPERTRANSACTION){
                amtremainder = amttogiveuser + GLOBALMAXCHARGEPERTRANSACTION;
                amttogiveuser = (-1)*GLOBALMAXCHARGEPERTRANSACTION;
            }
        } else {
            logger.debug("amttogiveuser=0 for userid="+user.getUserid());
            return;
        }

        //Get the payment method class based on the user's settings
        PaymentMethod pm = new PaymentMethodCreditCard(user, amttogiveuser);
        String desc = "Charge.";
        int paymentmethod = 0;
        if (amttogiveuser>.01){
            //Paying user
            if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
                pm = new PaymentMethodCreditCard(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODCREDITCARD;
                desc = "Put Money Onto Credit Card.";
            } else if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
                pm = new PaymentMethodPayPal(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODPAYPAL;
                desc = "Transfer to PayPal account.";
            }
        } else if (amttogiveuser<((-1)*.01)){
            //Charging user
            if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
                pm = new PaymentMethodCreditCard(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODCREDITCARD;
                desc = "Charged Credit Card.";
            } else if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
                pm = new PaymentMethodPayPal(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODPAYPAL;
                desc = "Transfer from PayPal account.";
            }
        } else {
            //Set to zero because we're talking about less than a cent
            amttogiveuser = 0;
        }

        if(user!=null && (amttogiveuser<0 || amttogiveuser>0)){

            //Do the transaction
            pm.giveUserThisAmt();

            //Only affect the account balance if the real-world transaction was successful
            if (pm.getIssuccessful()){
                Balance balance = new Balance();
                balance.setAmt((-1)*amttogiveuser);
                balance.setDate(new Date());
                balance.setDescription(desc);
                CurrentBalanceCalculator cbc;
                try{
                    cbc = new CurrentBalanceCalculator(user);
                    balance.setCurrentbalance(cbc.getCurrentbalance() - amttogiveuser);
                    balance.setUserid(user.getUserid());
                    try{balance.save();}catch (Exception ex){
                        SendXMPPMessage xmpp2 = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "WRITE TO DATABASE FAILED!!! Successful Move Money in Real World: amttogiveuser=$"+amttogiveuser+" to/from userid="+user.getUserid()+" "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                        xmpp2.send();
                        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero balance write failed", "Successful Move Money in Real World: amttogiveuser=$"+amttogiveuser+" to/from userid="+user.getUserid()+" "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                        //@todo send failed money transaction database write to sysadmin or accountant
                        logger.error("",ex);
                    }
                    //Notify via XMPP
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Successful Move Money in Real World: amttogiveuser=$"+amttogiveuser+" to/from "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                    xmpp.send();
                } catch (Exception ex){
                    logger.error(ex);
                    //Notify via XMPP
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Failed Move Money in Real World: amttogiveuser=$"+amttogiveuser+" to/from "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") Notes: "+pm.getNotes());
                    xmpp.send();
                }
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Failed Move Money in Real World: amttogiveuser=$"+amttogiveuser+" to/from "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") Notes: "+pm.getNotes());
                xmpp.send();
            }

            //Always record the transaction itself, even if it fails... this does not affect the account balance
            Balancetransaction balancetransaction = new Balancetransaction();
            balancetransaction.setUserid(user.getUserid());
            balancetransaction.setAmt(amttogiveuser);
            balancetransaction.setDate(new Date());
            balancetransaction.setDescription(desc);
            balancetransaction.setIssuccessful(pm.getIssuccessful());
            balancetransaction.setPaymentmethod(paymentmethod);
            if (pm.getNotes()!=null){
                balancetransaction.setNotes(pm.getNotes());
            } else {
                balancetransaction.setNotes("");
            }
            if (pm.getCorrelationid()!=null){
                balancetransaction.setCorrelationid(pm.getCorrelationid());
            } else {
                balancetransaction.setCorrelationid("");
            }
            if (pm.getTransactionid()!=null){
                balancetransaction.setTransactionid(pm.getTransactionid());
            } else {
                balancetransaction.setTransactionid("");
            }
            try{balancetransaction.save();}catch (Exception ex){
                EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero balancetransaction write failed", "amttogiveuser="+amttogiveuser+" date="+ Time.dateformatcompactwithtime(Time.nowInUserTimezone("EST"))+" userid="+user.getUserid()+" error=<br/><br/>\n\n"+ ErrorDissect.dissect(ex));
                logger.error("",ex);
            }

            //Now charge the remainder
            if (amtremainder!=0){
                amttogiveuser = amtremainder;
                giveUserThisAmt();
            }
        } else {
            if (user==null){
                logger.error("Null user being passed to MoveMoneyInRealWorld.java");
            }
        }
    }



}
