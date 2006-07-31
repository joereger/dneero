package com.dneero.constants;

import com.dneero.util.Jsf;

import java.util.Iterator;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Jul 31, 2006
 * Time: 4:22:34 PM
 */
public class GetterBase {


    public static Object get(Object key, String beanname){
       Map hm = (Map)Jsf.getManagedBean(beanname);
       if (hm.containsKey(key)){
            return hm.get(key);
       }
       return null;
    }

    public static Object getKeyByVal(Object val, String beanname){
        Map hm = (Map)Jsf.getManagedBean(beanname);
        if (hm.containsValue(val)){
            Iterator keyValuePairs = hm.entrySet().iterator();
            for (int i = 0; i < hm.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                Object key = mapentry.getKey();
                Object value = mapentry.getValue();
                if (value.equals(val)){
                    return key;
                }
            }
        }
        return null;
    }

}
