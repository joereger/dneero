package com.dneero.formbeans;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:25:41 PM
 */
public class BloggerEarningsSurveyListPayments implements Serializable {

    private String description;
    private Date paymentdate;
    private String amt;

    public BloggerEarningsSurveyListPayments(){}


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
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
