package com.dneero.dao.extendedpropscache;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.extendedpropscache.RegerEntityExtension;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.cache.providers.CacheFactory;
import org.apache.log4j.Logger;

/**
 * Gets extended props from the cache for a given entity
 */
public class ExtendedPropsFactory {

    private static String CACHEGROUP = "extendedprops";

    public static Object getExtended(Object entity, int id){
        Logger logger = Logger.getLogger("com.dneero.dao.extendedpropscache.ExtendedPropsFactory");
        String entityClassname = entity.getClass().getName();
        //String id = String.valueOf(HibernateUtil.getIdentifier(entity));
        String nameInCache = entityClassname + "-" + id;

        logger.debug("getExtended(entity) called: " + entityClassname + " id=" + id);

        //if (id>0){
            Object fromCache = CacheFactory.getCacheProvider().get(nameInCache, CACHEGROUP);
            if (fromCache!=null){
                logger.debug("    returning from cache");
                return fromCache;
            } else {
                try{
                    logger.debug("    creating new and putting into cache");
                    RegerEntityExtension ext = (RegerEntityExtension)(Class.forName(entityClassname+"EXT").newInstance());
                    ext.load(entity);
                    CacheFactory.getCacheProvider().put(nameInCache, CACHEGROUP, ext);
                    return ext;
                } catch (Exception e){
                    logger.error("Error setting up extended object props cache");
                    logger.debug("    returning null");
                    return null;
                }
            }
        //}
        //return null;
    }

    public static void flushExtended(Object entity){
        String entityClassname = entity.getClass().getName();
        String id = String.valueOf(HibernateUtil.getIdentifier(entity));
        String nameInCache = entityClassname + "-" + id;
        Logger logger = Logger.getLogger("com.dneero.dao.extendedpropscache.ExtendedPropsFactory");
        logger.debug("flushExtended(entity) called: " + entityClassname + " id=" + id);
        CacheFactory.getCacheProvider().flush(nameInCache, CACHEGROUP);
    }

}
