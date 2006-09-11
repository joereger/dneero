package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

import com.dneero.dao.Payblogger;
import com.dneero.util.Jsf;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:47 PM
 */
public class BloggerEarningsPayment {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Date date;
    private int paybloggerid;

    public BloggerEarningsPayment(){
        logger.debug("Instanciated");
    }

    public String beginView(){
        logger.debug("beginView called");
        String tmpPaybloggerid = Jsf.getRequestParam("paybloggerid");
        if (com.dneero.util.Num.isinteger(tmpPaybloggerid)){
            logger.debug("beginView called: found tmpPaybloggerid in param="+tmpPaybloggerid);
            Payblogger payblogger = Payblogger.get(Integer.parseInt(tmpPaybloggerid));
            if (payblogger.canRead(Jsf.getUserSession().getUser())){
                paybloggerid = payblogger.getPaybloggerid();
                date = payblogger.getDate();
            }
        } else {
            logger.debug("beginView called: NOT found tmpPaybloggerid in param="+tmpPaybloggerid);
        }
        return "bloggerearningspayment";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPaybloggerid() {
        return paybloggerid;
    }

    public void setPaybloggerid(int paybloggerid) {
        this.paybloggerid = paybloggerid;
    }


}
