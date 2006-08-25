package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerEarningsListSurveys {

    private int surveyid;
    private int responseid;
    private String surveytitle;
    private Date responsedate;
    private int impressions;
    private int impressionsthatqualifyforpay;
    private double amtforresponse;
    private double amttotal;

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
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

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public double getAmtforresponse() {
        return amtforresponse;
    }

    public void setAmtforresponse(double amtforresponse) {
        this.amtforresponse = amtforresponse;
    }

    public double getAmttotal() {
        return amttotal;
    }

    public void setAmttotal(double amttotal) {
        this.amttotal = amttotal;
    }

    public int getImpressionsthatqualifyforpay() {
        return impressionsthatqualifyforpay;
    }

    public void setImpressionsthatqualifyforpay(int impressionsthatqualifyforpay) {
        this.impressionsthatqualifyforpay = impressionsthatqualifyforpay;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }
}
