<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Manually Run Scheduled Task";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



            <table cellpadding="0" cellspacing="0" border="0">
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunCloseSurveysByDate()%>" value="CloseSurveysByDate" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunCloseSurveysByNumRespondents()%>" value="CloseSurveysByNumRespondents" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunNotifyBloggersOfNewOffers()%>" value="NotifyBloggersOfNewOffers" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunPendingToOpenSurveys()%>" value="PendingToOpenSurveys" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunQualityAverager()%>" value="QualityAverager" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunMoveMoneyAround()%>" value="MoveMoneyAround" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunResearcherRemainingBalanceOperations()%>" value="ResearcherRemainingBalanceOperations" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunDeleteOldPersistentlogins()%>" value="DeleteOldPersistentlogins" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunSocialInfluenceRatingUpdate()%>" value="SocialInfluenceRatingUpdate" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunSystemStats()%>" value="SystemStats" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunSendMassemails()%>" value="SendMassemails" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunImpressionActivityObjectQueue()%>" value="ImpressionActivityObjectQueue" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunCharityCalculateAmountDonated()%>" value="CharityCalculateAmountDonated" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunUpdateResponsePoststatus()%>" value="UpdateResponsePoststatus" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunUpdateResponsePoststatusForAll()%>" value="UpdateResponsePoststatus(All)" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunPayForSurveyResponsesOncePosted()%>" value="PayForSurveyResponsesOncePosted" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunImpressionPayments()%>" value="ImpressionPayments" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminManuallyRunScheduledTask)Pagez.getBeanMgr().get("SysadminManuallyRunScheduledTask")).getRunSystemStatsFinancial()%>" value="SystemStatsFinancial" styleClass="formsubmitbutton"></h:commandButton>
                </td>
            </table>



<%@ include file="/jsp/templates/footer.jsp" %>