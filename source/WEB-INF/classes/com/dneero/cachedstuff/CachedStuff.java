package com.dneero.cachedstuff;

import com.dneero.dao.Pl;

import java.util.Calendar;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jan 29, 2008
 * Time: 1:56:16 PM
 */
public interface CachedStuff {

    public String getKey();
    public void refresh(Pl pl);
    public Calendar refreshedTimestamp();
    public int maxAgeInMinutes();


}
