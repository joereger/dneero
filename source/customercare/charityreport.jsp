<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.CustomercareCharityReport" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Charity Report";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareCharityReport customercareCharityReportport= (CustomercareCharityReport) Pagez.getBeanMgr().get("CustomercareCharityReport");
%>
<%@ include file="/template/header.jsp" %>


    <%=customercareCharityReportport.getReport()%>


<%@ include file="/template/footer.jsp" %>