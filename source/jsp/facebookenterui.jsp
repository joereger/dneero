<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicFacebookenterui" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Enter Facebook App";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <a href="<%=((com.dneero.htmluibeans.PublicFacebookenterui)Pagez.getBeanMgr().get("PublicFacebookenterui")).getUrl()%>">Please click here to continue.</a>

<%@ include file="/jsp/templates/footer.jsp" %>
