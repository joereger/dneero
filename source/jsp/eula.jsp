<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicEula" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Terms of Use and Privacy Statement";
String navtab = "home";
String acl = "public";
%>
<%
PublicEula publicEula = (PublicEula)Pagez.getBeanMgr().get("PublicEula");
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <center>
    <textarea rows="15" cols="80"><%=publicEula.getEula()%></textarea>
    </center>

<%@ include file="/jsp/templates/footer.jsp" %>
