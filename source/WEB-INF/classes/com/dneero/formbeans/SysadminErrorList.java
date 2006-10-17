package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.dao.Error;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminErrorList extends SortableList {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private List errors;

    public SysadminErrorList() {
        //Default sort column
        super("errorid");
        //Go get the users from the database
        errors = HibernateUtil.getSession().createQuery("from Error").list();
    }

    public List getErrors() {
        //logger.debug("getSurveys");
        sort(getSort(), isAscending());
        return errors;
    }

    public void setErrors(List errors) {
        //logger.debug("setSurveys");
        this.errors = errors;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Error user1 = (Error)o1;
                Error user2 = (Error)o2;

                if (column == null) {
                    return 0;
                }

                if (column.equals("errorid")){
                    return ascending ? user2.getErrorid()-user1.getErrorid() : user1.getErrorid()-user2.getErrorid() ;
                } else if (column.equals("status")){
                    return ascending ? user2.getStatus()-user1.getStatus() : user1.getStatus()-user2.getStatus() ;
                }  else if (column.equals("level")){
                    return ascending ? user2.getLevel()-user1.getLevel() : user1.getLevel()-user2.getLevel() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (errors != null && !errors.isEmpty()) {
            //logger.debug("sorting users and initializing ListDataModel");
            Collections.sort(errors, comparator);
        }
    }




}
