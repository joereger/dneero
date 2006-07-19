package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class CloseSurveys implements Job {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("execute() called");

    }

}
