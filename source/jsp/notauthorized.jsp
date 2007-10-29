<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Not Authorized";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:outputText>You are not authorized to view that resource.</h:outputText>


<%@ include file="/jsp/templates/footer.jsp" %>