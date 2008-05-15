package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:52:57 PM
 */
public class CurrentBalanceCalculator implements Serializable {

    private double currentbalance = 0;
    private double currentbalanceresearcher = 0;
    private double currentbalanceblogger = 0;
    private User user;

    public CurrentBalanceCalculator(User user){
        this.user = user;
        if (user!=null){
            loadCurrentbalance();
        }
    }

    public void loadCurrentbalance(){
        if (user!=null){
            currentbalance = 0;
            currentbalance = NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"'");
            currentbalanceblogger = NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"' and isbloggermoney=true");
            currentbalanceresearcher = NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"' and isresearchermoney=true");
        }
    }




    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(double currentbalance) {
        this.currentbalance = currentbalance;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public double getCurrentbalanceresearcher() {
        return currentbalanceresearcher;
    }

    public void setCurrentbalanceresearcher(double currentbalanceresearcher) {
        this.currentbalanceresearcher=currentbalanceresearcher;
    }

    public double getCurrentbalanceblogger() {
        return currentbalanceblogger;
    }

    public void setCurrentbalanceblogger(double currentbalanceblogger) {
        this.currentbalanceblogger=currentbalanceblogger;
    }
}
