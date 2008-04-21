package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Researcher;
import com.dneero.dao.hibernate.HibernateUtil;
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


    private int questionid;
    private static ThreadPool tp;

    public RankForQuestionThread(int questionid){
        this.questionid = questionid;
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Question question = Question.get(questionid);
        if (question!=null){
            RankForQuestion.processAndSave(question);
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