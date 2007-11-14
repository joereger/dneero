<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicProfileAnswers" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" align=\"right\" alt=\"\" border=\"0\"/>"+((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getFirstname()+" "+ ((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getLastname()+"'s Answers<br/><br clear=\"all\"/>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
PublicProfileAnswers publicProfileAnswers = (PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers");
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <font class="mediumfont" style="color: #cccccc;"><%=publicProfileAnswers.getSurvey().getTitle()%></font>
    <br/><br/>
    <%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getResultsashtml()%>

<%@ include file="/jsp/templates/footer.jsp" %>