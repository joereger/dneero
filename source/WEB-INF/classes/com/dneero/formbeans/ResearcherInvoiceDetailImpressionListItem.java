package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Oct 27, 2006
 * Time: 4:11:31 PM
 */
public class ResearcherInvoiceDetailImpressionListItem {

    private int impressionid;
    private String referer;
    private String surveytitle;
    private int surveyid;
    private String ip;
    private Date impressiondate;
    


    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid = impressionid;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }


    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public Date getImpressiondate() {
        return impressiondate;
    }

    public void setImpressiondate(Date impressiondate) {
        this.impressiondate = impressiondate;
    }
}
