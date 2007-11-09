package com.dneero.htmluibeans;

import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Num;
import com.dneero.util.Str;

import java.util.List;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountIndex implements Serializable {

    private String currentbalance = "$0.00";
    private boolean userhasresponsependings = false;
    private String msg = "";
    private boolean isfirsttimelogin = false;

    public AccountIndex(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(Pagez.getUserSession().getUser()!=null && Num.isinteger(String.valueOf(Pagez.getUserSession().getUser().getUserid()))){
            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
            currentbalance = "$"+Str.formatForMoney(cbc.getCurrentbalance());
            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            if (responsependings.size()>0){
                userhasresponsependings = true;
                Pagez.sendRedirect("/jsp/blogger/index.jsp");
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getIsfirsttimelogin() {
        return isfirsttimelogin;
    }

    public void setIsfirsttimelogin(boolean isfirsttimelogin) {
        this.isfirsttimelogin = isfirsttimelogin;
    }

 
}
