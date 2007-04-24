package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.LostPasswordSend;
import com.dneero.email.EmailActivationSend;
import com.dneero.money.MoveMoneyInAccountBalance;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminTransactions implements Serializable {

    private List transactions;


    public SysadminTransactions(){


    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        load();
        return "sysadmintransactions";
    }

    private void load(){
        //Load transaction info
            List trans = HibernateUtil.getSession().createQuery("from Balancetransaction order by balancetransactionid desc").list();
            transactions = new ArrayList<AccountBalancetransactionListItem>();
            for (Iterator iterator = trans.iterator(); iterator.hasNext();) {
                Balancetransaction transaction = (Balancetransaction) iterator.next();
                AccountBalancetransactionListItem abli = new AccountBalancetransactionListItem();
                abli.setAmt("$"+ Str.formatForMoney(transaction.getAmt()));
                abli.setBalancetransactionid(transaction.getBalancetransactionid());
                abli.setDate(transaction.getDate());
                abli.setDescription(transaction.getDescription());
                abli.setNotes(transaction.getNotes());
                abli.setUserid(transaction.getUserid());
                abli.setIssuccessful(transaction.getIssuccessful());
                transactions.add(abli);
            }
    }


    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }
}
