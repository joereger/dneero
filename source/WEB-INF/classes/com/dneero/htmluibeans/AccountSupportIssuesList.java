package com.dneero.htmluibeans;

import com.dneero.util.SortableList;

import com.dneero.util.GeneralException;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountSupportIssuesList implements Serializable {

    private List<Supportissue> supportissues;


    public AccountSupportIssuesList() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load called");
        User user = Pagez.getUserSession().getUser();
        if (user!=null){
            supportissues = HibernateUtil.getSession().createQuery("from Supportissue where userid='"+user.getUserid()+"' order by supportissueid desc").list();
        }
    }

    public List getSupportissues() {
        return supportissues;
    }

    public void setSupportissues(List supportissues) {
        this.supportissues = (ArrayList)supportissues;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }



}
