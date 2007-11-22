<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.InviteLandingPage" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Welcome to dNeero!";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

    <%if (((InviteLandingPage) Pagez.getBeanMgr().get("InviteLandingPage")).getReferredby()!=null){%>
        <font class="largefont"><%=((InviteLandingPage) Pagez.getBeanMgr().get("InviteLandingPage")).getReferredby()%> has invited you to make money with your blog!</font>
    <%}%>
    <%if (((InviteLandingPage) Pagez.getBeanMgr().get("InviteLandingPage")).getReferredby()==null){%>
        <font class="largefont">You've been invited to make money with your blog and/or social network!</font>
    <%}%>
    <br/>
    <a href="registration.jsp"><font class="mediumfont">Click here to Sign Up!</font></a>


<%@ include file="/jsp/templates/footer.jsp" %>