<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit EULA";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



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