package com.dneero.cachedstuff;

import com.dneero.charity.CharityReport;
import com.dneero.dao.Pl;

import java.io.Serializable;
import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 2:03:07 PM
 */
public class DonationsToCharityMiniReport implements CachedStuff, Serializable {

    private Calendar refreshedTimestamp;
    private String html;

    public String getKey() {
        return "DonationsToCharityMiniReport";
    }

    public void refresh(Pl pl) {
        html = CharityReport.getTotalsreport();
        refreshedTimestamp = Calendar.getInstance();
    }

    public Calendar refreshedTimestamp() {
        return refreshedTimestamp;
    }

    public int maxAgeInMinutes() {
        return 10080; //7 days
    }

    public String getHtml() {
        return html;
    }
}
