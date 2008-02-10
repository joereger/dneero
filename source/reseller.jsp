<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "dNeero Reseller Program";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <%@ include file="/reseller-description.jsp" %> 


<%@ include file="/template/footer.jsp" %>
