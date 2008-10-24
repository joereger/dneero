package com.dneero.cache.providers;

import com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider;
import com.dneero.cache.providers.oscache.OsCacheProvider;
import com.dneero.cache.providers.oscache.OsCacheClusteredProvider;
import com.dneero.cache.providers.dbcache.DbcacheProvider;

/**
 * Factory class to get a cache provider
 */
public class CacheFactory {

    public static CacheProvider getCacheProvider(){
        //return getCacheProvider("OsCacheProvider");
        return getCacheProvider("JbossTreeCacheAOPProvider");
    }

    public static CacheProvider getCacheProvider(String providername){
        if (providername.equals("JbossTreeCacheAOPProvider")){
            return new JbossTreeCacheAOPProvider();
        } else if (providername.equals("OsCacheProvider")){
            return new OsCacheProvider();
        } else if (providername.equals("DbcacheProvider")){
            return new DbcacheProvider();
        } else if (providername.equals("OsCacheClusteredProvider")){
            return new OsCacheClusteredProvider();
        } else {
            return getCacheProvider();
        }
    }

}
