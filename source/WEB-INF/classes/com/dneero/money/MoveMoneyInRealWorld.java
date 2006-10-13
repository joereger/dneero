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

        pm.pay(user, amt);

        if (pm.issuccessful()){
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
        balancetransaction.setIssuccessful(pm.issuccessful());
        balancetransaction.setNotes(pm.getNotes());
        balancetransaction.setUserid(user.getUserid());
        try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}



    }

    public static void charge(User user, double amt){
        Logger logger = Logger.getLogger(MoveMoneyInRealWorld.class);

        PaymentMethod pm = new PaymentMethodManual();
        String desc = "Charge via manual means.";
        if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
            pm = new PaymentMethodCreditCard();
            desc = "Charge to credit card.";
        } else if (user.getChargemethod()==PaymentMethod.PAYMENTMETHODPAYPAL){
            pm = new PaymentMethodPayPal();
            desc = "Charge to PayPal account.";
        }

        pm.charge(user, amt);

        if (pm.issuccessful()){
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
        balancetransaction.setIssuccessful(pm.issuccessful());
        balancetransaction.setNotes(pm.getNotes());
        balancetransaction.setUserid(user.getUserid());
        try{balancetransaction.save();}catch (Exception ex){logger.error(ex);}
    }



}
