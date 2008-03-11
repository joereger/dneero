package com.dneero.htmluibeans;

import com.dneero.dao.Coupon;
import com.dneero.dao.Couponredemption;
import com.dneero.dao.Pageperformance;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;
import java.util.List;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminPagePerformance implements Serializable {


    private List<Pageperformance> pps;
    private List<Pageperformance> ppssinglepageid;
    private String pageid = null;


    public SysadminPagePerformance() {

    }



    public void initBean(){

        pps = HibernateUtil.getSession().createCriteria(Pageperformance.class)
                                           .addOrder(Order.desc("pageperformanceid"))
                                           .setCacheable(true)
                                           .setMaxResults(600)
                                           .list();


        if (Pagez.getRequest().getParameter("pageid")!=null && !Pagez.getRequest().getParameter("pageid").equals("")){
            pageid = Pagez.getRequest().getParameter("pageid");
        }
        if (pageid!=null){
            ppssinglepageid = HibernateUtil.getSession().createCriteria(Pageperformance.class)
                                            .add(Restrictions.eq("pageid", pageid))
                                            .addOrder(Order.desc("pageperformanceid"))
                                           .setCacheable(true)
                                           .setMaxResults(600)
                                           .list();
        }
    }


    public List<Pageperformance> getPps() {
        return pps;
    }

    public void setPps(List<Pageperformance> pps) {
        this.pps = pps;
    }

    public List<Pageperformance> getPpssinglepageid() {
        return ppssinglepageid;
    }

    public void setPpssinglepageid(List<Pageperformance> ppssinglepageid) {
        this.ppssinglepageid = ppssinglepageid;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }
}
