package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.Researcher;
import com.dneero.dao.Invoice;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Aug 29, 2006
 * Time: 10:58:31 AM
 */
public class ResearcherInvoices {

    public ArrayList<ResearcherInvoicesListItem> listitems;

    public ResearcherInvoices(){
        listitems = new ArrayList<ResearcherInvoicesListItem>();
        if (Jsf.getUserSession().getUser()!=null){
            if (Jsf.getUserSession().getUser().getResearcher()!=null){
                List invoices = HibernateUtil.getSession().createQuery("from Invoice where researcherid='"+Jsf.getUserSession().getUser().getResearcher().getResearcherid()+"' order by invoiceid desc").list();
                for (Iterator iterator = invoices.iterator(); iterator.hasNext();) {
                    Invoice invoice = (Invoice) iterator.next();
                    ResearcherInvoicesListItem item = new ResearcherInvoicesListItem();
                    item.setInvoiceid(invoice.getInvoiceid());
                    item.setStartdate(invoice.getStartdate());
                    item.setEnddate(invoice.getEnddate());
                    item.setAmtbase("$"+ Str.formatForMoney(invoice.getAmtbase()));
                    item.setAmtdneero("$"+ Str.formatForMoney(invoice.getAmtdneero()));
                    item.setAmttotal("$"+ Str.formatForMoney(invoice.getAmttotal()));
                    listitems.add(item);
                }
            }
        }
    }

    public ArrayList<ResearcherInvoicesListItem> getListitems() {
        return listitems;
    }

    public void setListitems(ArrayList<ResearcherInvoicesListItem> listitems) {
        this.listitems = listitems;
    }


}
