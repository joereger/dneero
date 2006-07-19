package com.dneero.formbeans;

/**
 * User: Joe Reger Jr
 * Date: Jul 17, 2006
 * Time: 3:19:07 PM
 */
public class ResearcherSurveyResultsImpressionsObj {

    private String blogtitle;
    private String blogurl;
    private int totalimpressions;
    private String referer;

    public String getBlogtitle() {
        return blogtitle;
    }

    public void setBlogtitle(String blogtitle) {
        this.blogtitle = blogtitle;
    }

    public String getBlogurl() {
        return blogurl;
    }

    public void setBlogurl(String blogurl) {
        this.blogurl = blogurl;
    }

    public int getTotalimpressions() {
        return totalimpressions;
    }

    public void setTotalimpressions(int totalimpressions) {
        this.totalimpressions = totalimpressions;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
