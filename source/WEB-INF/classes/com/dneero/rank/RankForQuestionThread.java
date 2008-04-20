package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Researcher;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.threadpool.ThreadPool;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class RankForQuestionThread implements Runnable, Serializable {


    private Question question;
    private static ThreadPool tp;

    public RankForQuestionThread(Question question){
        if (question!=null){
            this.question = question;
        }
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (question!=null){
            RankForQuestion.processAndSave(question);
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}