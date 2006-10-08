package com.dneero.formbeans;

import com.dneero.paypal.Charge;
import com.dneero.paypal.Pay;
import com.dneero.util.Jsf;

/**
 * User: Joe Reger Jr
 * Date: Oct 6, 2006
 * Time: 3:35:02 AM
 */
public class SysadminPaypalManual {

    private String paypaladdress = "joe@reger.com";
    private double amt = 12.99;

    public String dopaypal(){

        Pay.pay(paypaladdress, amt);
        Jsf.setFacesMessage("Pay complete.");

        return "success";
    }

    public String getPaypaladdress() {
        return paypaladdress;
    }

    public void setPaypaladdress(String paypaladdress) {
        this.paypaladdress = paypaladdress;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

}
