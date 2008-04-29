<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Note Submitted";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



        Success!  Your comment has been added.
        <br/><br/>
        <a href="/customercare/sysadminsupportissueslist.jsp">Continue</a>




<%@ include file="/template/footer.jsp" %>


