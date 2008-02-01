package com.dneero.cachedstuff;

import com.dneero.htmluibeans.SurveyListItem;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class EmptyStuff implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "EmptyStuff";
    }

    public void refresh() {
        StringBuffer out = new StringBuffer();

        html = out.toString();
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 5;
    }

    public String getHtml() {
        return html;
    }
}
