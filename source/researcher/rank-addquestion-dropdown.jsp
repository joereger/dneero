<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanels" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Custom Rankings";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankAddquestionDropdown researcherRankAddquestionDropdown =(ResearcherRankAddquestionDropdown) Pagez.getBeanMgr().get("ResearcherRankAddquestionDropdown");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("dosomething")) {
        try {
            researcherRankAddquestionDropdown.saveAction();
            Pagez.getUserSession().setMessage("Panel deleted.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>








<%@ include file="/template/footer.jsp" %>

