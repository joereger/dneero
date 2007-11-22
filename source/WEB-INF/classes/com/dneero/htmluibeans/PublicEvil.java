package com.dneero.htmluibeans;

import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.systemprops.BaseUrl;

import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: May 1, 2007
 * Time: 3:40:27 PM
 */
public class PublicEvil {

    private String socialbookmarklinks;

    public PublicEvil(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String url = "";
        try{
            url = URLEncoder.encode(BaseUrl.get(false)+"evil.jsp", "UTF-8");
        } catch (Exception ex){
            logger.debug(ex);
        }
        if (!url.equals("")){
            socialbookmarklinks = SocialBookmarkLinks.getSocialBookmarkLinks(url, "dNeero.com: oh, btw, we're evil.");
        } else {
            socialbookmarklinks = "";
        }
    }


    public String getSocialbookmarklinks() {
        return socialbookmarklinks;
    }

    public void setSocialbookmarklinks(String socialbookmarklinks) {
        this.socialbookmarklinks = socialbookmarklinks;
    }
}
