<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Awaiting Email Activation";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:outputText>Your account has not yet been activated by email.  It must be activated by email within 3 days after signup.  Please check your email inbox and click the link that we sent you to begin the activation process. (Also check your Junk Mail and/or Spam folders... sometimes email clients put the message there.)</h:outputText>
            <f:verbatim><br/><br/></f:verbatim>
            <h:commandLink action="#{emailActivationResend.beginView}" value="Click here to re-send your email activation message."></h:commandLink>
        </h:form>
    </ui:define>
</ui:composition>
</html>
