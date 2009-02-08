package com.dneero.cachedstuff;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.htmluibeans.PublicIndexCacheitem;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.dao.Pl;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 1:57:05 PM
 */
public class GetCachedStuff {

    public static CachedStuff get(CachedStuff cs, Pl pl){
        Logger logger = Logger.getLogger(CachedStuff.class);
        String key = cs.getKey();
        String group = "CachedStuff-plid="+pl.getPlid();
        try{
            Object obj = DbcacheexpirableCache.get(key, group);
            if (obj!=null && (obj instanceof CachedStuff)){
                CachedStuff cachedCs = (CachedStuff)obj;
                int minago = DateDiff.dateDiff("minute", Calendar.getInstance(), cachedCs.refreshedTimestamp());
                if (minago>cs.maxAgeInMinutes()){
                    cs.refresh(pl);
                    Date expirationdate = Time.xMinutesAgoEnd(Calendar.getInstance(), (-1)*cs.maxAgeInMinutes()).getTime();
                    DbcacheexpirableCache.put(key, group, cs, expirationdate);
                    return cs;
                } else {
                    return cachedCs;
                }
            } else {
                cs.refresh(pl);
                Date expirationdate = Time.xMinutesAgoEnd(Calendar.getInstance(), (-1)*cs.maxAgeInMinutes()).getTime();
                DbcacheexpirableCache.put(key, group, cs, expirationdate);
                return cs;
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return cs;
    }

    public static void refresh(CachedStuff cs, Pl pl){
        Logger logger = Logger.getLogger(CachedStuff.class);
        String key = cs.getKey()+"-plid="+pl.getPlid();
        String group = "CachedStuff";
        try{
            Object obj = DbcacheexpirableCache.get(key, group);
            if (obj!=null && (obj instanceof CachedStuff)){
                CachedStuff cachedCs = (CachedStuff)obj;
                cs.refresh(pl);
                Date expirationdate = Time.xMinutesAgoEnd(Calendar.getInstance(), (-1)*cs.maxAgeInMinutes()).getTime();
                DbcacheexpirableCache.put(key, group, cs, expirationdate);
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

}
