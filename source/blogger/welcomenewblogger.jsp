<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "One-Time Configuration is Complete!";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <font class="smallfont">With your profile and blog set up, you're ready to join some conversations!</font>
    <br/><br/>
    <a href="/blogger/index.jsp"><font class="mediumfont" style="color: #0000ff;">Click Here to Continue</font></a>

<%@ include file="/template/footer.jsp" %>