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
 * Date: Aug 21, 2006
 * Time: 7:12:35 PM
 */
public class BloggerEarningsRevshare extends SortableList {

    private ArrayList<BloggerEarningsRevshareListRevshares> list;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarningsRevshare(){
        super("username");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            list = new ArrayList();
            for (Iterator<Response> iterator = userSession.getUser().getBlogger().getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                BloggerEarningsRevshareListRevshares listitem = new BloggerEarningsRevshareListRevshares();
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
                BloggerEarningsRevshareListRevshares obj1 = (BloggerEarningsRevshareListRevshares)o1;
                BloggerEarningsRevshareListRevshares obj2 = (BloggerEarningsRevshareListRevshares)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? obj1.getUsername().compareTo(obj2.getUsername()) : obj2.getUsername().compareTo(obj1.getUsername());
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

    public ArrayList<BloggerEarningsRevshareListRevshares> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsRevshareListRevshares> list) {
        this.list = list;
    }


}
