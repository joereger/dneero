package com.dneero.iptrack;

import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Dec 4, 2008
 * Time: 11:47:04 AM
 */
public class IptrackAnalyzer {


    public static HashMap<String, Integer> analyze(int minUseridsPerIpToInclude){
        Logger logger = Logger.getLogger(IptrackAnalyzer.class);
        HashMap<String, Integer> out = new HashMap<String, Integer>();
        String emptyString = "";
        Iterator resultsIter = HibernateUtil.getSession().createQuery("select iptrack.ip, count(distinct userid) as cnt from Iptrack iptrack group by iptrack.ip"+emptyString).setCacheable(true).list().iterator();
        while ( resultsIter.hasNext() ) {
            Object[] row = (Object[]) resultsIter.next();
            String ip = (String)row[0];
            Long countUserid = (Long)row[1];
            logger.error("ip="+ip+" countUserid="+countUserid);
            if (countUserid>=minUseridsPerIpToInclude){
                out.put(ip, countUserid.intValue());
            }
        }
        return out;
    }


}
