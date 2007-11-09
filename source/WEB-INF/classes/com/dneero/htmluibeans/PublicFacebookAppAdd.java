package com.dneero.htmluibeans;

import com.dneero.systemprops.SystemProperty;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.facebook.FacebookPendingReferrals;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 13, 2007
 * Time: 5:03:27 PM
 */
public class PublicFacebookAppAdd implements Serializable {

    private String dummy = "";
    private String addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY);

    public PublicFacebookAppAdd(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        //@todo append action to addurl
        if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            String[] split = Pagez.getRequest().getParameter("action").split("-");
            if (split.length>=3){
                try{
                    String next = URLEncoder.encode("&action=showsurvey"+"-"+split[1]+"-"+split[2], "UTF-8");
                    addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY) + "&next="+next;
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public String getAddurl() {
        return addurl;
}

    public void setAddurl(String addurl) {
        this.addurl = addurl;
    }


}
