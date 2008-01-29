package com.dneero.htmluibeans;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jan 28, 2008
 * Time: 8:26:22 PM
 */
public class ResearcherResultsDemographicsCacheitem {

    private String html;
    private Calendar lastupdated;


    public ResearcherResultsDemographicsCacheitem(String html, Calendar lastupdated) {
        this.html = html;
        this.lastupdated = lastupdated;
    }

    public String getHtml() {
        return html;
    }

    public Calendar getLastupdated() {
        return lastupdated;
    }
}
