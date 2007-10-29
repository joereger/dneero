<%@ page import="com.dneero.htmlui.Textbox" %>
<%@ page import="com.dneero.htmluibeans.Test" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "HtmlUiBean";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

<form action="" method="get">
    <%=Textbox.getHtml("test", "Test.test", 255, 35, "", "font-size: 14px; background: #ffcc00;")%>
<input type="submit">
</form>
<br/><br/>
hasbeeninitialized=<%=((Test) Pagez.getBeanMgr().get("Test")).getHasbeeninitialized()%>
<br/><br/>
Pagez.getUserSession().getPendingSurveyResponseAsString()=<%=Pagez.getUserSession().getPendingSurveyResponseAsString()%>

<%@ include file="/jsp/templates/footer.jsp" %>