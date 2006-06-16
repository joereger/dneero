<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<h:form>
    <h:panelGrid columns="3" cellpadding="3" border="0">
        <%-- Email --%>
        <h:panelGroup>
            <h:outputText value="Email"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{Login.email}" id="email" required="true">
                <f:validateLength minimum="3" maximum="255"></f:validateLength>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="email" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Password --%>
        <h:panelGroup>
            <h:outputText value="Password"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputSecret value="#{Login.password}" id="password" required="true">
                <f:validateLength minimum="3" maximum="255"></f:validateLength>
            </h:inputSecret>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="password" styleClass="RED"></h:message>
        </h:panelGroup>

        <%-- Submit Button --%>
        <h:panelGroup>
            <h:commandButton action="#{Login.login}" title="Log In"></h:commandButton>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

    </h:panelGrid>
</h:form>


