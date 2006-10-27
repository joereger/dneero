package com.dneero.formbeans;

import com.dneero.dao.Invoice;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Aug 29, 2006
 * Time: 10:58:57 AM
 */
public class ResearcherInvoicesListItem {

    private int invoiceid;
    private Date startdate;
    private Date enddate;
    private String amtbase;
    private String amttotal;
    private String amtdneero;


    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getAmtbase() {
        return amtbase;
    }

    public void setAmtbase(String amtbase) {
        this.amtbase = amtbase;
    }

    public String getAmttotal() {
        return amttotal;
    }

    public void setAmttotal(String amttotal) {
        this.amttotal = amttotal;
    }

    public String getAmtdneero() {
        return amtdneero;
    }

    public void setAmtdneero(String amtdneero) {
        this.amtdneero = amtdneero;
    }
}
