package com.dneero.htmluibeans;

import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import org.apache.log4j.Logger;

import java.util.List;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 30, 2006
 * Time: 11:10:50 AM
 */
public class SysadminRateBlogPost implements Serializable {

    private int impressionid;
    private Impression impression;
    private int quality;
    private String iframestr;
    private boolean haveposttoreview;
    private int remainingtoreview;

    public SysadminRateBlogPost(){

    }


    public void initBean(){
        remainingtoreview = NumFromUniqueResult.getInt("select count(*) from Impression where quality='0'");
        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where quality='0'").setMaxResults(1).list();
        if (impressions.size()>0){
            impression = impressions.get(0);
            impressionid = impression.getImpressionid();
            quality = 0;
            iframestr = "<iframe frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"auto\" src=\""+impression.getReferer()+"\" style=\"width:725px;height:500px;border: 3px solid #e6e6e6;\"></iframe>";
            haveposttoreview = true;
        } else {
            impressionid = 0;
            quality=0;
            iframestr = "";
            haveposttoreview = false;
        }
    }

    public String rateAction(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (impressionid>0){
            Impression impression = Impression.get(impressionid);
            impression.setQuality(quality);
            try{impression.save();} catch (Exception ex){logger.error("",ex);}
        }
        initBean();
        return "";
    }

    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid = impressionid;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getIframestr() {
        return iframestr;
    }

    public void setIframestr(String iframestr) {
        this.iframestr = iframestr;
    }

    public boolean isHaveposttoreview() {
        return haveposttoreview;
    }

    public void setHaveposttoreview(boolean haveposttoreview) {
        this.haveposttoreview = haveposttoreview;
    }

    public int getRemainingtoreview() {
        return remainingtoreview;
    }

    public void setRemainingtoreview(int remainingtoreview) {
        this.remainingtoreview = remainingtoreview;
    }


    public Impression getImpression() {
        return impression;
    }

    public void setImpression(Impression impression) {
        this.impression = impression;
    }
}
