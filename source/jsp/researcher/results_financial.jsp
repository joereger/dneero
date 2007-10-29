<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



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

    


<%@ include file="/jsp/templates/footer.jsp" %>