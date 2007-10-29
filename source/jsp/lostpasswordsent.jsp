<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Password Sent";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:outputText>Please check your email... we have sent a password reset link.</h:outputText>


<%@ include file="/jsp/templates/footer.jsp" %>
