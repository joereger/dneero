package com.dneero.formbeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 17, 2006
 * Time: 3:19:07 PM
 */
public class ResearcherResultsImpressionsListitem implements Serializable {


    private int impressionsqualifyingforpayment;
    private String referer;
    private int impressionid;
    private String referertruncated;
    private String impressionquality;

    public ResearcherResultsImpressionsListitem(){}



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

    public String getReferertruncated() {
        return referertruncated;
    }

    public void setReferertruncated(String referertruncated) {
        this.referertruncated = referertruncated;
    }


    public String getImpressionquality() {
        return impressionquality;
    }

    public void setImpressionquality(String impressionquality) {
        this.impressionquality = impressionquality;
    }
}
