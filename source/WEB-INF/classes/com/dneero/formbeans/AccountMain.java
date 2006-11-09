package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.money.CurrentBalanceCalculator;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountMain {

    public String currentbalance = "$0.00";

    public AccountMain(){
        load();
    }

    private void load(){
        currentbalance = "$"+Str.formatForMoney(CurrentBalanceCalculator.getCurrentBalance(Jsf.getUserSession().getUser()));
    }

    




}
