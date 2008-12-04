package com.dneero.iptrack;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Dec 3, 2008
 * Time: 8:10:21 PM
 */
public class CleanOldEntries {

    public static void clean(){
        clean(7);    
    }

    public static void clean(int daysago){
        int hoursago = daysago * 24;
        int minutesago = hoursago * 60;
        Calendar xcal = Time.xMinutesAgoStart(Calendar.getInstance(), minutesago);
        HibernateUtil.getSession().createQuery("delete Iptrack s where s.datetime<'"+ Time.dateformatfordb(xcal)+"'").executeUpdate();
    }

}
