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

    public String runCreateInvoices(){
        try{CreateInvoices task = new CreateInvoices();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runCreatePaybloggers(){
        try{CreatePaybloggers task = new CreatePaybloggers();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runInvoiceCollectViaCreditCard(){
        try{InvoiceCollectViaCreditCard task = new InvoiceCollectViaCreditCard();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runInvoiceMarkPartiallyPaid(){
        try{InvoiceMarkPartiallyPaid task = new InvoiceMarkPartiallyPaid();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runInvoiceMarkPastDue(){
        try{InvoiceMarkPastDue task = new InvoiceMarkPastDue();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runNotifyBloggersOfNewOffers(){
        try{NotifyBloggersOfNewOffers task = new NotifyBloggersOfNewOffers();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }

    public String runPaybloggerViaCreditCard(){
        try{PaybloggerViaCreditCard task = new PaybloggerViaCreditCard();
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

    public String runSendEmailInvoicePastDue(){
        try{SendEmailInvoicePastDue task = new SendEmailInvoicePastDue();
            task.execute(null);} catch (Exception ex){logger.error(ex);}
        return "sysadminmanuallyrunscheduledtask";
    }


}
