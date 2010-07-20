<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicFacebookenterui" %>
<%String jspPageName="/facebookenterui.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Enter Facebook App";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <a href="<%=((com.dneero.htmluibeans.PublicFacebookenterui)Pagez.getBeanMgr().get("PublicFacebookenterui")).getUrl()%>">Please click here to continue.</a>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>
