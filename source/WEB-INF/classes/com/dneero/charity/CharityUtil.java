package com.dneero.charity;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jan 27, 2008
 * Time: 3:10:59 PM
 */
public class CharityUtil {

    public static Calendar getQuarterStartdate(int year, int quarter){
        Calendar out = null;
        if (quarter==1){
            out = Time.formtocalendar(year, 1, 1, 12, 0, 0, "AM");
        } else if (quarter==2){
            out = Time.formtocalendar(year, 3, 1, 12, 0, 0, "AM");
        } else if (quarter==3){
            out = Time.formtocalendar(year, 6, 1, 12, 0, 0, "AM");
        } else if (quarter==4){
            out = Time.formtocalendar(year, 9, 1, 12, 0, 0, "AM");
        }
        return out;
    }

    public static Calendar getQuarterEnddate(int year, int quarter){
        Calendar out = null;
        if (quarter==1){
            out = Time.formtocalendar(year, 2, 29, 11, 59, 59, "PM");
        } else if (quarter==2){
            out = Time.formtocalendar(year, 5, 31, 11, 59, 59, "PM");
        } else if (quarter==3){
            out = Time.formtocalendar(year, 8, 31, 11, 59, 59, "PM"); 
        } else if (quarter==4){
            out = Time.formtocalendar(year, 12, 31, 11, 59, 59, "PM");
        }
        return out;
    }

    public static ArrayList<String> getUniqueCharities(){
        ArrayList<String> out = new ArrayList<String>();
        List charitynames = HibernateUtil.getSession().createQuery("select distinct charityname from Charitydonation").list();
        if (charitynames!=null){
            out = (ArrayList<String>)charitynames;
        }
        return out;
    }

}
