package com.dneero.formbeans;

import com.dneero.dao.Offer;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerEarnings {

    private double earnings;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarnings(){
        logger.debug("BloggerEarnings instanciated.");
        earnings = 3.45;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }


}
