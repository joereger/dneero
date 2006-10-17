package com.dneero.formbeans;

import com.dneero.util.Jsf;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 6, 2006
 * Time: 3:35:02 AM
 */
public class SysadminPaypalManual {

    private String paypaladdress = "joe@reger.com";
    private double amt = 12.99;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public String dopaypal(){
        try{
            //@hook up to another pay method or delete this test harness
            Jsf.setFacesMessage("Pay complete.");
        } catch (Exception ex){
            logger.error(ex);
        }

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
