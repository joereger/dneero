package com.dneero.cachedstuff;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.reports.FieldAggregator;
import com.dneero.reports.SimpleTableOutput;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class AgeDemographic implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "AgeDemographic";
    }

    public void refresh() {
        //@todo Implement this efficiently... I can't run this massive query after every restart!
        //List bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();
        //FieldAggregator fa = new FieldAggregator((ArrayList)bloggers);
        //SimpleTableOutput sto = new SimpleTableOutput(fa, "age");
        //html = sto.getHtml();
        html = "NOT YET IMPLEMENTED";
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 1440;
    }

    public String getHtml() {
        return html;
    }
}
