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


    public PaymentMethodManual(User user, double amt){
        super(user, amt);
    }

    public void giveUserThisAmt() {
        notes = "Manual payment.";
        issuccessful = true;
    }



}
