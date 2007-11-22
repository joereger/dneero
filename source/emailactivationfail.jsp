<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Failed";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

            Email activation failed.
            <br/><br/>
            <a href="emailactivationresend.jsp">Click here to re-send email activation message.</a>


<%@ include file="/jsp/templates/footer.jsp" %>
