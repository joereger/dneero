<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicFacebookLandingPage" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = Pagez.getUserSession().getPl().getNameforui()+" "+Pagez._Surveys();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <a href="<%=((PublicFacebookLandingPage)Pagez.getBeanMgr().get("PublicFacebookLandingPage")).getUrltoredirectto()%>">Click here please.</a>

<%@ include file="/template/footer.jsp" %>