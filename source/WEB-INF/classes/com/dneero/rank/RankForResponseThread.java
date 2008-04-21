package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class RankForResponseThread implements Runnable, Serializable {


    private int responseid;
    private static ThreadPool tp;

    public RankForResponseThread(int responseid){
        this.responseid = responseid;
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Response response = Response.get(responseid);
        if (response!=null){
            RankForResponse.processAndSave(response);
        }
        try{
            HibernateUtil.closeSession();
        } catch (Exception ex){
            logger.debug("Error closing hibernate session at end of thread");
            logger.error("",ex);
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}