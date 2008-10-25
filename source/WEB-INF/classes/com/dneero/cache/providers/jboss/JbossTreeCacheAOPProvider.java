package com.dneero.cache.providers.jboss;

import com.dneero.cache.providers.CacheProvider;
import com.dneero.systemprops.WebAppRootDir;
import org.jboss.cache.Fqn;
import org.jboss.cache.Cache;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.CacheFactory;
import org.apache.log4j.Logger;
import org.hibernate.cache.TreeCache;

import java.util.Iterator;
import java.util.HashSet;

/**
 * Implementation of the jbosscache
 */
public class JbossTreeCacheAOPProvider implements CacheProvider {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private static Cache cache;

    public JbossTreeCacheAOPProvider(){

    }

    public String getProviderName(){
        return "JbossTreeCacheAOPProvider";
    }

    private static void setupCache(){
        Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
        try{

            CacheFactory factory = new DefaultCacheFactory();
            cache = factory.createCache(WebAppRootDir.getWebAppRootPath()+"WEB-INF"+java.io.File.separator+"classes"+java.io.File.separator+"jbc2-cache-configuration-nondao.xml");

            logger.debug("JBossCache Cache NonDAO Created");
        } catch (Exception e){
            logger.error("Error setting up NonDAO cache.", e);
        }
    }

    public static Cache getCache(){
        if (cache == null){
            synchronized (JbossTreeCacheAOPProvider.class){
                setupCache();
            }
        }
        return cache;
    }

    public Object get(String key, String group) {
        Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
        try{
            Fqn fqn = Fqn.fromString("/"+group);
            return JbossTreeCacheAOPProvider.getCache().get(fqn, key);
        } catch (Exception ex){
            logger.debug("Object not found in cache. key="+key);
            return null;
        }
    }

    public void put(String key, String group, Object obj) {
        try{
            logger.debug("put("+key+" , "+group+") called");
            Fqn fqn = Fqn.fromString("/"+group);
            JbossTreeCacheAOPProvider.getCache().put(fqn, key, obj);
        }catch (Exception e){
            Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
            logger.error("Error putting to cache", e);
        }
    }

    public void flush() {
        try{
            Fqn fqn = Fqn.fromString("/");
            JbossTreeCacheAOPProvider.getCache().removeNode(fqn);
        }catch (Exception e){
            Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
            logger.error("Error flushing from cache", e);
        }
    }

    public void flush(String group) {
        try{
            logger.debug("flush("+group+") called");
            Fqn fqn = Fqn.fromString("/"+group);
            //logger.debug("pre-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn));
            JbossTreeCacheAOPProvider.getCache().removeNode(fqn);
            //logger.debug("post-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn));
        }catch (Exception e){
            Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
            logger.error("Error flushing from cache", e);
        }
    }

    public void flush(String key, String group) {
        Logger logger = Logger.getLogger("com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider");
        try{
            logger.debug("flush("+key+", "+group+") called");
            Fqn fqn = Fqn.fromString("/"+group);
            //logger.debug("pre-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn));
            //logger.debug("pre-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+key+", "+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn, key));
            JbossTreeCacheAOPProvider.getCache().remove(fqn, key);
            //logger.debug("post-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn));
            //logger.debug("post-remove() - JbossTreeCacheAOPProvider.getTreeCache().exists("+key+", "+group+")="+JbossTreeCacheAOPProvider.getCache().exists(fqn, key));
        }catch (Exception e){
            logger.error("Error flushing from cache", e);
        }
    }

    public String[] getKeys(){
        if (JbossTreeCacheAOPProvider.getCache()!=null){
            try{
                HashSet hm = (HashSet)JbossTreeCacheAOPProvider.getCache().getKeys("/");
                String[] out = new String[hm.size()];
                int i = 0;
                for (Iterator iterator = hm.iterator(); iterator.hasNext();) {
                    String s = (String) iterator.next();
                    out[i] = s;
                    i=i+1;
                }
                return out;
            } catch (Exception ex){
                return new String[0];
            }
        }
        return new String[0];
    }

    public String[] getKeys(String group){
        if (JbossTreeCacheAOPProvider.getCache()!=null){
            try{
                HashSet hm = (HashSet)JbossTreeCacheAOPProvider.getCache().getKeys("/"+group);
                String[] out = new String[hm.size()];
                int i = 0;
                for (Iterator iterator = hm.iterator(); iterator.hasNext();) {
                    String s = (String) iterator.next();
                    out[i] = s;
                    i=i+1;
                }
                return out;
            } catch (Exception ex){
                return new String[0];
            }
        }
        return new String[0];
    }

    public String getCacheStatsAsHtml() {
        StringBuffer mb = new StringBuffer();
        mb.append("JbossCacheAOPProvider<br>");
        mb.append(com.dneero.cache.providers.jboss.CacheDumper.getHtml("/", 10));
        String[] keys = getKeys("");
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            mb.append(key + "<br/>");
        }
        return mb.toString();
    }
}
