package com.dneero.formbeans;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerEarningsListSurveys {

    private int surveyid;
    private String surveytitle;
    private Date surveydate;
    private int impressions;
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

    public Date getSurveydate() {
        return surveydate;
    }

    public void setSurveydate(Date surveydate) {
        this.surveydate = surveydate;
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


}
