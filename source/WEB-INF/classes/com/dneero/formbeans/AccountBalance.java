package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Blog;
import com.dneero.dao.Balance;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountBalance extends SortableList {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private List balances;

    public AccountBalance() {
        super("id");
        logger.debug("instanciating AccountBalance");
        load();

    }

    private void load(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userSession.getUser().getUserid()+"' order by balanceid desc").list();
            balances = new ArrayList<AccountBalanceListItem>();
            for (Iterator iterator = bals.iterator(); iterator.hasNext();) {
                Balance balance = (Balance) iterator.next();
                AccountBalanceListItem abli = new AccountBalanceListItem();
                abli.setAmt("$"+ Str.formatForMoney(balance.getAmt()));
                abli.setBalanceid(balance.getBalanceid());
                abli.setCurrentbalance("$"+ Str.formatForMoney(balance.getCurrentbalance()));
                abli.setDate(Time.dateformatcompactwithtime(Time.getCalFromDate(balance.getDate())));
                abli.setDescription(balance.getDescription());
                abli.setUserid(balance.getUserid());
                abli.setOptionalpaybloggerid(balance.getOptionalpaybloggerid());
                abli.setOptionalinvoiceid(balance.getOptionalinvoiceid());
                balances.add(abli);
            }

        }
    }



    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }



    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AccountBalanceListItem obj1 = (AccountBalanceListItem)o1;
                AccountBalanceListItem obj2 = (AccountBalanceListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("id")) {
                    return ascending ? obj1.getBalanceid()-obj2.getBalanceid() : obj2.getBalanceid()-obj1.getBalanceid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (balances!=null && !balances.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(balances, comparator);
        }
    }


    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }
}
