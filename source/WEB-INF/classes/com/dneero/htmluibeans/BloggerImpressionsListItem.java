package com.dneero.htmluibeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerImpressionsListItem implements Serializable {


    private int impressionid;
    private String referer;
    private int impressionspaidandtobepaid;
    private int impressionstotal;
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


    public int getImpressionspaidandtobepaid() {
        return impressionspaidandtobepaid;
    }

    public void setImpressionspaidandtobepaid(int impressionspaidandtobepaid) {
        this.impressionspaidandtobepaid=impressionspaidandtobepaid;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getImpressionstotal() {
        return impressionstotal;
    }

    public void setImpressionstotal(int impressionstotal) {
        this.impressionstotal = impressionstotal;
    }
}
