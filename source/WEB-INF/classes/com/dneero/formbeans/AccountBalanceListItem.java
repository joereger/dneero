package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Oct 13, 2006
 * Time: 9:05:26 AM
 */
public class AccountBalanceListItem {

     private int balanceid;
     private int userid;
     private String date;
     private String amt;
     private String currentbalance;
     private String description;


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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
