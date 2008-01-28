<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.SysadminCharityReport" %>
<%@ page import="com.dneero.htmluibeans.SysadminDemographicReport" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Demographic Report";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminDemographicReport sysadminDemographicReport = (SysadminDemographicReport) Pagez.getBeanMgr().get("SysadminDemographicReport");
%>
<%@ include file="/template/header.jsp" %>


    <%=sysadminDemographicReport.getReport()%>


<%@ include file="/template/footer.jsp" %>