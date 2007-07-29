package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Error;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminErrorList implements Serializable {

    private List errors;
    private int minleveltoshow=0;

    public SysadminErrorList() {

    }

    public String beginView(){
        load();
        return "sysadminerrorlist";
    }

    public String load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Go get the users from the database
        logger.debug("load() called. minleveltoshow="+minleveltoshow);
        errors = HibernateUtil.getSession().createQuery("from Error where level>='"+minleveltoshow+"' order by errorid desc").list();
        return "";        
    }

    public String markallold(){
        int ers = HibernateUtil.getSession().createQuery("update Error set status= :statusold").setString("statusold", String.valueOf(Error.STATUS_OLD)).executeUpdate();
        load();
        return "sysadminerrorlist";
    }
    public String deleteall(){
        int ers = HibernateUtil.getSession().createQuery("delete from Error where errorid>'0'").executeUpdate();
        errors = new ArrayList();
        return "sysadminerrorlist";        
    }

    public String onlyerrors(){
        minleveltoshow = Level.ERROR_INT;
        load();
        return "";
    }

    public List getErrors() {
        //logger.debug("getListitems");
        sort("errorid", true);
        return errors;
    }

    public void setErrors(List errors) {
        //logger.debug("setListitems");
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

    public LinkedHashMap getLevels(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Show All", 0);
        out.put("Debug or Higher", Level.DEBUG_INT);
        out.put("Warn or Higher", Level.WARN_INT);
        out.put("Info or Higher", Level.INFO_INT);
        out.put("Error or Higher", Level.ERROR_INT);
        out.put("Fatal Only", Level.FATAL_INT);
        return out;
    }


    public int getMinleveltoshow() {
        return minleveltoshow;
    }

    public void setMinleveltoshow(int minleveltoshow) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setMinleveltoshow() called. minleveltoshow="+minleveltoshow);
        this.minleveltoshow = minleveltoshow;
    }
}
