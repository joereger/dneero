package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import com.dneero.threadpool.ThreadPool;

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
        if (amttogiveuser >0){
            if (amttogiveuser >GLOBALMAXCHARGEPERTRANSACTION){
                amtremainder = amttogiveuser - GLOBALMAXCHARGEPERTRANSACTION;
                amttogiveuser = GLOBALMAXCHARGEPERTRANSACTION;
            }
        } else if (amttogiveuser <0){
            if (((-1)* amttogiveuser)>GLOBALMAXCHARGEPERTRANSACTION){
                amtremainder = amttogiveuser + GLOBALMAXCHARGEPERTRANSACTION;
                amttogiveuser = (-1)*GLOBALMAXCHARGEPERTRANSACTION;
            }
        } else {
            logger.debug("amttogiveuser=0 for userid="+user.getUserid());
            return;
        }

        //Get the payment method class based on the user's settings
        PaymentMethod pm = new PaymentMethodManual(user, amttogiveuser);
        String desc = "Charge.";
        int paymentmethod = 0;
        if (amttogiveuser >0){
            //Paying user
            if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
                pm = new PaymentMethodCreditCard(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODCREDITCARD;
                desc = "Charge to credit card.";
            } else if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
                pm = new PaymentMethodPayPal(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODPAYPAL;
                desc = "Charge to PayPal account.";
            }
        } else if (amttogiveuser <0){
            //Charging user
            if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
                pm = new PaymentMethodCreditCard(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODCREDITCARD;
                desc = "Charge to credit card.";
            } else if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
                pm = new PaymentMethodPayPal(user, amttogiveuser);
                paymentmethod = PaymentMethod.PAYMENTMETHODPAYPAL;
                desc = "Charge to PayPal account.";
            }
        }

        //Do the transaction
        pm.giveUserThisAmt();

        //Only affect the account balance if the real-world transaction was successful
        if (pm.getIssuccessful()){
            Balance balance = new Balance();
            balance.setAmt((-1)*amttogiveuser);
            balance.setDate(new Date());
            balance.setDescription(desc);
            balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) - amttogiveuser);
            balance.setUserid(user.getUserid());
            try{balance.save();}catch (Exception ex){logger.error(ex);}
        }

        //Always record the transaction itself, even if it fails... this does not affect the account balance
        Balancetransaction balancetransaction = new Balancetransaction();
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
        balancetransaction.setUserid(user.getUserid());
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
        try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}

        //Now charge the remainder
        if (amtremainder!=0){
            amttogiveuser = amtremainder;
            giveUserThisAmt();
        }

    }



}
