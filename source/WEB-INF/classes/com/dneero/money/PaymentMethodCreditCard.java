package com.dneero.money;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.money.paypal.Pay;
import com.dneero.money.verisign.Verisign;
import com.dneero.money.verisign.VerisignException;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodCreditCard implements PaymentMethod {
   Logger logger = Logger.getLogger(this.getClass().getName());
   private boolean issuccessful = false;
   private String notes="";

    public boolean userAcceptableForThisPayMethod(User user) {
        //@todo how to validate user for paymethod cc?
        return true;
    }

    public void pay(User user, double amt) {
        try{
            Verisign vs = new Verisign();
            vs.chargeCard((-1)*amt, user.getPaymethodccnum(), Integer.parseInt(user.getPaymethodccexpmo()), Integer.parseInt(user.getPaymethodccexpyear()), "", "");
            issuccessful = true;
        } catch (VerisignException ex){
            issuccessful = false;
            notes = ex.getUserFriendlyMessage();
        } catch (Exception ex){
            logger.error(ex);
            issuccessful = false;
        }
    }

    public void charge(User user, double amt) {
        try{
            Verisign vs = new Verisign();
            vs.chargeCard(amt, user.getChargemethodccnum(), Integer.parseInt(user.getChargemethodccexpmo()), Integer.parseInt(user.getChargemethodccexpyear()), "", "");
            issuccessful = true;
        } catch (VerisignException ex){
            issuccessful = false;
            notes = ex.getUserFriendlyMessage();
        } catch (Exception ex){
            logger.error(ex);
            issuccessful = false;
        }
    }

    public boolean issuccessful() {
        return issuccessful;
    }

    public String getNotes() {
        return notes;
    }
}
