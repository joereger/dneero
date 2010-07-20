<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%String jspPageName="/facebookexitui.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Exit Facebook App";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <a href="<%=BaseUrl.get(true)%>">Please click here to continue.</a>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>
