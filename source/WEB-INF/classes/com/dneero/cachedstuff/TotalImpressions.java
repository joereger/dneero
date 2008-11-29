package com.dneero.cachedstuff;

import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.hibernate.NumFromUniqueResultImpressions;

import java.io.Serializable;
import java.util.Calendar;
import java.text.DecimalFormat;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class TotalImpressions implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "TotalImpressions";
    }

    public void refresh() {
        int totalimpressions = NumFromUniqueResult.getInt("select sum(impressionstotal) from Response");
        DecimalFormat formatter = new DecimalFormat();
        formatter.applyPattern("###,###,###,###");
        html = formatter.format(Double.parseDouble(String.valueOf(totalimpressions)));
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
