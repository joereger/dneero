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
    <ui:define name="title">InstanceProps... Be Careful... You Can Break a Lot of Stuff Here... These apply to this machine alone and are saved to a config file.<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
        <d:authorization acl="systemadmin" redirectonfail="true"/>

         <h:form>


            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Instance Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.instancename}" id="instancename" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="instancename" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="dbConnectionUrl" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbConnectionUrl}" id="dbConnectionUrl" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbConnectionUrl" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="dbUsername" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbUsername}" id="dbUsername" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbUsername" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="dbPassword" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbPassword}" id="dbPassword" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbPassword" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="dbMaxActive" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbMaxActive}" id="dbMaxActive" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbMaxActive" styleClass="RED"></h:message>
                </h:panelGroup>
                
                <h:panelGroup>
                    <h:outputText value="dbMaxIdle" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbMaxIdle}" id="dbMaxIdle" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbMaxIdle" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="dbMinIdle" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbMinIdle}" id="dbMinIdle" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbMinIdle" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="dbMaxWait" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbMaxWait}" id="dbMaxWait" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbMaxWait" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="dbDriverName" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.dbDriverName}" id="dbDriverName" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="dbDriverName" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="runScheduledTasksOnThisInstance" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminInstanceProps.runScheduledTasksOnThisInstance}" id="runScheduledTasksOnThisInstance" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="runScheduledTasksOnThisInstance" styleClass="RED"></h:message>
                </h:panelGroup>



                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <br/><br/>
                    <h:commandButton action="#{sysadminInstanceProps.saveProps}" value="Save Instance Props" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>

         </h:form>

    </ui:define>


</ui:composition>
</html>