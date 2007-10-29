<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



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


