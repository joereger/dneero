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
    <ui:define name="title">End User License Agreement<br/><br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="false"/>


    <t:div rendered="#{param.msg eq 'autologin'}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Your previous session timed out so you've been logged-in automatically!</font>
        </div>
    </t:div>

        <h:form id="eulaform">
            <h:messages styleClass="RED"/>
            <h:panelGrid columns="1" cellpadding="3" border="0">

                <h:panelGroup>
                    <font class="formfieldnamefont">The End User License Agreement has changed.<br/>You must read and agree to it before you can proceed:</font>
                </h:panelGroup>


                <h:panelGroup>
                    <h:commandButton action="#{loginAgreeNewEula.agree}" value="I Agree to the EULA" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>


                <h:panelGroup>
                    <h:message for="eula" styleClass="RED"></h:message>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{loginAgreeNewEula.eula}" id="eula" cols="80" rows="25" required="true">
                    </h:inputTextarea>
                </h:panelGroup>


            </h:panelGrid>
        </h:form>

    </ui:define>
</ui:composition>
</html>