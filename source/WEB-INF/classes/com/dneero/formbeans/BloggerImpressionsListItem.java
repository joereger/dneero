package com.dneero.formbeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerImpressionsListItem implements Serializable {


    private int impressionid;
    private String referer;
    private int impressionsqualifyingforpayment;
    private int quality;

    public BloggerImpressionsListItem(){}


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

    public int getImpressionsqualifyingforpayment() {
        return impressionsqualifyingforpayment;
    }

    public void setImpressionsqualifyingforpayment(int impressionsqualifyingforpayment) {
        this.impressionsqualifyingforpayment = impressionsqualifyingforpayment;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
