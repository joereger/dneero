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

    public static double GLOBALMAXCHARGEPERTRANSACTION = 10000;


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
            //See if this needs to be broken into multiple transactions
            double amttocharge = amt;
            double amtremainder = 0;
            if (amt>GLOBALMAXCHARGEPERTRANSACTION){
                amttocharge = GLOBALMAXCHARGEPERTRANSACTION;
                amtremainder = amt - GLOBALMAXCHARGEPERTRANSACTION;
            }
            //Do the transaction
            pm.pay(user, amttocharge);

            if (pm.getIssuccessful()){
                Balance balance = new Balance();
                balance.setAmt((-1)*amttocharge);
                balance.setDate(new Date());
                balance.setDescription(desc);
                balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) - amttocharge);
                balance.setUserid(user.getUserid());
                try{balance.save();}catch (Exception ex){logger.error(ex);}
            }

            Balancetransaction balancetransaction = new Balancetransaction();
            balancetransaction.setAmt((-1)*amttocharge);
            balancetransaction.setDate(new Date());
            balancetransaction.setDescription(desc);
            balancetransaction.setIssuccessful(pm.getIssuccessful());
            balancetransaction.setNotes(pm.getNotes());
            balancetransaction.setUserid(user.getUserid());
            balancetransaction.setCorrelationid(pm.getCorrelationid());
            balancetransaction.setTransactionid(pm.getTransactionid());
            try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}

            //Now handle the remainder
            if (amtremainder!=0){
                pay(user, amtremainder);
            }

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
            //See if this needs to be broken into multiple transactions
            double amttocharge = amt;
            double amtremainder = 0;
            if (amt>GLOBALMAXCHARGEPERTRANSACTION){
                amttocharge = GLOBALMAXCHARGEPERTRANSACTION;
                amtremainder = amt - GLOBALMAXCHARGEPERTRANSACTION;
            }

            //Do the transaction
            pm.charge(user, amttocharge);

            if (pm.getIssuccessful()){
                Balance balance = new Balance();
                balance.setAmt(amttocharge);
                balance.setDate(new Date());
                balance.setDescription(desc);
                balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) + amttocharge);
                balance.setUserid(user.getUserid());
                try{balance.save();}catch (Exception ex){logger.error(ex);}
            }

            Balancetransaction balancetransaction = new Balancetransaction();
            balancetransaction.setAmt(amttocharge);
            balancetransaction.setDate(new Date());
            balancetransaction.setDescription(desc);
            balancetransaction.setIssuccessful(pm.getIssuccessful());
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
                charge(user, amtremainder);
            }
        }
    }



}
