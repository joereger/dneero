<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <a href="<%=((PublicFacebookLandingPage)Pagez.getBeanMgr().get("PublicFacebookLandingPage")).getUrltoredirectto()%>">Click here please.</a>
    <br/>
    <br/>
    userSession.isfacebookui=<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getIsfacebookui()%>




<%@ include file="/jsp/templates/footer.jsp" %>