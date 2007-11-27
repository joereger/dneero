<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDelete" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Delete Survey";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDelete researcherSurveyDelete = (ResearcherSurveyDelete) Pagez.getBeanMgr().get("ResearcherSurveyDelete");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            researcherSurveyDelete.deleteSurvey();
            Pagez.getUserSession().setMessage("Survey deleted.");
            Pagez.sendRedirect("index.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="/researcher/researchersurveydelete.jsp" method="post">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydelete.jsp">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDelete.getSurvey().getSurveyid()%>">
        <font class="mediumfont">Are you sure you want to delete the survey<br/>"<%=((ResearcherSurveyDelete)Pagez.getBeanMgr().get("ResearcherSurveyDelete")).getTitle()%>"?</font>
        <br/><br/>
        <input type="submit" class="formsubmitbutton" value="Yes, Delete this Survey">
        <br/><br/>
        <a href="/researcher/index.jsp"><font class="subnavfont">Nevermind, Don't Delete this Survey</font></a>
    </form>


<%@ include file="/template/footer.jsp" %>