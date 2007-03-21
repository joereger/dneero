package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
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


    public SysadminSupportIssuesList() {

    }
    
    public String beginView(){
        load();
        return "sysadminsupportissueslist";
    }

    public void load(){
        supportissues = new ArrayList();
        supportissues = HibernateUtil.getSession().createQuery("from Supportissue order by supportissueid desc").list();
    }

    public List getSupportissues() {
        sort("supportissueid", false);
        return supportissues;
    }

    public void setSupportissues(List supportissues) {
        this.supportissues = (ArrayList)supportissues;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }

    protected void sort(final String column, final boolean ascending) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Supportissue obj1 = (Supportissue)o1;
                Supportissue obj2 = (Supportissue)o2;
                if (column == null) {
                    return 0;
                }
                if (obj1!=null && obj2!=null && column.equals("supportissueid")) {
                    return ascending ? obj1.getSupportissueid()-obj2.getSupportissueid() : obj2.getSupportissueid()-obj1.getSupportissueid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (supportissues != null && !supportissues.isEmpty()) {
            logger.debug("sorting supportissues and initializing ListDataModel");
            Collections.sort(supportissues, comparator);
        }
    }






}
