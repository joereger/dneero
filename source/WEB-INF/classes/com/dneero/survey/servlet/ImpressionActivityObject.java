package com.dneero.survey.servlet;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:11:26 PM
 */
public class ImpressionActivityObject implements Serializable {

    //These are the things we track on each impression
    private int surveyid;
    private int blogid;
    private String referer;
    private String ip;

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
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
}
