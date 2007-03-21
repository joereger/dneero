package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountIndex implements Serializable {

    private String currentbalance = "$0.00";
    private boolean userhasresponsependings = false;

    public AccountIndex(){

    }

    public String beginView(){
        load();
        return "accountindex";
    }

    private void load(){
        if(Jsf.getUserSession().getUser()!=null){
            currentbalance = "$"+Str.formatForMoney(CurrentBalanceCalculator.getCurrentBalance(Jsf.getUserSession().getUser()));
            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Jsf.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            if (responsependings.size()>0){
                userhasresponsependings = true;
            }
        }

    }


    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }


    public boolean isUserhasresponsependings() {
        return userhasresponsependings;
    }

    public void setUserhasresponsependings(boolean userhasresponsependings) {
        this.userhasresponsependings = userhasresponsependings;
    }
}
