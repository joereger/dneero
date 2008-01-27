<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.SysadminCharityReport" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Charity Report";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminCharityReport sysadminCharityReport = (SysadminCharityReport) Pagez.getBeanMgr().get("SysadminCharityReport");
%>
<%@ include file="/template/header.jsp" %>


    <%=sysadminCharityReport.getReport()%>


<%@ include file="/template/footer.jsp" %>