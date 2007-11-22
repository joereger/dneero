<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Successful";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

            Email activation was successful!  Your account is ready to roll!  You can now log in with the email address and password that you provided when you signed up.
            <br/><br/>
            <%if (!Pagez.getUserSession().getIsloggedin()){%>
                <a href="login.jsp">Please Log In Now</a>
            <%}%>


<%@ include file="/jsp/templates/footer.jsp" %>
