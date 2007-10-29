<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <a href="#{publicFacebookLandingPage.urltoredirectto}">Click here please.</a>
    <br/>
    <br/>
    userSession.isfacebookui=#{userSession.isfacebookui}




<%@ include file="/jsp/templates/footer.jsp" %>