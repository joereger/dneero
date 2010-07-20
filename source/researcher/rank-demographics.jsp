<%@ page import="com.dneero.dao.Blogger" %>
<%@ page import="com.dneero.dao.User" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankDemographics" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%String jspPageName="/researcher/rank-demographics.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankDemographics researcherRankDemographics = (ResearcherRankDemographics) Pagez.getBeanMgr().get("ResearcherRankDemographics");
%>
<%@ include file="/template/header.jsp" %>
<font class="mediumfont"><%=researcherRankDemographics.getRank().getName()%></font>
<br/><br/>



    <font class="smallfont" style="color: #cccccc;">Includes all people with a positive ranking. Report generated <%=researcherRankDemographics.getWhengenerated()%>.  It refreshes every 60 minutes.</font>
    <br/><br/>


    <%=researcherRankDemographics.getHtml()%>

    


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>