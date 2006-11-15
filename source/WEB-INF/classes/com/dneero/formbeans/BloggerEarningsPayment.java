package com.dneero.formbeans;

import org.apache.log4j.Logger;

import com.dneero.dao.Impressionpaymentgroup;
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
    private int impressionpaymentgroupid;

    public BloggerEarningsPayment(){
        logger.debug("Instanciated");
    }

    public String beginView(){
        logger.debug("beginView called");
        String tmpImpressionpaymentgroupid = Jsf.getRequestParam("impressionpaymentgroupid");
        if (com.dneero.util.Num.isinteger(tmpImpressionpaymentgroupid)){
            logger.debug("beginView called: found tmpImpressionpaymentgroupid in param="+tmpImpressionpaymentgroupid);
            Impressionpaymentgroup impressionpaymentgroup = Impressionpaymentgroup.get(Integer.parseInt(tmpImpressionpaymentgroupid));
            if (impressionpaymentgroup.canRead(Jsf.getUserSession().getUser())){
                impressionpaymentgroupid = impressionpaymentgroup.getImpressionpaymentgroupid();
                date = impressionpaymentgroup.getDate();
            }
        } else {
            logger.debug("beginView called: NOT found tmpImpressionpaymentgroupid in param="+tmpImpressionpaymentgroupid);
        }
        return "bloggerearningspayment";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getImpressionpaymentgroupid() {
        return impressionpaymentgroupid;
    }

    public void setImpressionpaymentgroupid(int impressionpaymentgroupid) {
        this.impressionpaymentgroupid = impressionpaymentgroupid;
    }


}
