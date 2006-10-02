package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:25:41 PM
 */
public class BloggerEarningsSurveyListPayments {

    private int paybloggerid;
    private Date paymentdate;
    private String amt;

    public int getPaybloggerid() {
        return paybloggerid;
    }

    public void setPaybloggerid(int paybloggerid) {
        this.paybloggerid = paybloggerid;
    }

    public Date getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }


}
