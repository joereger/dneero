package com.dneero.formbeans;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;
import com.dneero.dao.Impressiondetail;
import com.dneero.util.Time;
import com.dneero.util.Str;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicIndex {

    private String dollarswaiting;
    private String bloggersRegistered;
    private String surveysServed30Days;

    public PublicIndex(){
        load();
    }

    public String getDollarswaiting() {
        return dollarswaiting;
    }

    public void setDollarswaiting(String dollarswaiting) {
        this.dollarswaiting = dollarswaiting;
    }

    public String getBloggersRegistered() {
        return bloggersRegistered;
    }

    public void setBloggersRegistered(String bloggersRegistered) {
        this.bloggersRegistered = bloggersRegistered;
    }

    public String getSurveysServed30Days() {
        return surveysServed30Days;
    }

    public void setSurveysServed30Days(String surveysServed30Days) {
        this.surveysServed30Days = surveysServed30Days;
    }

    private void load(){
        if (dollarswaiting==null){
            List results = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_OPEN+"'").list();
            double dollars = 0;
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();
                dollars = dollars + (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested()) + ((survey.getWillingtopaypercpm() * survey.getMaxdisplaystotal())/1000);
            }
            dollarswaiting = "$"+Str.formatForMoney(dollars);
        }

        if (bloggersRegistered==null){
            List results = HibernateUtil.getSession().createQuery("select count(*) from Blogger").list();
            if (results!=null && results.size()>0){
                bloggersRegistered = String.valueOf(results.get(0));
            } else {
                bloggersRegistered = "";
            }
        }

        if (surveysServed30Days==null){
            Calendar startDate = Time.xDaysAgoStart(Calendar.getInstance(), 30);
            List results = HibernateUtil.getSession().createQuery("select count(*) from Impressiondetail where impressiondate>='"+Time.dateformatfordb(startDate)+"' and impressiondate<='"+Time.dateformatfordb(Calendar.getInstance())+"'").list();
            if (results!=null && results.size()>0){
                surveysServed30Days = String.valueOf(results.get(0));
            } else {
                surveysServed30Days = "";
            }

        }


    }


}
