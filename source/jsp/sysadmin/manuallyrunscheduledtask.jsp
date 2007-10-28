<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Manually Run Scheduled Task<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
        <d:authorization acl="systemadmin" redirectonfail="true"/>

         <h:form>

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