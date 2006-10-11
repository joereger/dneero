package com.dneero.money;

import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:08:11 PM
 */
public interface PaymentMethod {

    public static int PAYMENTMETHODCREDITCARD = 1;
    public static int PAYMENTMETHODMANUAL = 2;
    public static int PAYMENTMETHODPAYPAL = 3;

    public boolean userAcceptableForThisPayMethod(User user);
    public void pay(User user, double amt);
    public void charge(User user, double amt);
    public boolean issuccessful();
    public String getNotes();
   

}
