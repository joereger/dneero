<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" align=\"right\" alt=\"\" border=\"0\"/><%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getFirstname()%> <%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getLastname()%>'s Answers<br/><br clear=\"all\"/>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="mediumfont" style="color: #cccccc;"><%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getSurvey().getTitle()%></font>
    <br/><br/>




    <f:verbatim escape="false"><%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getResultsashtml()%></f:verbatim>

    


<%@ include file="/jsp/templates/footer.jsp" %>