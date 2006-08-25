package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.*;
import com.dneero.invoice.BloggerIncomeCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Aug 24, 2006
 * Time: 12:06:04 PM
 */
public class BloggerEarningsPaymentListRev extends SortableList {

    private ArrayList<BloggerEarningsPaymentListRevshares> list;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarningsPaymentListRev(){
        super("name");

        int paybloggerid = 0;
        String tmpPaybloggerid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paybloggerid");
        if (com.dneero.util.Num.isinteger(tmpPaybloggerid)){
            logger.debug("beginView called: found tmpPaybloggerid in param="+tmpPaybloggerid);
            paybloggerid = Integer.parseInt(tmpPaybloggerid);
        } else {
            logger.debug("beginView called: NOT found tmpPaybloggerid in param="+tmpPaybloggerid);
        }

        UserSession userSession = Jsf.getUserSession();
        if (paybloggerid>0 && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Payblogger payblogger = Payblogger.get(paybloggerid);
            ArrayList<Revshare> revshares = BloggerIncomeCalculator.getRevsharesForAPayblogger(payblogger);
            list = new ArrayList();
            for (Iterator<Revshare> iterator = revshares.iterator(); iterator.hasNext();) {
                Revshare revshare = iterator.next();
                Blogger sourceblogger = Blogger.get(revshare.getSourcebloggerid());
                User sourceuser = User.get(sourceblogger.getUserid());
                BloggerEarningsPaymentListRevshares listitem = new BloggerEarningsPaymentListRevshares();
                listitem.setAmt(revshare.getAmt());
                listitem.setName(sourceuser.getFirstname()+" "+sourceuser.getLastname());
                list.add(listitem);
            }


        }
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerEarningsPaymentListRevshares obj1 = (BloggerEarningsPaymentListRevshares)o1;
                BloggerEarningsPaymentListRevshares obj2 = (BloggerEarningsPaymentListRevshares)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("name")) {
                    return ascending ? obj1.getName().compareTo(obj2.getName()) : obj2.getName().compareTo(obj1.getName());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (list != null && !list.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(list, comparator);
        }
    }

    public ArrayList<BloggerEarningsPaymentListRevshares> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsPaymentListRevshares> list) {
        this.list = list;
    }


}
