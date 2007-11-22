<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Note Submitted";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



        Success!  Your comment has been added.
        <br/><br/>
        <a href="sysadminsupportissueslist.jsp">Continue</a>




<%@ include file="/jsp/templates/footer.jsp" %>


