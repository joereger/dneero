package com.dneero.htmluibeans;


import com.dneero.dao.Balance;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.PendingBalanceCalculator;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountBalance implements Serializable {

    private List balances;
    private String currentbalance = "$0.00";
    private String pendingearnings = "$0.00";
    private double currentbalanceDbl = 0.0;
    private double pendingearningsDbl = 0.0;

    public AccountBalance() {

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            PendingBalanceCalculator pbc = new PendingBalanceCalculator(Pagez.getUserSession().getUser());
            currentbalanceDbl = Pagez.getUserSession().getUser().getCurrentbalance();
            pendingearningsDbl = pbc.getPendingearnings();
            currentbalance = "$"+Str.formatForMoney(currentbalanceDbl);
            pendingearnings = "$"+Str.formatForMoney(pendingearningsDbl);
            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userSession.getUser().getUserid()+"' order by balanceid desc").setCacheable(true).list();
            balances = new ArrayList<AccountBalanceListItem>();
            for (Iterator iterator = bals.iterator(); iterator.hasNext();) {
                Balance balance = (Balance) iterator.next();
                AccountBalanceListItem abli = new AccountBalanceListItem();
                abli.setAmt("$"+ Str.formatForMoney(balance.getAmt()));
                abli.setBalanceid(balance.getBalanceid());
                abli.setCurrentbalance("$"+ Str.formatForMoney(balance.getCurrentbalance()));
                abli.setDate(balance.getDate());
                abli.setDescription(balance.getDescription());
                abli.setUserid(balance.getUserid());
                StringBuffer fundstype = new StringBuffer();
                if (balance.getIsbloggermoney()){
                    fundstype.append("Blogger");
                }
                if (balance.getIsresearchermoney()){
                    fundstype.append("Researcher");
                }
                if (balance.getIsreferralmoney()){
                    fundstype.append("Referral");
                }
                if (balance.getIsresellermoney()){
                    fundstype.append("Reseller");
                }
                abli.setFundstype(fundstype.toString());
                balances.add(abli);
            }

        }
    }



    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }






    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }


    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }

    public double getCurrentbalanceDbl() {
        return currentbalanceDbl;
    }

    public void setCurrentbalanceDbl(double currentbalanceDbl) {
        this.currentbalanceDbl = currentbalanceDbl;
    }

    public String getPendingearnings() {
        return pendingearnings;
    }

    public void setPendingearnings(String pendingearnings) {
        this.pendingearnings = pendingearnings;
    }

    public double getPendingearningsDbl() {
        return pendingearningsDbl;
    }

    public void setPendingearningsDbl(double pendingearningsDbl) {
        this.pendingearningsDbl = pendingearningsDbl;
    }
}
