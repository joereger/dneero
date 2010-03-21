package com.dneero.cachedstuff;

import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.Pl;

import java.io.Serializable;
import java.util.Calendar;
import java.text.DecimalFormat;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class TotalSurveysTaken implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "TotalSurveysTaken";
    }

    public void refresh(Pl pl) {
        int totalsurveystaken = NumFromUniqueResult.getInt("select count(*) from Response");
        DecimalFormat formatter = new DecimalFormat();
        formatter.applyPattern("###,###,###,###");
        html = formatter.format(Double.parseDouble(String.valueOf(totalsurveystaken)));
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 60*24;
    }

    public String getHtml() {
        return html;
    }
}
