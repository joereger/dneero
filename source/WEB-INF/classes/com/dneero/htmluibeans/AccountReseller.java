package com.dneero.htmluibeans;

import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.util.Str;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Balance;
import com.dneero.dao.Survey;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountReseller implements Serializable {

    private List balances;
    private List surveys;


    public AccountReseller() {

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession!=null && userSession.getUser()!=null){

            surveys = HibernateUtil.getSession().createQuery("from Survey where resellercode='"+userSession.getUser().getResellercode()+"' and status>='"+ Survey.STATUS_OPEN +"' order by surveyid desc").setCacheable(true).list();

            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userSession.getUser().getUserid()+"' and isresellermoney=true order by balanceid desc").setCacheable(true).list();
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


    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }


    public List getSurveys() {
        return surveys;
    }

    public void setSurveys(List surveys) {
        this.surveys = surveys;
    }
}
