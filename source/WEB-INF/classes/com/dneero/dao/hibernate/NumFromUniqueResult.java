package com.dneero.dao.hibernate;

import org.apache.log4j.Logger;
import com.dneero.util.Num;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2007
 * Time: 7:35:25 AM
 */
public class NumFromUniqueResult {

    public static int getInt(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResult.class);
        Object obj = HibernateUtil.getSession().createQuery(query).setCacheable(true).uniqueResult();
        try{
            //logger.error("obj="+String.valueOf(obj));
            if (Num.isdouble(String.valueOf(obj))){
                Double dbl = Double.parseDouble(String.valueOf(obj));
                return dbl.intValue();
            }
        }catch(Exception ex){logger.error("",ex); return 0;}
        return 0;
    }

    public static double getDouble(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResult.class);
        Object obj = HibernateUtil.getSession().createQuery(query).setCacheable(true).uniqueResult();
        try{
            //logger.error("obj="+String.valueOf(obj));
            if (Num.isdouble(String.valueOf(obj))){
                Double dbl = Double.parseDouble(String.valueOf(obj));
                return dbl;        
            }
        }catch(Exception ex){logger.error("",ex); return 0;}
        return new Double(0);
    }




}
