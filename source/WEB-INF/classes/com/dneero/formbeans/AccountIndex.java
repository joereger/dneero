package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;

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
    private String init;
    private String msg = "";
    private boolean isfirsttimelogin = false;

    public AccountIndex(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        load();
        if (userhasresponsependings){
            try{Jsf.redirectResponse("/blogger/index.jsf");}catch(Exception ex){logger.error(ex);}
        }
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //load();
        if (userhasresponsependings){
//            BloggerIndex bean = (BloggerIndex)Jsf.getManagedBean("bloggerIndex");
//            return bean.beginView();
            try{Jsf.redirectResponse("/blogger/index.jsf");}catch(Exception ex){logger.error(ex);}
        }
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
        AccountBalance bean = (AccountBalance)Jsf.getManagedBean("accountBalance");
        bean.beginView();
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (init!=null && init.equals("doinit")){
            logger.debug("init = doinit so calling load()");
            load();
        } else {
            logger.debug("init null or not doinit");
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
