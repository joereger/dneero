package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Balance;
import com.dneero.dao.Balancetransaction;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountBalancetransaction extends SortableList {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private List balances;

    public AccountBalancetransaction() {
        super("id");
        logger.debug("instanciating AccountBalancetransaction");
        load();

    }

    private void load(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            List bals = HibernateUtil.getSession().createQuery("from Balancetransaction where userid='"+userSession.getUser().getUserid()+"' order by balancetransactionid desc").list();
            balances = new ArrayList<AccountBalancetransactionListItem>();
            for (Iterator iterator = bals.iterator(); iterator.hasNext();) {
                Balancetransaction balance = (Balancetransaction) iterator.next();
                AccountBalancetransactionListItem abli = new AccountBalancetransactionListItem();
                abli.setAmt("$"+ Str.formatForMoney(balance.getAmt()));
                abli.setBalancetransactionid(balance.getBalancetransactionid());
                abli.setDate(balance.getDate());
                abli.setDescription(balance.getDescription());
                abli.setNotes(balance.getNotes());
                abli.setUserid(balance.getUserid());
                abli.setIssuccessful(balance.getIssuccessful());
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
                AccountBalancetransactionListItem obj1 = (AccountBalancetransactionListItem)o1;
                AccountBalancetransactionListItem obj2 = (AccountBalancetransactionListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("id")) {
                    return ascending ? obj1.getBalancetransactionid()-obj2.getBalancetransactionid() : obj2.getBalancetransactionid()-obj1.getBalancetransactionid() ;
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
