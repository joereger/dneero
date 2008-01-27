package com.dneero.charity;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.Charitydonation;
import com.dneero.util.Time;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jan 27, 2008
 * Time: 3:10:08 PM
 */
public class CharityCalculator {

    public static double getDonations(String charityname, int year, int quarter){
        double totaldonations = 0;
        Calendar startdate = CharityUtil.getQuarterStartdate(year, quarter);
        Calendar enddate = CharityUtil.getQuarterEnddate(year, quarter);
        List<Charitydonation> donations = HibernateUtil.getSession().createCriteria(Charitydonation.class)
                                   .add(Restrictions.ge("date", startdate.getTime()))
                                    .add(Restrictions.le("date", enddate.getTime()))
                                   .add(Restrictions.eq("charityname", charityname))
                                    .setCacheable(true)
                                   .list();
        for (Iterator<Charitydonation> iterator = donations.iterator(); iterator.hasNext();) {
            Charitydonation donation =  iterator.next();
            totaldonations = totaldonations + donation.getAmt();
        }
        return totaldonations;
    }
    

}
