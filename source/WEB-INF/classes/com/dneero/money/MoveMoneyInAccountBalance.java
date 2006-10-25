package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:41:48 PM
 */
public class MoveMoneyInAccountBalance {


    public static void pay(User user, double amt, String desc){
        Logger logger = Logger.getLogger(MoveMoneyInAccountBalance.class);

        Balance balance = new Balance();
        balance.setAmt(amt);
        balance.setDate(new Date());
        balance.setDescription(desc);
        balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) + amt);
        balance.setUserid(user.getUserid());
        try{balance.save();}catch (Exception ex){logger.error(ex);}
    }

    public static void charge(User user, double amt, String desc){
        Logger logger = Logger.getLogger(MoveMoneyInAccountBalance.class);

        Balance balance = new Balance();
        balance.setAmt((-1)*amt);
        balance.setDate(new Date());
        balance.setDescription(desc);
        balance.setCurrentbalance(CurrentBalanceCalculator.getCurrentBalance(user) - amt);
        balance.setUserid(user.getUserid());
        try{balance.save();}catch (Exception ex){logger.error(ex);}       
    }



}
