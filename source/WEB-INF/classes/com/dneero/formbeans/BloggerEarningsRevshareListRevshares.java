package com.dneero.formbeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:27:26 PM
 */
public class BloggerEarningsRevshareListRevshares implements Serializable {

    private String username;
    private double amt;

    public BloggerEarningsRevshareListRevshares(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }


}
