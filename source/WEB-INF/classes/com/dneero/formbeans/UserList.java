package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class UserList extends SortableList {

    //private Logger logger = Logger.getLogger(UserList.class);
    private List users;

    public UserList() {
        //Default sort column
        super("userid");
        //Go get the users from the database
        users = HibernateUtil.getSession().createQuery("from User").list();
    }

    public List getUsers() {
        //logger.debug("getOffers");
        sort(getSort(), isAscending());
        return users;
    }

    public void setUsers(List users) {
        //logger.debug("setOffers");
        this.users = users;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                User user1 = (User)o1;
                User user2 = (User)o2;

                if (column == null) {
                    return 0;
                }
                if (column.equals("email")) {
                    return ascending ? user1.getEmail().compareTo(user2.getEmail()) : user2.getEmail().compareTo(user1.getEmail());
                } else if (column.equals("firstname")) {
                    return ascending ? user1.getFirstname().compareTo(user2.getFirstname()) : user2.getFirstname().compareTo(user1.getFirstname());
                } else if (column.equals("userid")){
                    return ascending ? user2.getUserid()-user1.getUserid() : user1.getUserid()-user2.getUserid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (users != null && !users.isEmpty()) {
            //logger.debug("sorting users and initializing ListDataModel");
            Collections.sort(users, comparator);
        }
    }




}
