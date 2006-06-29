package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Offer;
import com.dneero.util.GeneralException;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerOfferDetail {

    private Offer offer;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerOfferDetail(){
        logger.debug("BloggerOfferDetail instanciated.");
        offer = new Offer();
    }


    public String beginView(){
        logger.debug("beginView called: offer.offerid="+offer.getOfferid());
        String tmpOfferid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("offerid");
        if (com.dneero.util.Num.isinteger(tmpOfferid)){
            logger.debug("beginView called: found offerid in param="+tmpOfferid);
            offer = Offer.get(Integer.parseInt(tmpOfferid));
        } else {
            logger.debug("beginView called: NOT found offerid in param="+tmpOfferid);
        }
        return "bloggerofferdetail";
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

}
