package com.dneero.helpers;

import com.dneero.cachedstuff.CachedStuff;
import com.dneero.json.JsonCacheFlusher;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.SurveyDisplayCacheFlusher;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 20, 2010
 * Time: 10:20:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostSurveyFlushThread implements Runnable, Serializable  {


    private int surveyid;
    private int bloggerid;
    private static ThreadPool tp;

    public PostSurveyFlushThread(int surveyid, int bloggerid){
        this.surveyid = surveyid;
        this.bloggerid = bloggerid;
    }

    public void run(){
        Logger logger = Logger.getLogger(CachedStuff.class);
        logger.debug("run() called");
        try{
            //Pause for 3 seconds
            Thread.sleep(3000);
            EmbedCacheFlusher.flushCache(surveyid, bloggerid);
            SurveyDisplayCacheFlusher.flush(surveyid);
            JsonCacheFlusher.flush(surveyid);
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public void startThread(){
        if (tp==null){tp = new ThreadPool(15);}
        tp.assign(this);
    }




}