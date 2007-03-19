package com.dneero.formbeans;

import com.dneero.scheduledjobs.*;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Sep 20, 2006
 * Time: 8:42:14 AM
 */
public class SysadminManuallyRunScheduledTask implements Serializable {


    public String runCloseSurveysByDate(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{CloseSurveysByDate task = new CloseSurveysByDate();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCloseSurveysByNumRespondents(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{CloseSurveysByNumRespondents task = new CloseSurveysByNumRespondents();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runCreateImpressionpaymentgroups(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            CreateImpressionpaymentgroups task = new CreateImpressionpaymentgroups();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCreateImpressionchargegroups(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            CreateImpressionchargegroups task = new CreateImpressionchargegroups();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runRefreshImpressionsqualifyingforpaymentCount(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            RefreshImpressionsqualifyingforpaymentCount task = new RefreshImpressionsqualifyingforpaymentCount();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

  

    


    public String runNotifyBloggersOfNewOffers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{NotifyBloggersOfNewOffers task = new NotifyBloggersOfNewOffers();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }


    public String runPendingToOpenSurveys(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{PendingToOpenSurveys task = new PendingToOpenSurveys();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runQualityAverager(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{QualityAverager task = new QualityAverager();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    
    
    public String runMoveMoneyAround(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{MoveMoneyAround task = new MoveMoneyAround();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runDeleteOldPersistentlogins(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{DeleteOldPersistentlogins task = new DeleteOldPersistentlogins();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



    public String runResearcherRemainingBalanceOperations(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runSocialInfluenceRatingUpdate(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{SocialInfluenceRatingUpdate task = new SocialInfluenceRatingUpdate();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }
    
    public String runSystemStats(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{com.dneero.scheduledjobs.SystemStats task = new com.dneero.scheduledjobs.SystemStats();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }



}
