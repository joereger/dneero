package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:52:57 PM
 */
public class CurrentBalanceCalculator {

    public static double getCurrentBalance(User user){
        double bal = 0;
        List results = HibernateUtil.getSession().createQuery("from Balance where userid='"+user.getUserid()+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Balance balance = (Balance) iterator.next();
            bal = bal + balance.getAmt();
        }
        return bal;
    }


}
