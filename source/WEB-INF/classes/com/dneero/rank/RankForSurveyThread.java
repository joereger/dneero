package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class RankForSurveyThread implements Runnable, Serializable {


    private Survey survey;
    private static ThreadPool tp;

    public RankForSurveyThread(Survey survey){
        if (survey!=null){
            this.survey = survey;
        }
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (survey!=null){
            RankForSurvey.processAndSave(survey);
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}