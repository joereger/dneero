package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Calendar;

import com.dneero.dao.Coupon;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminCouponDetail implements Serializable {

    private Coupon coupon;

    public SysadminCouponDetail() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpCouponid = Pagez.getRequest().getParameter("couponid");
        if (com.dneero.util.Num.isinteger(tmpCouponid)){
            logger.debug("beginView called: found couponid in request param="+tmpCouponid);
            coupon = Coupon.get(Integer.parseInt(tmpCouponid));
        } else {
            coupon = new Coupon();
            coupon.setStartdate(Calendar.getInstance().getTime());
            coupon.setEnddate(Time.AddOneMonth(Calendar.getInstance()).getTime());
        }

    }

    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

        //@todo validation

        if (coupon.getCouponcode()!=null){
            coupon.setCouponcode(coupon.getCouponcode().toUpperCase());    
        }

        try{
            coupon.save();
        } catch (GeneralException gex){
            logger.debug("save() failed: " + gex.getErrorsAsSingleString());
            String message = "save() save failed: " + gex.getErrorsAsSingleString();
            vex.addValidationError(message);
            throw vex;
        }
    }


    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
