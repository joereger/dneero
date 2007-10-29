<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "End User License Agreement";
String navtab = "home";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


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


<%@ include file="/jsp/templates/footer.jsp" %>
