package com.dneero.htmluibeans;

import com.dneero.util.SortableList;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Error;
import com.dneero.htmlui.ValidationException;

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

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Go get the users from the database
        logger.debug("load() called. minleveltoshow="+minleveltoshow);
        errors = HibernateUtil.getSession().createQuery("from Error where level>='"+minleveltoshow+"' order by errorid desc").setMaxResults(250).list();
    }

    public String markallold() throws ValidationException {
        int ers = HibernateUtil.getSession().createQuery("update Error set status= :statusold").setString("statusold", String.valueOf(Error.STATUS_OLD)).executeUpdate();
        initBean();
        return "sysadminerrorlist";
    }
    public String deleteall() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //int ers = HibernateUtil.getSession().createQuery("delete from Error where errorid>'0'").executeUpdate();
        errors = HibernateUtil.getSession().createQuery("from Error").list();
        for (Iterator iterator = errors.iterator(); iterator.hasNext();) {
            Error error = (Error) iterator.next();
            try{error.delete();}catch(Exception ex){logger.error("",ex);}
        }
        initBean();
        return "sysadminerrorlist";        
    }

    public String onlyerrors() throws ValidationException {
        minleveltoshow = Level.ERROR_INT;
        initBean();
        return "";
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }



    public TreeMap<String, String> getLevels(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(0), "Show All");
        out.put(String.valueOf(Level.DEBUG_INT), "Debug or Higher");
        out.put(String.valueOf(Level.WARN_INT), "Warn or Higher");
        out.put(String.valueOf(Level.INFO_INT), "Info or Higher");
        out.put(String.valueOf(Level.ERROR_INT), "Error or Higher");
        out.put(String.valueOf(Level.FATAL_INT), "Fatal Only");
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
