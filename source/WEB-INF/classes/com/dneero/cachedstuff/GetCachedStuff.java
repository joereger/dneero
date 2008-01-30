package com.dneero.cachedstuff;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.htmluibeans.PublicIndexCacheitem;
import com.dneero.util.DateDiff;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 1:57:05 PM
 */
public class GetCachedStuff {

    public static CachedStuff get(CachedStuff cs){
        String key = cs.getKey();
        String group = "CachedStuff";

        Object obj = CacheFactory.getCacheProvider().get(key, group);
        if (obj!=null && (obj instanceof CachedStuff)){
            CachedStuff cachedCs = (CachedStuff)obj;
            int minago = DateDiff.dateDiff("minute", Calendar.getInstance(), cachedCs.refreshedTimestamp());
            if (minago>cs.maxAgeInMinutes()){
                cs.refresh();
                CacheFactory.getCacheProvider().put(key, group, cs);
                return cs;
            } else {
                return cachedCs;
            }
        } else {
            cs.refresh();
            CacheFactory.getCacheProvider().put(key, group, cs);
            return cs;
        }
    }

}
