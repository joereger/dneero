package com.dneero.formbeans;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerImpressionDetailsListItem implements Serializable {

    private int impressiondetailid;
    private int impressionid;
    private Date impressiondate;
    private String ip;
    private int qualifiesforpaymentstatus;
    private String referer;
    private int quality;

    public BloggerImpressionDetailsListItem(){}


    public int getImpressiondetailid() {
        return impressiondetailid;
    }

    public void setImpressiondetailid(int impressiondetailid) {
        this.impressiondetailid = impressiondetailid;
    }

    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid = impressionid;
    }

    public Date getImpressiondate() {
        return impressiondate;
    }

    public void setImpressiondate(Date impressiondate) {
        this.impressiondate = impressiondate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public int getQualifiesforpaymentstatus() {
        return qualifiesforpaymentstatus;
    }

    public void setQualifiesforpaymentstatus(int qualifiesforpaymentstatus) {
        this.qualifiesforpaymentstatus = qualifiesforpaymentstatus;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
