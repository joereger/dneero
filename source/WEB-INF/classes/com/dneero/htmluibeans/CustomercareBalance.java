package com.dneero.htmluibeans;

import com.dneero.dao.Balance;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
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
public class CustomercareBalance implements Serializable {


    private List balances;


    public CustomercareBalance() {

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession!=null && userSession.getUser()!=null){

            List bals = HibernateUtil.getSession().createQuery("from Balance order by balanceid desc").setMaxResults(2500).setCacheable(true).list();
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
                User user = User.get(balance.getUserid());
                abli.setUsername(user.getNickname());
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



}
