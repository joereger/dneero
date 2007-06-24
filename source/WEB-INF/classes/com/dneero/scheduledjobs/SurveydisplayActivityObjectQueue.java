package com.dneero.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;
import com.dneero.survey.servlet.SurveydisplayActivityObject;
import com.dneero.survey.servlet.SurveydisplayActivityObjectStorage;
import com.dneero.systemprops.InstanceProperties;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: May 1, 2007
 * Time: 11:30:22 PM
 */
public class SurveydisplayActivityObjectQueue implements Job {

    public static ArrayList<SurveydisplayActivityObject> sdaos;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SurveydisplayActivityObjectQueue called");
            try{
                if (sdaos !=null){
                    for (Iterator it = sdaos.iterator(); it.hasNext(); ) {
                        SurveydisplayActivityObject sdao = (SurveydisplayActivityObject)it.next();
                        try{
                            SurveydisplayActivityObjectStorage.store(sdao);
                            it.remove();
                        } catch (Exception ex){
                            logger.error(ex);
                        }
                    }
                }
            } catch (Exception ex){
                logger.debug("Error in top block.");
                logger.error(ex);
            }
        //} else {
            //logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        //}
    }

    public static void addSdao(SurveydisplayActivityObject sdao){
        Logger logger = Logger.getLogger(SurveydisplayActivityObject.class);
        try{
            if (sdaos ==null){
                sdaos = new ArrayList<SurveydisplayActivityObject>();
            }
            sdaos.add(sdao);
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public static ArrayList<SurveydisplayActivityObject> getSdaos() {
        return sdaos;
    }
}
