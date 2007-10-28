<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Survey Results<br/><br/></ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>

    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont">#{researcherResultsFinancial.survey.title}</font>
        <br/>
        <h:commandLink value="Results Main" action="#{researcherResults.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="#{researcherResultsAnswers.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="#{researcherResultsImpressions.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="#{researcherResultsRespondents.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="#{researcherResultsFinancial.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>
    <br/><br/>



    <h:panelGrid columns="3" cellpadding="3" border="0">
        <h:panelGroup>
            <h:outputText value="Survey Responses to Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherResultsFinancial.sms.responsesToDate}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Spent on Responses to Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            $<h:outputText value="#{researcherResultsFinancial.sms.spentOnResponsesToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Blog Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherResultsFinancial.sms.impressionsToDate}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Spent on Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            $<h:outputText value="#{researcherResultsFinancial.sms.spentOnImpressionsToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Total Spent to Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            $<h:outputText value="#{researcherResultsFinancial.sms.spentToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            $<h:outputText value="#{researcherResultsFinancial.sms.maxPossibleSpend}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Remaining Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            $<h:outputText value="#{researcherResultsFinancial.sms.remainingPossibleSpend}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
    </h:panelGrid>

    
</h:form>

</ui:define>


</ui:composition>
</html>