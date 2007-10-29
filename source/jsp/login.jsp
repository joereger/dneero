<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Log In";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{login.email}" id="email" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="email" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputSecret value="#{login.password}" id="password" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                    <f:verbatim><br/></f:verbatim>
                    <h:commandLink value="Lost your password?" action="#{lostPassword.beginView}" immediate="true" styleClass="tinyfont" style="color: #000000;"/>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="password" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox value="#{login.keepmeloggedin}" id="keepmeloggedin"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Stay Logged In?</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="keepmeloggedin" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{login.login}" value="Log In" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>


            </h:panelGrid>
        </h:form>

    </ui:define>
</ui:composition>
</html>
