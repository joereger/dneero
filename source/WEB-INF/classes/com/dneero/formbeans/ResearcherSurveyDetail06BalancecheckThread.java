package com.dneero.formbeans;

import com.dneero.dao.Researcher;
import com.dneero.threadpool.ThreadPool;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 17, 2006
 * Time: 1:00:05 PM
 */
public class ResearcherSurveyDetail06BalancecheckThread implements Runnable {
    private Researcher researcher;
    private static ThreadPool tp;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail06BalancecheckThread(Researcher researcher){
        if (researcher!=null){
            this.researcher = researcher;
        }
    }

    public void run(){
        if (researcher!=null){
            logger.debug("waiting researcherid="+researcher.getResearcherid());
            try{wait(10000);} catch (Exception ex){logger.error(ex);};
            logger.debug("running researcherid="+researcher.getResearcherid());
            ResearcherRemainingBalanceOperations.processResearcher(researcher);
            logger.debug("done researcherid="+researcher.getResearcherid());
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }
}
