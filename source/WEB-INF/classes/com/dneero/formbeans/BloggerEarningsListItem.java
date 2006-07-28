package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerEarningsListItem {

    private int responseid;
    private String surveytitle;
    private Date responsedate;
    private double amountearned;
    private int impressions;
    private String details;


    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }

    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }

    public double getAmountearned() {
        return amountearned;
    }

    public void setAmountearned(double amountearned) {
        this.amountearned = amountearned;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
