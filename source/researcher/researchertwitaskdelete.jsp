<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDelete" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/researchertwitaskdelete.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Delete Twitter Question";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDelete researcherTwitaskDelete= (ResearcherTwitaskDelete) Pagez.getBeanMgr().get("ResearcherTwitaskDelete");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            researcherTwitaskDelete.deleteTwitask();
            Pagez.getUserSession().setMessage("Twitter Question deleted.");
            Pagez.sendRedirect("/researcher/index.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="/researcher/researchertwitaskdelete.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/researchertwitaskdelete.jsp">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDelete.getTwitask().getTwitaskid()%>">
        <font class="mediumfont">Are you sure you want to delete the Twitter Question<br/>"<%=researcherTwitaskDelete.getTwitask().getQuestion()%>"?</font>
        <br/><br/>
        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Yes, Delete this Twitter Question">
        <br/><br/>
        <a href="/researcher/index.jsp"><font class="subnavfont">Nevermind, Don't Delete</font></a>
    </form>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>