package com.dneero.survey.servlet;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:11:26 PM
 */
public class ImpressionActivityObject {

    //These are the things we track on each impression
    private int surveyid;
    private int blogid;
    private String referer;

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
}
