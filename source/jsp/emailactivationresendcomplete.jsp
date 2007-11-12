<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Sent";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

            <h:outputText>Your email activation message has been sent.  Please check your email inbox.</h:outputText>
            <f:verbatim><br/><br/></f:verbatim>
            <h:outputText>Also, please note that all previous activation emails are now invalid... you must use the most recent one that we've sent.  This is for your security.  Thanks!</h:outputText>


<%@ include file="/jsp/templates/footer.jsp" %>
