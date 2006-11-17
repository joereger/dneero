package com.dneero.formbeans;

import com.dneero.scheduledjobs.*;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 20, 2006
 * Time: 8:42:14 AM
 */
public class SysadminManuallyRunScheduledTask {

    Logger logger = Logger.getLogger(this.getClass().getName());


    public String runCloseSurveysByDate(){
        try{CloseSurveysByDate task = new CloseSurveysByDate();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCloseSurveysByNumRespondents(){
        try{CloseSurveysByNumRespondents task = new CloseSurveysByNumRespondents();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runCreateImpressionpaymentgroups(){
        try{
            CreateImpressionpaymentgroups task = new CreateImpressionpaymentgroups();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCreateImpressionchargegroups(){
        try{
            CreateImpressionchargegroups task = new CreateImpressionchargegroups();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runRefreshImpressionsqualifyingforpaymentCount(){
        try{
            RefreshImpressionsqualifyingforpaymentCount task = new RefreshImpressionsqualifyingforpaymentCount();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

  

    


    public String runNotifyBloggersOfNewOffers(){
        try{NotifyBloggersOfNewOffers task = new NotifyBloggersOfNewOffers();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }


    public String runPendingToOpenSurveys(){
        try{PendingToOpenSurveys task = new PendingToOpenSurveys();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runQualityAverager(){
        try{QualityAverager task = new QualityAverager();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    
    
    public String runMoveMoneyAround(){
        try{MoveMoneyAround task = new MoveMoneyAround();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runDeleteOldPersistentlogins(){
        try{DeleteOldPersistentlogins task = new DeleteOldPersistentlogins();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runResearcherRemainingBalanceOperations(){
        try{
            ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



}
