package com.dneero.cachedstuff;

import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.dao.Pl;
import com.dneero.threadpool.ThreadPool;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 20, 2010
 * Time: 10:20:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetCachedStuffRefreshThread  implements Runnable, Serializable  {

    private CachedStuff cs;
    private Pl pl;
    private String key;
    private String group;
    private static ThreadPool tp;

    public GetCachedStuffRefreshThread(CachedStuff cs, Pl pl, String key, String group){
        this.cs = cs;
        this.pl = pl;
        this.key = key;
        this.group = group;
    }

    public void run(){
        Logger logger = Logger.getLogger(CachedStuff.class);
        logger.debug("run() called for key="+key+" group="+group);
        try{
            cs.refresh(pl);
            Date expirationdate = Time.xMinutesAgoEnd(Calendar.getInstance(), (-1)*cs.maxAgeInMinutes()).getTime();
            DbcacheexpirableCache.put(key, group, cs, expirationdate);
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public void startThread(){
        if (tp==null){tp = new ThreadPool(15);}
        tp.assign(this);
    }




}
