package com.dneero.json;

import com.dneero.cache.html.DbcacheexpirableCache;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Sep 20, 2010
 * Time: 11:45:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonCacheFlusher {

    
    public static void flush(int surveyid){
        //Flush entire group, for now
        DbcacheexpirableCache.flush("SurveysAsJson");
    }


}
