<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "InstanceProps... Be Careful!!!";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



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