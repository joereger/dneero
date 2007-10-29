<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Change Password";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


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


