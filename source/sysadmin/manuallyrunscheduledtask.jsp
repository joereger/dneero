<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminManuallyRunScheduledTask" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Manually Run Scheduled Task";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminManuallyRunScheduledTask sysadminManuallyRunScheduledTask = (SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("run")) {
        try {
            if (request.getParameter("task")!=null){
                if (request.getParameter("task").equals("CloseSurveysByDate")){
                    sysadminManuallyRunScheduledTask.runCloseSurveysByDate();
                } else if (request.getParameter("task").equals("CloseSurveysByNumRespondents")){
                    sysadminManuallyRunScheduledTask.runCloseSurveysByNumRespondents();
                } else if (request.getParameter("task").equals("NotifyBloggersOfNewOffers")){
                    sysadminManuallyRunScheduledTask.runNotifyBloggersOfNewOffers();
                } else if (request.getParameter("task").equals("PendingToOpenSurveys")){
                    sysadminManuallyRunScheduledTask.runPendingToOpenSurveys();
                } else if (request.getParameter("task").equals("QualityAverager")){
                    sysadminManuallyRunScheduledTask.runQualityAverager();
                } else if (request.getParameter("task").equals("MoveMoneyAround")){
                    sysadminManuallyRunScheduledTask.runMoveMoneyAround();
                } else if (request.getParameter("task").equals("ResearcherRemainingBalanceOperations")){
                    sysadminManuallyRunScheduledTask.runResearcherRemainingBalanceOperations();
                } else if (request.getParameter("task").equals("DeleteOldPersistentlogins")){
                    sysadminManuallyRunScheduledTask.runDeleteOldPersistentlogins();
                } else if (request.getParameter("task").equals("SocialInfluenceRatingUpdate")){
                    sysadminManuallyRunScheduledTask.runSocialInfluenceRatingUpdate();
                } else if (request.getParameter("task").equals("SystemStats")){
                    sysadminManuallyRunScheduledTask.runSystemStats();
                } else if (request.getParameter("task").equals("SendMassemails")){
                    sysadminManuallyRunScheduledTask.runSendMassemails();
                } else if (request.getParameter("task").equals("ImpressionActivityObjectQueue")){
                    sysadminManuallyRunScheduledTask.runImpressionActivityObjectQueue();
                } else if (request.getParameter("task").equals("CharityCalculateAmountDonated")){
                    sysadminManuallyRunScheduledTask.runCharityCalculateAmountDonated();
                } else if (request.getParameter("task").equals("UpdateResponsePoststatus")){
                    sysadminManuallyRunScheduledTask.runUpdateResponsePoststatus();
                } else if (request.getParameter("task").equals("UpdateResponsePoststatusAll")){
                    sysadminManuallyRunScheduledTask.runUpdateResponsePoststatusForAll();
                } else if (request.getParameter("task").equals("PayForSurveyResponsesOncePosted")){
                    sysadminManuallyRunScheduledTask.runPayForSurveyResponsesOncePosted();
                } else if (request.getParameter("task").equals("ImpressionPayments")){
                    sysadminManuallyRunScheduledTask.runImpressionPayments();
                } else if (request.getParameter("task").equals("SystemStatsFinancial")){
                    sysadminManuallyRunScheduledTask.runSystemStatsFinancial();
                } else if (request.getParameter("task").equals("PagePerformanceRecordAndFlush")){
                    sysadminManuallyRunScheduledTask.runPagePerformanceRecordAndFlush();
                } else if (request.getParameter("task").equals("CurrentBalanceUpdater")){
                    sysadminManuallyRunScheduledTask.runCurrentBalanceUpdater();
                } else {
                    throw new ValidationException("task not found.");    
                }
            } else {
                throw new ValidationException("task not defined.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CloseSurveysByDate"><font class="mediumfont">CloseSurveysByDate</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CloseSurveysByNumRespondents"><font class="mediumfont">CloseSurveysByNumRespondents</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=NotifyBloggersOfNewOffers"><font class="mediumfont">NotifyBloggersOfNewOffers</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PendingToOpenSurveys"><font class="mediumfont">PendingToOpenSurveys</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=QualityAverager"><font class="mediumfont">QualityAverager</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=MoveMoneyAround"><font class="mediumfont">MoveMoneyAround</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ResearcherRemainingBalanceOperations"><font class="mediumfont">ResearcherRemainingBalanceOperations</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=DeleteOldPersistentlogins"><font class="mediumfont">DeleteOldPersistentlogins</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SocialInfluenceRatingUpdate"><font class="mediumfont">SocialInfluenceRatingUpdate</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SystemStats"><font class="mediumfont">SystemStats</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SendMassemails"><font class="mediumfont">SendMassemails</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ImpressionActivityObjectQueue"><font class="mediumfont">ImpressionActivityObjectQueue</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CharityCalculateAmountDonated"><font class="mediumfont">CharityCalculateAmountDonated</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=UpdateResponsePoststatus"><font class="mediumfont">UpdateResponsePoststatus</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=UpdateResponsePoststatusAll"><font class="mediumfont">UpdateResponsePoststatus(All)</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PayForSurveyResponsesOncePosted"><font class="mediumfont">PayForSurveyResponsesOncePosted</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ImpressionPayments"><font class="mediumfont">ImpressionPayments</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SystemStatsFinancial"><font class="mediumfont">SystemStatsFinancial</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PagePerformanceRecordAndFlush"><font class="mediumfont">PagePerformanceRecordAndFlush</font></a>
<br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CurrentBalanceUpdater"><font class="mediumfont">CurrentBalanceUpdater</font></a>



<%@ include file="/template/footer.jsp" %>