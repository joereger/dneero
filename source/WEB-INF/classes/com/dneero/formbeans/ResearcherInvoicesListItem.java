package com.dneero.formbeans;

import com.dneero.dao.Invoice;
import com.dneero.dao.Invoicetransaction;

import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Aug 29, 2006
 * Time: 10:58:57 AM
 */
public class ResearcherInvoicesListItem {

    private Invoice invoice;
    private ArrayList<Invoicetransaction> invoicetransactions;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ArrayList<Invoicetransaction> getInvoicetransactions() {
        return invoicetransactions;
    }

    public void setInvoicetransactions(ArrayList<Invoicetransaction> invoicetransactions) {
        this.invoicetransactions = invoicetransactions;
    }


}
