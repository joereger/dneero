package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.dao.Coupon;
import com.dneero.dao.Couponredemption;
import com.dneero.util.Num;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class CustomercareCouponList implements Serializable {

    private List<Coupon> coupons;
    private List<Coupon> activecoupons;
    private List<Coupon> upcomingcoupons;
    private List<Couponredemption> couponredemptions;

    public CustomercareCouponList() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        coupons = HibernateUtil.getSession().createQuery("from Coupon order by couponid desc").list();
        couponredemptions = HibernateUtil.getSession().createQuery("from Couponredemption order by couponredemptionid desc").list();
        activecoupons = HibernateUtil.getSession().createCriteria(Coupon.class)
                                           .add(Restrictions.le("startdate", Calendar.getInstance().getTime()))
                                           .add(Restrictions.ge("enddate", Calendar.getInstance().getTime()))
                                           .setCacheable(true)
                                           .list();
        upcomingcoupons = HibernateUtil.getSession().createCriteria(Coupon.class)
                                           .add(Restrictions.ge("startdate", Calendar.getInstance().getTime()))
                                           .add(Restrictions.ge("enddate", Calendar.getInstance().getTime()))
                                           .setCacheable(true)
                                           .list();
    }


    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public List<Couponredemption> getCouponredemptions() {
        return couponredemptions;
    }

    public void setCouponredemptions(List<Couponredemption> couponredemptions) {
        this.couponredemptions = couponredemptions;
    }

    public List<Coupon> getActivecoupons() {
        return activecoupons;
    }

    public void setActivecoupons(List<Coupon> activecoupons) {
        this.activecoupons = activecoupons;
    }

    public List<Coupon> getUpcomingcoupons() {
        return upcomingcoupons;
    }

    public void setUpcomingcoupons(List<Coupon> upcomingcoupons) {
        this.upcomingcoupons = upcomingcoupons;
    }
}
