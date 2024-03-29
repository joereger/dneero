package com.dneero.cachedstuff;

import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.dao.Pl;
import com.dneero.htmlui.Pagez;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

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
            boolean forceRefreshByURL = false;
            if (Pagez.getRequest()!=null && Pagez.getRequest().getParameter("refresh")!=null && Pagez.getRequest().getParameter("refresh").equals("1")){
                forceRefreshByURL = true;
            }
            //Note third argument which tells cache to return object instead of null even if expired
            Object obj = DbcacheexpirableCache.get(key, group, false);
            if (obj!=null && !forceRefreshByURL && (obj instanceof CachedStuff)){
                CachedStuff cachedCs = (CachedStuff)obj;
                int minago = DateDiff.dateDiff("minute", Calendar.getInstance(), cachedCs.refreshedTimestamp());
                if (minago>cs.maxAgeInMinutes()){
                    //Kick off the background thread to refresh
                    logger.debug("minago>cs.maxAgeInMinutes() so kicking off thread to refresh");
                    GetCachedStuffRefreshThread gcsrt = new GetCachedStuffRefreshThread(cs, pl, key, group);
                    gcsrt.startThread();
                }
                //Immediately return most recently refreshed
                return cachedCs;
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

    public static void flush(CachedStuff cs, Pl pl){
        Logger logger = Logger.getLogger(CachedStuff.class);
        String key = cs.getKey();
        String group = "CachedStuff-plid="+pl.getPlid();
        try{
            DbcacheexpirableCache.flush(key, group);
        } catch (Exception ex){
            logger.error("", ex);
        }
    }



}
