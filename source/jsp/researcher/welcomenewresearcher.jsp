<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "One-Time Researcher Configuration Complete!";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <font class="smallfont">Your researcher profile is created and ready to roll.  In the future you'll be able to skip this step.</font>
    <br/><br/>
    <a href="/jsp/researcher/index.jsp"><font class="mediumfont" style="color: #0000ff;">Click Here to Continue</font></a>

<%@ include file="/jsp/templates/footer.jsp" %>