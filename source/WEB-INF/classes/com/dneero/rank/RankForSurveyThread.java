package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class RankForSurveyThread implements Runnable, Serializable {


    private int surveyid;
    private static ThreadPool tp;

    public RankForSurveyThread(int surveyid){
        this.surveyid = surveyid;
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            RankForSurvey.processAndSave(survey);
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