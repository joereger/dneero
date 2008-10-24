package com.dneero.db;

import com.dneero.systemprops.InstanceProperties;

import java.util.HashMap;

/**
 * User: Joe Reger Jr
 * Date: Oct 23, 2008
 * Time: 12:34:35 PM
 */
public class DbFactory {

    private static HashMap<String, DbImplementation> cachedDbImplementations;

    public static DbImplementation get(String type){
        return get(type, getDefaultDbConfig());
    }

    public static DbImplementation get(String type, DbConfig dbConfig){
        //Make sure the dbConfig isn't null
        if (dbConfig==null){
            dbConfig = getDefaultDbConfig();
        }
        //Define the key
        String key = type+dbConfig.toString();
        //See if it's in the cache
        if (cachedDbImplementations==null){
            cachedDbImplementations = new HashMap<String, DbImplementation>();
        }
        if (cachedDbImplementations.containsKey(key)){
            //Return from cache
            return cachedDbImplementations.get(key);
        } else {
            //Create DbImplementation
            DbImplementation dbImplementation = null;
            if (type!=null && type.equals("simple")){
                dbImplementation = new com.dneero.db.simple.Db(dbConfig);
            } else if (type!=null && type.equals("proxool")){
                dbImplementation = new com.dneero.db.proxool.Db(dbConfig);
            } else {
                dbImplementation = new com.dneero.db.simple.Db(dbConfig);
            }
            //Put into cache and return
            cachedDbImplementations.put(key, dbImplementation);
            return dbImplementation;
        }
    }

    public static DbConfig getDefaultDbConfig(){
        //Pull config from InstanceProperties
        DbConfig dbConfig = new DbConfig(InstanceProperties.getDbConnectionUrl(), InstanceProperties.getDbDriverName(), InstanceProperties.getDbUsername(), InstanceProperties.getDbPassword());
        return dbConfig;
    }


}
