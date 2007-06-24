package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Balance;
import com.dneero.money.CurrentBalanceCalculator;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountBalance implements Serializable {


    private List balances;
    private String currentbalance = "$0.00";
    private double currentbalanceDbl = 0.0;

    public AccountBalance() {

    }

    public String beginView(){
        load();
        return "accountbalance";
    }


    private void load(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            currentbalanceDbl = CurrentBalanceCalculator.getCurrentBalance(Jsf.getUserSession().getUser());
            currentbalance = "$"+Str.formatForMoney(currentbalanceDbl);
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
                abli.setOptionalimpressionpaymentgroupid(balance.getOptionalimpressionpaymentgroupid());
                abli.setOptionalimpressionchargegroupid(balance.getOptionalimpressionchargegroupid());
                balances.add(abli);
            }
            sort("id", false);
        }
    }



    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }



    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AccountBalanceListItem obj1 = (AccountBalanceListItem)o1;
                AccountBalanceListItem obj2 = (AccountBalanceListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("id")) {
                    return ascending ? obj1.getBalanceid()-obj2.getBalanceid() : obj2.getBalanceid()-obj1.getBalanceid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (balances!=null && !balances.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(balances, comparator);
        }
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
}
