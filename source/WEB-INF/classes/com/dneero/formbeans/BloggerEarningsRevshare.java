package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.Response;
import com.dneero.dao.Revshare;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.*;

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
            List revshares = HibernateUtil.getSession().createQuery("FROM Revshare WHERE targetbloggerid='"+userSession.getUser().getBlogger().getBloggerid()+"'").list();
            for (Iterator iterator = revshares.iterator(); iterator.hasNext();) {
                Revshare revshare = (Revshare) iterator.next();
                Blogger sourceblogger = Blogger.get(revshare.getSourcebloggerid());
                User sourceuser = User.get(sourceblogger.getUserid());
                BloggerEarningsRevshareListRevshares listitem = new BloggerEarningsRevshareListRevshares();
                listitem.setAmt(revshare.getAmt());
                listitem.setUsername(sourceuser.getFirstname()+" "+sourceuser.getLastname());
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
                if (column.equals("username")) {
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
