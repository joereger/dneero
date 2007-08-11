package com.dneero.survey.servlet;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:11:26 PM
 */
public class ImpressionActivityObject implements Serializable {

    //These are the things we track on each impression
    private int surveyid;
    private int userid;
    private int responseid;
    private String referer;
    private String ip;
    private Date date;

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

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }
}
