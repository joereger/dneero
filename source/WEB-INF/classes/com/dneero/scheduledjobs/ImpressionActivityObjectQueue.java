package com.dneero.scheduledjobs;

import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.Iterator;

import com.dneero.systemprops.InstanceProperties;
import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;

/**
 * User: Joe Reger Jr
 * Date: May 1, 2007
 * Time: 11:30:22 PM
 */
public class ImpressionActivityObjectQueue implements Job {

    public static ArrayList<ImpressionActivityObject> iaos;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionActivityObjectQueue called");
            try{
                if (iaos!=null){
                    synchronized(iaos){
                        for (Iterator it = iaos.iterator(); it.hasNext(); ) {
                            ImpressionActivityObject iao = (ImpressionActivityObject)it.next();
                            try{
                                ImpressionActivityObjectStorage.store(iao);
                                synchronized(it){
                                    it.remove();
                                }
                            } catch (Exception ex){
                                logger.error(ex);
                            }
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

    public static void addIao(ImpressionActivityObject iao){
        if (iaos==null){
            iaos = new ArrayList<ImpressionActivityObject>();
        }
        synchronized(iaos){
            iaos.add(iao);
        }
    }

    public static ArrayList<ImpressionActivityObject> getIaos() {
        synchronized(iaos){
            return iaos;
        }
    }
}
