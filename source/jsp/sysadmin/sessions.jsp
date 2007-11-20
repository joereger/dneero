<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.SysadminSessions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Active Sessions";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
SysadminSessions sysadminSessions = (SysadminSessions) Pagez.getBeanMgr().get("SysadminSessions");
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <%=sysadminSessions.getSessionsashtml()%>



<%@ include file="/jsp/templates/footer.jsp" %>