package com.dneero.money;

import org.apache.log4j.Logger;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodManual extends PaymentMethodBase implements PaymentMethod {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public void pay(User user, double amt) {
        notes = "Manual payment.";
        issuccessful = true;
    }

    public void charge(User user, double amt) {
        notes = "Manual charge.";
        issuccessful = true;
    }

}
