package com.dneero.helpers;

import com.dneero.dao.Response;
import com.dneero.rank.RankForResponse;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class StoreResponsePost implements Runnable, Serializable {


    private int responseid;
    private static ThreadPool tp;

    public StoreResponsePost(int responseid){
        this.responseid = responseid;
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("run() starting");
        Response response = Response.get(responseid);
        if (response!=null){

            try{
                //Update rank points
                RankForResponse.processAndSave(response);
            } catch (Exception ex){logger.error("",ex);}

            try{
                //Process the statusHtml for the response
                UpdateResponsePoststatus.processSingleResponse(response);
            } catch (Exception ex){logger.error("",ex);}

            //Do the Incentive hook call
            try{
                response.getIncentive().doImmediatelyAfterResponse(response);
            } catch (Exception ex){logger.error("",ex);};

        }
//        try{
//            HibernateUtil.closeSession();
//        } catch (Exception ex){
//            logger.debug("Error closing hibernate session at end of thread");
//            logger.error("",ex);
//        }

        logger.debug("run() ending");
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}