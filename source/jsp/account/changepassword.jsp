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
    <ui:define name="title">Change Password<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <h:form>
            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">




                <h:panelGroup>
                    <h:outputText value="New Password" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputSecret value="#{changePassword.password}" id="password" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="password" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Verify New Password" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputSecret value="#{changePassword.passwordverify}" id="passwordverify" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="passwordverify" styleClass="RED"></h:message>
                </h:panelGroup>




                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <br/><br/>
                    <h:commandButton action="#{changePassword.saveAction}" value="Save New Password" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>


        </h:form>

</ui:define>


</ui:composition>
</html>


