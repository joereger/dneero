package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:41:48 PM
 */
public class MoveMoneyInRealWorld {


    public static void pay(User user, double amt){
        Logger logger = Logger.getLogger(MoveMoneyInRealWorld.class);

        PaymentMethod pm = new PaymentMethodManual();
        String desc = "Payment via manual means.";
        if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
            pm = new PaymentMethodCreditCard();
            desc = "Payment to credit card.";
        } else if (user.getPaymethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
            pm = new PaymentMethodPayPal();
            desc = "Payment to PayPal account.";
        }

        //Only operate if it's not a manual payment method
        if (!(pm instanceof PaymentMethodManual)){

            pm.pay(user, amt);

            if (pm.getIssuccessful()){
                Balance balance = new Balance();
                balance.setAmt((-1)*amt);
                balance.setDate(new Date());
                balance.setDescription(desc);
                balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) - amt);
                balance.setUserid(user.getUserid());
                try{balance.save();}catch (Exception ex){logger.error(ex);}
            }

            Balancetransaction balancetransaction = new Balancetransaction();
            balancetransaction.setAmt((-1)*amt);
            balancetransaction.setDate(new Date());
            balancetransaction.setDescription(desc);
            balancetransaction.setIssuccessful(pm.getIssuccessful());
            balancetransaction.setNotes(pm.getNotes());
            balancetransaction.setUserid(user.getUserid());
            balancetransaction.setCorrelationid(pm.getCorrelationid());
            balancetransaction.setTransactionid(pm.getTransactionid());
            try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}

        } else {
            logger.error("Can't pay manually. userid="+user.getUserid()+" amt="+amt);
            return;
        }



    }

    public static void charge(User user, double amt){
        Logger logger = Logger.getLogger(MoveMoneyInRealWorld.class);

        PaymentMethod pm = new PaymentMethodManual();
        String desc = "Charge.";
        if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
            pm = new PaymentMethodCreditCard();
            desc = "Charge to credit card.";
        } else if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
            pm = new PaymentMethodPayPal();
            desc = "Charge to PayPal account.";
        }

        //Only operate if it's not a manual payment method
        if (!(pm instanceof PaymentMethodManual)){
            pm.charge(user, amt);

            if (pm.getIssuccessful()){
                Balance balance = new Balance();
                balance.setAmt(amt);
                balance.setDate(new Date());
                balance.setDescription(desc);
                balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) + amt);
                balance.setUserid(user.getUserid());
                try{balance.save();}catch (Exception ex){logger.error(ex);}
            }

            Balancetransaction balancetransaction = new Balancetransaction();
            balancetransaction.setAmt(amt);
            balancetransaction.setDate(new Date());
            balancetransaction.setDescription(desc);
            balancetransaction.setIssuccessful(pm.getIssuccessful());
            balancetransaction.setNotes(pm.getNotes());
            balancetransaction.setUserid(user.getUserid());
            balancetransaction.setCorrelationid(pm.getCorrelationid());
            balancetransaction.setTransactionid(pm.getTransactionid());
            try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}
        }
    }



}
