<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>


<h:form>
    <f:verbatim><br></f:verbatim>
    <h:inputText value="#{BloggerRegistration.email}" id="email" required="true">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
        <t:validateEmail></t:validateEmail>
    </h:inputText>
    <h:message for="email" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <h:inputText value="#{BloggerRegistration.password}" id="password">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="password" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <h:inputText value="#{BloggerRegistration.firstname}" id="firstname">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="firstname" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <h:inputText value="#{BloggerRegistration.lastname}" id="lastname">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="lastname" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <h:commandButton action="#{BloggerRegistration.registerAction}"></h:commandButton>
</h:form>


