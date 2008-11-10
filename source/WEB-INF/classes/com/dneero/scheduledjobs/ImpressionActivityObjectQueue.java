package com.dneero.scheduledjobs;

import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.quartz.Job;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import com.dneero.survey.servlet.ImpressionActivityObjectCollatedStorage;
import com.dneero.survey.servlet.ImpressionActivityObjectCollated;
import com.dneero.xmpp.SendXMPPMessage;

/**
 * User: Joe Reger Jr
 * Date: May 1, 2007
 * Time: 11:30:22 PM
 */
public class ImpressionActivityObjectQueue implements Job {

    private static List<ImpressionActivityObjectCollated> iaocs;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionActivityObjectQueue called");



            //Handle the iaocs
            try{
                if (iaocs!=null){
                    synchronized(iaocs){
                        //ImpressionActivityObjectCollatedStorage.resetImpCache();
                        for (Iterator it = iaocs.iterator(); it.hasNext(); ) {
                            ImpressionActivityObjectCollated iaoc = (ImpressionActivityObjectCollated)it.next();
                            try{
                                ImpressionActivityObjectCollatedStorage.store(iaoc);
                                it.remove();
                            } catch (Exception ex){
                                ex.printStackTrace();
                                logger.error("",ex);
                            }
                        }
                        //ImpressionActivityObjectCollatedStorage.resetImpCache();
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
                logger.debug("Error in bottom block.");
                logger.error("",ex);
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Error recording impression in ImpressionActivityObjectQueue bottom block: "+ ex.getMessage());
                xmpp.send();
            }

        //} else {
            //logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        //}
    }





    public static synchronized void addIaoc(ImpressionActivityObjectCollated iaoc){
        Logger logger = Logger.getLogger(ImpressionActivityObjectQueue.class);
        if (iaocs==null){
            iaocs = Collections.synchronizedList(new ArrayList<ImpressionActivityObjectCollated>());
        }
        //This is where the collation happens
        //Iterate iaocs and see if we have this already
        int currentimpressions = 0;
        try{
            synchronized(iaocs){
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
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        //Add it
        iaoc.setImpressions(iaoc.getImpressions() + currentimpressions);
        synchronized(iaocs){
            iaocs.add(iaoc);
        }

    }

    public static List<ImpressionActivityObjectCollated> getIaocs() {
        if(iaocs!=null){
            synchronized(iaocs){
                return iaocs;
            }
        }
        return null;
    }
}
