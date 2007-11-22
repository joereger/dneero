<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicResultsRespondentsProfile" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Profile";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicResultsRespondentsProfile publicResultsRespondentsProfile = (PublicResultsRespondentsProfile)Pagez.getBeanMgr().get("PublicResultsRespondentsProfile");
%>
<%@ include file="/template/header.jsp" %>

    <font class="smallfont">This page has been displayed in error.  Please try again.  We apologize for the inconvenience.</font>
    <br/><br/><br/>
    <%=publicResultsRespondentsProfile.getDummy()%>

<%@ include file="/template/footer.jsp" %>


