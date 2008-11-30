package com.dneero.htmluibeans;

import com.dneero.dao.Supportissue;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class CustomercareSupportIssuesList implements Serializable {

    private List supportissues = new ArrayList();
    private boolean showall= false;


    public CustomercareSupportIssuesList() {

    }

    public void initBean(){
        supportissues = new ArrayList();
        String whereSql = " where (isflaggedforcustomercare=true) ";
        if (showall){
            whereSql = "";
        }
        supportissues = HibernateUtil.getSession().createQuery("from Mail "+whereSql+" order by mailid desc").list();



    }

    public void showAll() throws ValidationException {
        showall = true;
        initBean();
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




    public boolean getShowall() {
        return showall;
    }

    public void setShowall(boolean showall) {
        this.showall=showall;
    }
}
