package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Response;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class RankForResponseThread implements Runnable, Serializable {


    private Response response;
    private static ThreadPool tp;

    public RankForResponseThread(Response response){
        if (response!=null){
            this.response = response;
        }
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (response!=null){
            RankForResponse.processAndSave(response);
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}