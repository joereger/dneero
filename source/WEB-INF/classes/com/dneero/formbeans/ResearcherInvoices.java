package com.dneero.formbeans;

import com.dneero.util.Jsf;
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
        Researcher researcher = Jsf.getUserSession().getUser().getResearcher();
        if (researcher!=null){
            HibernateUtil.getSession().saveOrUpdate(researcher);
            for (Iterator<Invoice> iterator = researcher.getInvoices().iterator(); iterator.hasNext();) {
                Invoice invoice = iterator.next();
                ResearcherInvoicesListItem researcherInvoicesListItem = new ResearcherInvoicesListItem();
                researcherInvoicesListItem.setInvoice(invoice);
                researcherInvoicesListItem.setInvoicetransactions((ArrayList)com.dneero.util.Util.setToArrayList(invoice.getInvoicetransactions()));
                listitems.add(researcherInvoicesListItem);
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
