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
    <ui:define name="title">Edit Panel</ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>
    


    <h:form id="panels">
        <t:saveState id="save" value="#{researcherPanelsEdit}"></t:saveState>

        <h:panelGrid columns="3" cellpadding="3" border="0">
                <h:panelGroup>
                    <h:outputText value="Panel Name" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{researcherPanelsEdit.panel.name}" id="panelname"></h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="panelname" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{researcherPanelsEdit.edit}" value="Edit Panel" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>

    </h:form>

</ui:define>


</ui:composition>
</html>


