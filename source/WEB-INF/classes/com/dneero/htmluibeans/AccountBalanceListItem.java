package com.dneero.htmluibeans;

import java.util.Date;
import java.io.Serializable;


/**
 * User: Joe Reger Jr
 * Date: Oct 13, 2006
 * Time: 9:05:26 AM
 */
public class AccountBalanceListItem implements Serializable {

     private int balanceid;
     private int userid;
     private Date date;
     private String amt;
     private String currentbalance;
     private String description;
     private boolean isresearchermoney;
     private boolean isbloggermoney;


    public AccountBalanceListItem(){}

    public int getBalanceid() {
        return balanceid;
    }

    public void setBalanceid(int balanceid) {
        this.balanceid = balanceid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean getIsresearchermoney() {
        return isresearchermoney;
    }

    public void setIsresearchermoney(boolean isresearchermoney) {
        this.isresearchermoney=isresearchermoney;
    }

    public boolean getIsbloggermoney() {
        return isbloggermoney;
    }

    public void setIsbloggermoney(boolean isbloggermoney) {
        this.isbloggermoney=isbloggermoney;
    }
}
