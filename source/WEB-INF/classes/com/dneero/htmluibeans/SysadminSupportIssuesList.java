package com.dneero.htmluibeans;

import com.dneero.dao.Supportissue;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminSupportIssuesList implements Serializable {

    private List supportissues = new ArrayList();
    private boolean showall= false;


    public SysadminSupportIssuesList() {

    }

    public void initBean(){
        supportissues = new ArrayList();
        String whereSql = " where (status='"+Supportissue.STATUS_DNEEROWORKING+"' or status='"+Supportissue.STATUS_OPEN+"') ";
        if (showall){
            whereSql = "";
        }
        supportissues = HibernateUtil.getSession().createQuery("from Supportissue "+whereSql+" order by supportissueid desc").list();



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
