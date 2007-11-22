<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDelete" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Delete Survey";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
ResearcherSurveyDelete researcherSurveyDelete = (ResearcherSurveyDelete) Pagez.getBeanMgr().get("ResearcherSurveyDelete");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            researcherSurveyDelete.deleteSurvey();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <form action="researchersurveydelete.jsp" method="post">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDelete.getSurvey().getSurveyid()%>">
        <font class="mediumfont">Are you sure you want to delete the survey<br/>"<%=((ResearcherSurveyDelete)Pagez.getBeanMgr().get("ResearcherSurveyDelete")).getTitle()%>"?</font>
        <br/><br/>
        <input type="submit" value="Yes, Delete this Survey">
        <br/><br/>
        <a href="index.jsp"><font class="subnavfont">Nevermind, Don't Delete this Survey</font></a>
    </form>


<%@ include file="/jsp/templates/footer.jsp" %>