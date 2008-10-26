package com.dneero.cache.html;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.hibernate.HibernateUtilDbcache;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Oct 26, 2008
 * Time: 12:19:50 PM
 */
public class HtmlCacheFromDbcache {

    public static String GRPPREFIX = "htmlcache-";

    public static Object get(String key, CacheTTL ttl){
        Logger logger = Logger.getLogger(HtmlCacheFromDbcache.class);
        return CacheFactory.getCacheProvider("DbcacheProvider").get(key, generateGrp(ttl));
    }

    public static void put(String key, Object value, CacheTTL ttl){
        Logger logger = Logger.getLogger(HtmlCacheFromDbcache.class);
        CacheFactory.getCacheProvider("DbcacheProvider").put(key, generateGrp(ttl), value);
    }

    private static String generateGrp(CacheTTL ttl){
        return GRPPREFIX + ttl.getMinutesTL();
    }

    private static String getTimePurgePoint(CacheTTL ttl){
        Logger logger = Logger.getLogger(HtmlCacheFromDbcache.class);
        try{
            return Time.dateformatfordb(Time.xMinutesAgoEnd(Calendar.getInstance(), ttl.getMinutesTL()));
        } catch (Exception ex){
            logger.error("", ex);
        }
        //Worst case just don't let anything purge
        return "2000-01-01 12:00:00";
    }

    public static void purgeStaleItems(){
        Logger logger = Logger.getLogger(HtmlCacheFromDbcache.class);
        try{
            //15min
            try{HibernateUtilDbcache.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+generateGrp(new CacheTTL15min())+"' and d.date<'"+getTimePurgePoint(new CacheTTL15min())+"'").executeUpdate();} catch (Exception ex){logger.error("", ex);}
            //1hr
            try{HibernateUtilDbcache.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+generateGrp(new CacheTTL1hr())+"' and d.date<'"+getTimePurgePoint(new CacheTTL1hr())+"'").executeUpdate();} catch (Exception ex){logger.error("", ex);}
            //3hr
            try{HibernateUtilDbcache.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+generateGrp(new CacheTTL3hr())+"' and d.date<'"+getTimePurgePoint(new CacheTTL3hr())+"'").executeUpdate();} catch (Exception ex){logger.error("", ex);}
            //12hr
            try{HibernateUtilDbcache.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+generateGrp(new CacheTTL12hr())+"' and d.date<'"+getTimePurgePoint(new CacheTTL12hr())+"'").executeUpdate();} catch (Exception ex){logger.error("", ex);}
        } catch (Exception ex){

        }
    }

}
