<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Active Sessions";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <f:verbatim escape="false">#{sysadminSessions.sessionsashtml}</f:verbatim>


    </ui:define>


</ui:composition>
</html>