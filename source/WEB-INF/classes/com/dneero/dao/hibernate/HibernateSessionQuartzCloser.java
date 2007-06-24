package com.dneero.dao.hibernate;

import org.quartz.JobExecutionContext;
import org.quartz.JobListener;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2007
 * Time: 10:19:02 AM
 */
public class HibernateSessionQuartzCloser implements JobListener {

    public String getName() {
        return this.getClass().getName();
    }

    public void jobToBeExecuted(JobExecutionContext context){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("jobToBeExecuted(): "+context.getJobDetail().getFullName());
    }

    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobExecutionException) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("jobWasExecuted(): "+context.getJobDetail().getFullName());
        try{
            HibernateUtil.closeSession();
        } catch (Exception ex){
            logger.debug("Error closing hibernate session at end of quartz session");
            logger.error(ex);
        }
    }


}
