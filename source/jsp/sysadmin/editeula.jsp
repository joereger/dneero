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
    <ui:define name="title">Edit End User License Agreement<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
        <d:authorization acl="systemadmin" redirectonfail="true"/>

        <font class="mediumfont">Be careful here... every edit... even the slightest one... causes every user to have to read and accept the new EULA the next time they log in.</font>
        <br/><br/>

         <h:form id="eulaform">


            <h:messages styleClass="RED"/>
            <h:panelGrid columns="1" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Last Edited: ${sysadminEditEula.date}" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:outputText value="Eulaid: ${sysadminEditEula.eulaid}" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>
            

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{sysadminEditEula.eula}" id="eula" cols="80" rows="25" required="true">
                    </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="eula" styleClass="RED"></h:message>
                </h:panelGroup>





                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminEditEula.edit}" value="Edit the EULA and Force All Users to Re-Accept" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>

         </h:form>

    </ui:define>


</ui:composition>
</html>