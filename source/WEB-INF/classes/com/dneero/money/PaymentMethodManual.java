package com.dneero.money;

import org.apache.log4j.Logger;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodManual implements PaymentMethod {
   Logger logger = Logger.getLogger(this.getClass().getName());
   private boolean issuccessful = false;
   private String notes="";

    public boolean userAcceptableForThisPayMethod(User user) {
        return true;
    }

    public void pay(User user, double amt) {
        issuccessful = true;
    }

    public void charge(User user, double amt) {
        issuccessful = true;    
    }

    public boolean issuccessful() {
        return issuccessful;
    }

    public String getNotes() {
        return notes;
    }
}
