package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.money.paypal.Pay;
import com.dneero.money.paypal.Charge;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodPayPal implements PaymentMethod {
   Logger logger = Logger.getLogger(this.getClass().getName());
   private boolean issuccessful = false;
   private String notes="";

    public boolean userAcceptableForThisPayMethod(User user) {
        if (user.getPaymethodpaypaladdress()!=null && !user.getPaymethodpaypaladdress().equals("")){
            return true;
        }
        return false;
    }

    public void pay(User user, double amt) {
        try{
            Pay.pay(user.getPaymethodpaypaladdress(), amt);
            issuccessful = true;
        } catch (Exception ex){
            logger.error(ex);
            issuccessful = false;
            notes = "PayPal Error: "+ex.getMessage();
        }
    }

    public void charge(User user, double amt) {
        logger.error("Can't charge via PayPal... yet.");
        issuccessful = false;
    }

    public boolean issuccessful() {
        return issuccessful;
    }

    public String getNotes() {
        return notes;
    }
}
