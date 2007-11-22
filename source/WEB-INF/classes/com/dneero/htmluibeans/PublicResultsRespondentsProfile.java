package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.Num;

import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2007
 * Time: 1:15:50 PM
 */
public class PublicResultsRespondentsProfile {

    public String dummy = "";

    public PublicResultsRespondentsProfile(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Pagez.getRequest().getParameter("responseid"))){
            Response response = Response.get(Integer.parseInt(Pagez.getRequest().getParameter("responseid")));
            logger.debug("responseid found: "+Pagez.getRequest().getParameter("responseid"));
            Blogger blogger = Blogger.get(response.getBloggerid());
            try{Pagez.sendRedirect("/profile.jsp?userid="+blogger.getUserid()); return;}catch(Exception ex){logger.error("",ex);}
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
