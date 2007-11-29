package com.dneero.survey.servlet;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:11:26 PM
 */
public class ImpressionActivityObjectCollated implements Serializable {

    //These are the things we track on each impression
    private int surveyid;
    private int userid;
    private int responseid;
    private int impressions = 0;
    private String referer = "";

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }


    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions=impressions;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer=referer;
    }
}
