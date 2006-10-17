package com.dneero.money;

/**
 * User: Joe Reger Jr
 * Date: Oct 17, 2006
 * Time: 10:08:58 AM
 */
public class PaymentMethodBase {

    public boolean issuccessful=false;
    public String notes = "";
    public String transactionid = "";
    public String correlationid = "";


    public boolean getIssuccessful() {
        return issuccessful;
    }

    public void setIssuccessful(boolean issuccessful) {
        this.issuccessful = issuccessful;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getCorrelationid() {
        return correlationid;
    }

    public void setCorrelationid(String correlationid) {
        this.correlationid = correlationid;
    }
}
