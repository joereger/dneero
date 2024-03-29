package com.dneero.dao.hibernate;

import org.apache.log4j.Logger;
import com.dneero.util.Num;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2007
 * Time: 7:35:25 AM
 */
public class NumFromUniqueResultImpressions {

    public static int getInt(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResultImpressions.class.getName());
        Object obj = HibernateUtilImpressions.getSession().createQuery(query).setCacheable(true).uniqueResult();
        if (obj!=null && obj instanceof Long){
            try{
                Long lng =  (Long)obj;
                if (Num.isinteger(String.valueOf(lng))){
                    int i = lng.intValue();
                    return i;
                }
            }catch(Exception ex){logger.error("",ex); return 0;}
        }
        return 0;
    }

    public static double getDouble(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResultImpressions.class.getName());
        Object obj = HibernateUtilImpressions.getSession().createQuery(query).setCacheable(true).uniqueResult();
        if (obj!=null && obj instanceof Double){
            try{
                Double dbl =  (Double)obj;
                if (Num.isdouble(String.valueOf(dbl))){
                    return dbl;
                }
            }catch(Exception ex){logger.error("",ex); return 0;}
        }
        return new Double(0);
    }




}