<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.helpers.UserInputSafe" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="com.dneero.startup.Log4jLevels" %>
<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Scheduled Tasks";
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
                } else if (request.getParameter("task").equals("UpdateSurveyImpressionsPaid")){
                    sysadminManuallyRunScheduledTask.runUpdateSurveyImpressionsPaid();
                } else if (request.getParameter("task").equals("PurgeDeadUserSessions")){
                    sysadminManuallyRunScheduledTask.runPurgeDeadUserSessions();
                } else if (request.getParameter("task").equals("HtmlCacheFromDbcachePurgeStaleItems")){
                    sysadminManuallyRunScheduledTask.runHtmlCacheFromDbcachePurgeStaleItems();
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


<table cellpadding="0" cellspacing="10" border="0" width="100%">
    <tr>
        <td valign="top">
            <%List schedex = null;
            if (request.getParameter("taskname")==null || request.getParameter("taskname").equals("") || request.getParameter("taskname").equals("null")){
                 schedex= HibernateUtil.getSession().createQuery("from Schedextime order by date desc").setMaxResults(1000).list();
            } else {
                schedex= HibernateUtil.getSession().createQuery("from Schedextime where taskname='"+ UserInputSafe.clean(request.getParameter("taskname"))+"' order by date desc").setMaxResults(1000).list();
            }
            %>

            <%if (schedex==null || schedex.size()==0){%>
                <font class="normalfont">None found.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Task", "<a href=\"/sysadmin/manuallyrunscheduledtask.jsp?taskname=<$taskname$>\"><$taskname$></a>", false, "", "tinyfont"));
                    cols.add(new GridCol("Servername", "<$servername$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Millis", "<$millistoexecute$>", false, "", "tinyfont"));
                %>
                <%=Grid.render(schedex, cols, 250, "/sysadmin/manuallyrunscheduledtask.jsp?taskname="+request.getParameter("taskname"), "page")%>
            <%}%>


        </td>
        <td valign="top" width="20%">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <font class="formfieldnamefont">Scheduled Tasks</font>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CloseSurveysByDate"><font class="tinyfont">CloseSurveysByDate</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CloseSurveysByNumRespondents"><font class="tinyfont">CloseSurveysByNumRespondents</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=NotifyBloggersOfNewOffers"><font class="tinyfont">NotifyBloggersOfNewOffers</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PendingToOpenSurveys"><font class="tinyfont">PendingToOpenSurveys</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=QualityAverager"><font class="tinyfont">QualityAverager</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=MoveMoneyAround"><font class="tinyfont">MoveMoneyAround</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ResearcherRemainingBalanceOperations"><font class="tinyfont">ResearcherRemainingBalanceOperations</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=DeleteOldPersistentlogins"><font class="tinyfont">DeleteOldPersistentlogins</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SocialInfluenceRatingUpdate"><font class="tinyfont">SocialInfluenceRatingUpdate</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SystemStats"><font class="tinyfont">SystemStats</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SendMassemails"><font class="tinyfont">SendMassemails</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ImpressionActivityObjectQueue"><font class="tinyfont">ImpressionActivityObjectQueue</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CharityCalculateAmountDonated"><font class="tinyfont">CharityCalculateAmountDonated</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=UpdateResponsePoststatus"><font class="tinyfont">UpdateResponsePoststatus</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=UpdateResponsePoststatusAll"><font class="tinyfont">UpdateResponsePoststatus(All)</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PayForSurveyResponsesOncePosted"><font class="tinyfont">PayForSurveyResponsesOncePosted</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=ImpressionPayments"><font class="tinyfont">ImpressionPayments</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=SystemStatsFinancial"><font class="tinyfont">SystemStatsFinancial</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PagePerformanceRecordAndFlush"><font class="tinyfont">PagePerformanceRecordAndFlush</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=CurrentBalanceUpdater"><font class="tinyfont">CurrentBalanceUpdater</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=UpdateSurveyImpressionsPaid"><font class="tinyfont">UpdateSurveyImpressionsPaid</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=PurgeDeadUserSessions"><font class="tinyfont">PurgeDeadUserSessions</font></a>
                <br/><a href="/sysadmin/manuallyrunscheduledtask.jsp?action=run&task=HtmlCacheFromDbcachePurgeStaleItems"><font class="tinyfont">HtmlCacheFromDbcachePurgeStaleItems</font></a>
            </div>
        </td>
    </tr>
</table>





<%@ include file="/template/footer.jsp" %>