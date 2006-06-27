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

        out.append("<b>dNeero Survey</b>");
        out.append("<br>");
        out.append(offer.getOfferbody());
        out.append("<br>");
        out.append("<b>dNeero - blog for pay</b>");

        return out.toString();
    }

}
