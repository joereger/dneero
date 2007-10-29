<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Successful";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

            <h:outputText>Email activation was successful!  Your account is ready to roll!  You can now log in with the email address and password that you provided when you signed up.</h:outputText>
            <br/><br/>
            <h:commandButton action="#{login.beginView}" value="Please Log In" styleClass="formsubmitbutton" rendered="#{!userSession.isloggedin}"/>
        </h:form>
    </ui:define>
</ui:composition>
</html>
