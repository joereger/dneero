<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Manually Run Scheduled Task";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



            <h:panelGrid columns="1" cellpadding="3" border="0">
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runCloseSurveysByDate}" value="CloseSurveysByDate" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runCloseSurveysByNumRespondents}" value="CloseSurveysByNumRespondents" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runNotifyBloggersOfNewOffers}" value="NotifyBloggersOfNewOffers" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runPendingToOpenSurveys}" value="PendingToOpenSurveys" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runQualityAverager}" value="QualityAverager" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runMoveMoneyAround}" value="MoveMoneyAround" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runResearcherRemainingBalanceOperations}" value="ResearcherRemainingBalanceOperations" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runDeleteOldPersistentlogins}" value="DeleteOldPersistentlogins" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runSocialInfluenceRatingUpdate}" value="SocialInfluenceRatingUpdate" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runSystemStats}" value="SystemStats" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runSendMassemails}" value="SendMassemails" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runImpressionActivityObjectQueue}" value="ImpressionActivityObjectQueue" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runCharityCalculateAmountDonated}" value="CharityCalculateAmountDonated" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runUpdateResponsePoststatus}" value="UpdateResponsePoststatus" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runUpdateResponsePoststatusForAll}" value="UpdateResponsePoststatus(All)" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runPayForSurveyResponsesOncePosted}" value="PayForSurveyResponsesOncePosted" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runImpressionPayments}" value="ImpressionPayments" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminManuallyRunScheduledTask.runSystemStatsFinancial}" value="SystemStatsFinancial" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
            </h:panelGrid>

         </h:form>

    </ui:define>


</ui:composition>
</html>