package com.dneero.scheduledjobs;

import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.Iterator;

import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.survey.servlet.ImpressionActivityObjectCollatedStorage;
import com.dneero.survey.servlet.ImpressionActivityObjectCollated;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;

/**
 * User: Joe Reger Jr
 * Date: May 1, 2007
 * Time: 11:30:22 PM
 */
public class
ImpressionActivityObjectQueue implements Job {

    private static ArrayList<ImpressionActivityObject> iaos;
    private static ArrayList<ImpressionActivityObjectCollated> iaocs;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionActivityObjectQueue called");


            //Handle the iaos
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


            //Handle the iaocs
            try{
                if (iaocs!=null){
                    synchronized(iaocs){
                        for (Iterator it = iaocs.iterator(); it.hasNext(); ) {
                            ImpressionActivityObjectCollated iaoc = (ImpressionActivityObjectCollated)it.next();
                            try{
                                ImpressionActivityObjectCollatedStorage.store(iaoc);
                                synchronized(it){
                                    it.remove();
                                }
                            } catch (Exception ex){
                                ex.printStackTrace();
                                logger.error(ex);
                            }
                        }
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
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
        if(iaos!=null){
            synchronized(iaos){
                return iaos;
            }
        }
        return null;
    }

    public static void addIaoc(ImpressionActivityObjectCollated iaoc){
        if (iaocs==null){
            iaocs = new ArrayList<ImpressionActivityObjectCollated>();
        }
        //This is where the collation happens
        synchronized(iaocs){
            //Iterate iaocs and see if we have this already
            int currentimpressions = 0;
            for (Iterator<ImpressionActivityObjectCollated> iterator=iaocs.iterator(); iterator.hasNext();) {
                ImpressionActivityObjectCollated iaocTmp=iterator.next();
                if (iaocTmp.getSurveyid()==iaoc.getSurveyid()){
                    if (iaocTmp.getUserid()==iaoc.getUserid()){
                        if (iaocTmp.getResponseid()==iaoc.getResponseid()){
                            if (iaocTmp.getReferer().trim().equals(iaoc.getReferer().trim())){
                                currentimpressions = currentimpressions + iaocTmp.getImpressions();
                                iterator.remove();
                            }
                        }
                    }
                }
            }
            //Add it
            iaoc.setImpressions(iaoc.getImpressions() + currentimpressions);
            iaocs.add(iaoc);
        }
    }

    public static ArrayList<ImpressionActivityObjectCollated> getIaocs() {
        if(iaocs!=null){
            synchronized(iaocs){
                return iaocs;
            }
        }
        return null;
    }
}
