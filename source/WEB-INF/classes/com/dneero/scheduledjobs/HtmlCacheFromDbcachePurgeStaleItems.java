package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.cache.html.HtmlCache;
import com.dneero.cache.html.HtmlCacheFromDbcache;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class HtmlCacheFromDbcachePurgeStaleItems implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() HtmlCacheFromDbcachePurgeStaleItems called");
            HtmlCacheFromDbcache.purgeStaleItems();
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }

}