<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>

<h:form>
    <h:panelGrid columns="3" cellpadding="3" border="1">
        <%-- Title --%>
        <h:panelGroup>
            <h:outputText value="Offer Title"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{NewOffer.title}" id="title" required="true">
                <f:validateLength minimum="3" maximum="255"></f:validateLength>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="title" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Description --%>
        <h:panelGroup>
            <h:outputText value="Description"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputTextarea value="#{NewOffer.description}" id="description" required="true">
                <f:validateLength minimum="3" maximum="50000"></f:validateLength>
            </h:inputTextarea>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="description" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Willingtopayperrespondent --%>
        <h:panelGroup>
            <h:outputText value="Willing to Pay Per Respondent"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{NewOffer.willingtopayperrespondent}" id="willingtopayperrespondent" required="true">
                <%--<f:converter converterId="javax.faces.convert.DoubleConverter"/>--%>
                <f:validateDoubleRange minimum="0" maximum="10000"></f:validateDoubleRange>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="willingtopayperrespondent" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Numberofrespondentsrequested --%>
        <h:panelGroup>
            <h:outputText value="Number of Respondents Requested"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{NewOffer.numberofrespondentsrequested}" id="numberofrespondentsrequested" required="true">
                <%--<f:converter converterId="javax.faces.convert.IntegerConverter"/>--%>
                <f:validateDoubleRange minimum="100" maximum="10000000"></f:validateDoubleRange>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="numberofrespondentsrequested" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Startdate --%>
        <h:panelGroup>
            <h:outputText value="Start Date"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <t:inputDate value="#{NewOffer.startdate}"  type="both" popupCalendar="true" id="startdate" required="true"></t:inputDate>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="startdate" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Enddate --%>
        <h:panelGroup>
            <h:outputText value="End Date"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <t:inputDate value="#{NewOffer.enddate}" type="both" popupCalendar="true" id="enddate" required="true"></t:inputDate>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="enddate" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Submit Button --%>
        <h:panelGroup>
            <h:commandButton action="#{NewOffer.createNewOffer}" title="Create Offer"></h:commandButton>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

    </h:panelGrid>
</h:form>