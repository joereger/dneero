package com.dneero.formbeans;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class AccountImpressionchargegroupListItem implements Serializable {


    private int impressiondetailid;
    private Date impressiondate;
    private String ip;
    private String referer;
    private String surveytitle;


    public int getImpressiondetailid() {
        return impressiondetailid;
    }

    public void setImpressiondetailid(int impressiondetailid) {
        this.impressiondetailid = impressiondetailid;
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


    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }


    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }
}
