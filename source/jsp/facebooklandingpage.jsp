<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicFacebookLandingPage" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <a href="<%=((PublicFacebookLandingPage)Pagez.getBeanMgr().get("PublicFacebookLandingPage")).getUrltoredirectto()%>">Click here please.</a>

<%@ include file="/jsp/templates/footer.jsp" %>