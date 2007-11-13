<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Sent";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

            Your email activation message has been sent.  Please check your email inbox.
            <br/><br/>
            Also, please note that all previous activation emails are now invalid... you must use the most recent one that we've sent.  This is for your security.  Thanks!


<%@ include file="/jsp/templates/footer.jsp" %>
