package com.dneero.htmluibeans;

import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.PendingBalanceCalculator;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.session.PersistentLogin;

import java.util.List;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;

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
    private String pendingearnings = "$0.00";
    private double currentbalanceDbl = 0.0;
    private double pendingearningsDbl = 0.0;

    public AccountIndex(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(Pagez.getUserSession().getUser()!=null && Num.isinteger(String.valueOf(Pagez.getUserSession().getUser().getUserid()))){
            PendingBalanceCalculator pbc = new PendingBalanceCalculator(Pagez.getUserSession().getUser());
            currentbalanceDbl = Pagez.getUserSession().getUser().getCurrentbalance();
            pendingearningsDbl = pbc.getPendingearnings();
            currentbalance = "$"+Str.formatForMoney(currentbalanceDbl);
            pendingearnings = "$"+Str.formatForMoney(pendingearningsDbl);

            //Set persistent login cookie, if necessary
            if (Pagez.getRequest().getParameter("keepmeloggedin")!=null && Pagez.getRequest().getParameter("keepmeloggedin").equals("1")){
                //Get all possible cookies to set
                Cookie[] cookies = PersistentLogin.getPersistentCookies(Pagez.getUserSession().getUser().getUserid(), Pagez.getRequest());
                //Add a cookies to the response
                for (int j = 0; j < cookies.length; j++) {
                    Pagez.getResponse().addCookie(cookies[j]);
                }
            }

            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
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


    public boolean getUserhasresponsependings() {
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

    public String getPendingearnings() {
        return pendingearnings;
    }

    public void setPendingearnings(String pendingearnings) {
        this.pendingearnings = pendingearnings;
    }

    public double getCurrentbalanceDbl() {
        return currentbalanceDbl;
    }

    public void setCurrentbalanceDbl(double currentbalanceDbl) {
        this.currentbalanceDbl = currentbalanceDbl;
    }

    public double getPendingearningsDbl() {
        return pendingearningsDbl;
    }

    public void setPendingearningsDbl(double pendingearningsDbl) {
        this.pendingearningsDbl = pendingearningsDbl;
    }
}
