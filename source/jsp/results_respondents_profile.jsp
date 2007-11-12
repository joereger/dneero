<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Profile";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>

    <font class="smallfont">This page has been displayed in error.  Please try again.  We apologize for the inconvenience.</font>
    <br/><br/><br/>
    <%=((PublicResultsRespondentsProfile)Pagez.getBeanMgr().get("PublicResultsRespondentsProfile")).getDummy()%>




<%@ include file="/jsp/templates/footer.jsp" %>


