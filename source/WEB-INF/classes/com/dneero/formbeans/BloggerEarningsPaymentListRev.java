package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.Response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import org.apache.log4j.Logger;

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
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            list = new ArrayList();
            for (Iterator<Response> iterator = userSession.getUser().getBlogger().getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                BloggerEarningsPaymentListRevshares listitem = new BloggerEarningsPaymentListRevshares();
                //@todo populate listitem
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
