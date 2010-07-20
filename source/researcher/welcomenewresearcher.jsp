<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%String jspPageName="/researcher/welcomenewresearcher.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "One-Time Researcher Configuration Complete!";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <font class="smallfont">Your researcher profile is created and ready to roll.  In the future you'll be able to skip this step.</font>
    <br/><br/>
    <a href="/researcher/index.jsp"><font class="mediumfont" style="color: #0000ff;">Click Here to Continue</font></a>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>