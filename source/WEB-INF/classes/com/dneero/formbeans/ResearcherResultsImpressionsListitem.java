package com.dneero.formbeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 17, 2006
 * Time: 3:19:07 PM
 */
public class ResearcherResultsImpressionsListitem implements Serializable {

    private String blogtitle;
    private String blogurl;
    private int impressionsqualifyingforpayment;
    private String referer;
    private int impressionid;

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

    public int getImpressionsqualifyingforpayment() {
        return impressionsqualifyingforpayment;
    }

    public void setImpressionsqualifyingforpayment(int impressionsqualifyingforpayment) {
        this.impressionsqualifyingforpayment = impressionsqualifyingforpayment;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }


    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid = impressionid;
    }
}
