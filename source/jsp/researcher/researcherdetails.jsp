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
    <ui:define name="title">Researcher Profile</ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <t:div rendered="#{userSession.isloggedin and (userSession.user.researcherid eq 0)}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">One-time Researcher Configuration Step 1 of 1: Profile Setup</font>
            <br/>
            <font class="smallfont">We must take your profile so that we can provide you the highest level of service.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    </t:div>


    <h:form id="researcherdetails">


            <h:panelGrid columns="3" cellpadding="3" border="0">


                <h:panelGroup>
                    <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Could be company name.  Just something to identify your research or marketing organization." styleClass="tinyfont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{researcherDetails.companyname}" id="companyname"></h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="companyname" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Type" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Choose the option that best describes you/your organization." styleClass="tinyfont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectOneMenu value="#{researcherDetails.companytype}" id="companytype" required="true">
                        <f:selectItems value="#{researcherDetails.companytypes}"/>
                    </h:selectOneMenu>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="companytype" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Phone (Optional)" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">So we can contact you regarding<br/>billing and survey issues.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{researcherDetails.phone}" id="phone" required="false"></h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="phone" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{researcherDetails.saveAction}" value="Save Researcher Profile" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>




        </h:form>

</ui:define>


</ui:composition>
</html>

