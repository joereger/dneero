package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2007
 * Time: 1:15:50 PM
 */
public class PublicResultsRespondentsProfile {

    public String dummy = "";

    public PublicResultsRespondentsProfile(){
        load();
    }



    public void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Jsf.getRequestParam("responseid"))){
            Response response = Response.get(Integer.parseInt(Jsf.getRequestParam("responseid")));
            logger.debug("responseid found: "+Jsf.getRequestParam("responseid"));
            Blogger blogger = Blogger.get(response.getBloggerid());
            try{Jsf.redirectResponse("/profile.jsf?userid="+blogger.getUserid()); return;}catch(Exception ex){logger.error(ex);}
        }
        logger.debug("Should never get to this point.");
        //return "publicsurvey";
    }


    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
