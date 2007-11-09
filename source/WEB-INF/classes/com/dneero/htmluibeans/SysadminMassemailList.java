package com.dneero.htmluibeans;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;

import java.io.Serializable;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminMassemailList implements Serializable {

    private List massemails;

    public SysadminMassemailList() {



    }



    public void initBean(){
        massemails = HibernateUtil.getSession().createQuery("from Massemail order by massemailid desc").list();
    }

    public List getMassemails() {
        //logger.debug("getListitems");
        //sort("title", true);
        return massemails;
    }

    public void setMassemails(List massemails) {
        //logger.debug("setListitems");
        this.massemails = massemails;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Survey survey1 = (Survey)o1;
                Survey survey2 = (Survey)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (massemails != null && !massemails.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(massemails, comparator);
        }
    }




}
