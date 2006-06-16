<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<h:form>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Email: </f:verbatim>
    <h:inputText value="#{Registration.email}" id="email" required="true">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
        <t:validateEmail></t:validateEmail>
    </h:inputText>
    <h:message for="email" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Password: </f:verbatim>
    <h:inputText value="#{Registration.password}" id="password">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="password" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>First Name: </f:verbatim>
    <h:inputText value="#{Registration.firstname}" id="firstname">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="firstname" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Last Name: </f:verbatim>
    <h:inputText value="#{Registration.lastname}" id="lastname">
        <f:validateLength minimum="3" maximum="255"></f:validateLength>
    </h:inputText>
    <h:message for="lastname" styleClass="RED"></h:message>
    <f:verbatim><br></f:verbatim>
    <h:commandButton action="#{Registration.registerAction}" title="Create Account"></h:commandButton>
</h:form>


