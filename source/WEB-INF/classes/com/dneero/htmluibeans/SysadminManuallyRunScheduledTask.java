package com.dneero.htmluibeans;

import com.dneero.scheduledjobs.*;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Sep 20, 2006
 * Time: 8:42:14 AM
 */
public class SysadminManuallyRunScheduledTask implements Serializable {

    public SysadminManuallyRunScheduledTask(){}

    public void initBean(){
        
    }


    public String runCloseSurveysByDate(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{CloseSurveysByDate task = new CloseSurveysByDate();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCloseSurveysByNumRespondents(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{CloseSurveysByNumRespondents task = new CloseSurveysByNumRespondents();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }




   

    public String runNotifyBloggersOfNewOffers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{NotifyBloggersOfNewOffers task = new NotifyBloggersOfNewOffers();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }


    public String runPendingToOpenSurveys(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{PendingToOpenSurveys task = new PendingToOpenSurveys();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }


    
    
    public String runMoveMoneyAround(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{MoveMoneyAround task = new MoveMoneyAround();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runDeleteOldPersistentlogins(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{DeleteOldPersistentlogins task = new DeleteOldPersistentlogins();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runResearcherRemainingBalanceOperations(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runSiRRankUpdate(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            SiRRankUpdate task = new SiRRankUpdate();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runSiRPointsUpdate(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            SiRPointsUpdate task = new SiRPointsUpdate();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }
    
    public String runSystemStats(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.SystemStats task = new com.dneero.scheduledjobs.SystemStats();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runSendMassemails(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.SendMassemails task = new com.dneero.scheduledjobs.SendMassemails();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runImpressionActivityObjectQueue(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.ImpressionActivityObjectQueue task = new com.dneero.scheduledjobs.ImpressionActivityObjectQueue();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCharityCalculateAmountDonated(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.CharityCalculateAmountDonated task = new com.dneero.scheduledjobs.CharityCalculateAmountDonated();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runPayForSurveyResponsesOncePosted(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{PayForSurveyResponsesOncePosted task = new com.dneero.scheduledjobs.PayForSurveyResponsesOncePosted();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runUpdateResponsePoststatus(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{UpdateResponsePoststatus task = new com.dneero.scheduledjobs.UpdateResponsePoststatus();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runUpdateResponsePoststatusForAll(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{UpdateResponsePoststatus.processAllResponses();} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runImpressionPayments(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{ImpressionPayments task = new com.dneero.scheduledjobs.ImpressionPayments();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runSystemStatsFinancial(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{SystemStatsFinancial task = new com.dneero.scheduledjobs.SystemStatsFinancial();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runPagePerformanceRecordAndFlush(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{PagePerformanceRecordAndFlush task = new com.dneero.scheduledjobs.PagePerformanceRecordAndFlush();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCurrentBalanceUpdater(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{CurrentBalanceUpdater task = new com.dneero.scheduledjobs.CurrentBalanceUpdater();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runUpdateSurveyImpressionsPaid(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{UpdateSurveyImpressionsPaid task = new com.dneero.scheduledjobs.UpdateSurveyImpressionsPaid();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runPurgeDeadUserSessions(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{PurgeDeadUserSessions task = new com.dneero.scheduledjobs.PurgeDeadUserSessions();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runHtmlCacheFromDbcachePurgeStaleItems(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{HtmlCacheFromDbcachePurgeStaleItems task = new com.dneero.scheduledjobs.HtmlCacheFromDbcachePurgeStaleItems();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



}
