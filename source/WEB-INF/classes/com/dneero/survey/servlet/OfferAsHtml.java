package com.dneero.survey.servlet;

import com.dneero.dao.Offer;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:42:43 AM
 */
public class OfferAsHtml {

    public static String getHtml(Offer offer, User user){
        StringBuffer out = new StringBuffer();

        out.append("Hello Javascript World! Offerid="+offer.getOfferid()+" userid="+user.getUserid()+"\n");
        out.append("<br>"+"\n");
        out.append("<b>This is a line in bold</b>"+"\n");

        return out.toString();
    }

}
