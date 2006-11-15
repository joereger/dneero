package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:25:41 PM
 */
public class BloggerEarningsSurveyListPayments {

    private int impressionpaymentgroupid;
    private Date paymentdate;
    private String amt;

    public int getImpressionpaymentgroupid() {
        return impressionpaymentgroupid;
    }

    public void setImpressionpaymentgroupid(int impressionpaymentgroupid) {
        this.impressionpaymentgroupid = impressionpaymentgroupid;
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
